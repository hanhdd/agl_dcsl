package jda.modules.mbsl.model.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jda.modules.common.Toolkit;
import jda.modules.common.exceptions.ConstraintViolationException;
import jda.modules.common.exceptions.NotPossibleException;
import jda.modules.dcsl.syntax.DOpt;
import jda.modules.mbsl.model.appmodules.ModuleAct;
import jda.mosa.controller.ControllerBasic;
import jda.mosa.module.ModuleService;

/**
 * @overview 
 *  Represents activity nodes.
 *  
 * @author Duc Minh Le (ducmle)
 *
 * @version 4.0
 */
public class Node {
//  private static Logger logger = (Logger) LoggerFactory.getLogger(Node.class.getSimpleName());
  
  // v5.6.0: added id 
  private int id;
  private static int idCounter = 0;
  
  private String label;
  private Class refCls;
  private Class serviceCls;
  private List<ModuleAct> actSeq;
  private List<Edge> out;
  //private boolean transformResult;
  
  ///// derived attributes
  /**derived from {@link #refCls}*/
  private ModuleService refModuleService;
  
  /** derived from the complete status of {@link #exec(Node, ControllerBasic, Object...)}*/
  private boolean stopped;
  
  private List<ModuleAct> filterActs;
  
  public Node(String label, Class refCls, Class serviceCls) {
    this.id = nextID(null);
    this.label = label;
    this.refCls = refCls;
    this.serviceCls = serviceCls;
  }

  @DOpt(type=DOpt.Type.AutoAttributeValueGen)
  private static int nextID(Integer currID) {
  // automatically generate the next student id
    if (currID == null) { // generate one
      idCounter++;
      return idCounter;
    } else { // update
      int num = currID.intValue();
      
      if (num > idCounter) {
        idCounter=num;
      }   
      return currID;
    }
  }
  
  /**
   * @modifies this.{@link #actSeq}
   * @effects 
   *   set this.{@link #actSeq} to contain those in <tt>actSeq</tt> (removing any existing 
   *   entries of {@link #actSeq}) 
   */
  public void setActSeq(List<ModuleAct> actSeq) {
    if (actSeq != null) {
      this.actSeq = new ArrayList<>();
      for (ModuleAct opt : actSeq) {
        // v5.6: see if there are filter actions
        if (opt.isFilterAct()) {
          if (this.filterActs == null) filterActs = new ArrayList<>();
          filterActs.add(opt);
        } else {
          this.actSeq.add(opt);          
        }
      }
    } else {
      this.actSeq = null;
    }
  }

  /**
   * @effects return out
   */
  public List<Edge> getOut() {
    return out;
  }

  /**
   * @modifies this.{@link #out}
   * @effects 
   *  if this.{@link #out} does not contain e
   *    add e to this.{@link #out} (initialise it to be empty first if needed)
   *  else
   *    do nothing
   */
  public void addOutEdge(Edge e) {
    if (out == null) out = new ArrayList<>();
    
    if (!out.contains(e)) {
      out.add(e);
    }
  }
  
  /**
   * @effects 
   *  return {@link #refCls}
   */
  public Class getRefCls() {
    return refCls;
  }
  

  /**
   * @effects 
   *  set this.{@link #refModuleService} = refModuleService
   * @version 5.6 
   * 
   */
  public void setRefModuleService(ModuleService refModuleService) {
    this.refModuleService = refModuleService;
  }


  /**
   * @effects 
   *  return this.{@link #refModuleService}
   * @version 5.6 
   * 
   */
  public ModuleService getRefModuleService() {
    return this.refModuleService;
  }
  
  /**
   * @effects 
   *  return this.label
   */
  public String getLabel() {
    return label;
  }
  
//  /**
//   * @effects 
//   *  set this.{@link #transformResult} = transformResult 
//   */
//  public void setTransformResult(boolean transformResult) {
//    this.transformResult = transformResult;
//  }
//
//  /**
//   * @effects 
//   *  if this is configured to transform result on behalf of the outgoing edge
//   *    return true
//   *  else
//   *    return false 
//   */
//  public boolean isTransformResult() {
//    return transformResult;
//  }
//
//  /**
//   * @requires 
//   *  {@link #isTransformResult()} = true
//   * @effects 
//   *  if this supports transformation (i.e. {@link #isJoinRefClass()} = true)
//   *    transform input into an {@link Object} that is to be used as the output token
//   *  else
//   *    return <tt>input</tt>
//   */
//  protected Object transformResult(Object[] input) {
//    return input;
//  }
  
  /**
   * @effects 
   *  executes this using <tt>actMService</tt> as the application handle.  
   *  
   *  <p>throws NotPossibleException if failed  
   */
  public void exec(Node src, ModuleService actMService, Object...args) throws NotPossibleException {
    stopped = false;
    
    // (1)
    validate();

    // (2)
    execReceive(src, actMService, args);
    
    // (3) 
    Object[] results = execSelf(src, actMService, args);
    
    // (4)
    execOffer(src, actMService, args, results);
  }
  
  /**
   * @modifies <tt>args</stt>
   * @effects 
   *  receive tokens offered by <tt>src</tt>
   *  
   */
  protected void execReceive(Node src, ModuleService actMService, Object...args) {
    // nothing to do here
    if (filterActs != null) {
      filterActs.forEach(filter -> {
        Object[] filteredArgs = filter.getFilterObject().eval(args); }
      );
    }
  }
  
  /**
   * <b>Note:</b> certain GUI-related tasks that are performed by this operation 
   * need to be synchronised so that they would not create conflicts
   * when multiple {@link Node}s are running at the same time. 
   * 
   * @effects 
   *  execute the logic encapsulated by this and return the result as {@link Object}[].
   *  
   *  <p>If no result is produced then an empty array is returned.
   */
  protected Object[] execSelf(Node src, ModuleService actMService, Object...args) throws NotPossibleException {
    
    List<Object> results = new ArrayList<>();
    
    /* This part involves activating the view and performing module operations on it.
     * We therefore need to synchronise this code to avoid potential conflicts 
     *  when multiple nodes are executing at the same time. 
     *  Why? because technically speaking only one view can be activated at any given time!
     */
    synchronized(this) {
      activateRefModuleService(actMService);
  
      if (actSeq != null) { // execute operations     
        //results = new ArrayList<>();//new Object[optSeq.size()];
        //int i = 0;
        
        // execute the operations sequence...
        for (ModuleAct opt : actSeq) {
          opt.exec(refModuleService, args);
        }
        
        // wait for all operations to complete
        for (ModuleAct opt : actSeq) {
          while (!opt.isStopped()) {
            Toolkit.sleep(100);
          }
          
          // finished...obtain result
          //results[i++] = opt.getResult();
          Object result = opt.getResult();
          if (result != null) {
            results.add(result);
          }
        }
      }
    } // end synchronized
    
    // flag stopped to true
    if (!stopped)
      stopped = true;
    
    return results.toArray(new Object[results.size()]);
  }

  /**
   * @effects 
   *  offer tokens to outgoing edges 
   * @version 
   * - 5.6: support coordinator node
   */
  protected void execOffer(Node src, ModuleService actMService, Object[] args, Object...results) throws NotPossibleException {
    // execute outgoing edges
    // TODO: support multiple outgoing edges for action nodes?
    // (control nodes are handled separately by each such type of node)
    if (out != null) {
//      Object transfResult;
//      if (isTransformResult()) {
//        transfResult = transformResult(results);
//      } else {
//        transfResult = results;
//      }
      
      // normal node: pass its data long
      out.get(0).exec(actMService, results);
    }    
  }

  /**
   * @modifies this.{@link #refModuleService}
   * @effects <pre> 
   *  if {@link #refModuleService} has not been initialised
   *    initialise it to be a module service based on {@link #serviceCls} or {@link #refCls}
   *  
   *  if {@link #refModuleService} has view
   *    activite its view
   *  </pre>
   */
  protected void activateRefModuleService(final ModuleService actMService) {
    
    /* v5.6: if specified for this node, it must have already been set by setRefModuleService
    if (refModuleService == null) {
      // v5.6: should not happen, because we must have set it through setRefModuleService...
      // refModuleService has not been initialised, initialise it
      if (serviceCls != null) {
        if (actMService.isDataService(serviceCls)) {
          // data controller service
          // look up the descendant data service of refCls in the containment hierarchy of actMService's module
          refModuleService = actMService.getModule().getDescendantDataService(refCls);
        } else {
          // controller service
          refModuleService = actMService.getContext().lookUpPrimaryService(refCls);
        }
      }
    }
    */
    
    if (refModuleService != null) {
      if (refModuleService.hasView()) refModuleService.activateView();
    } else {
      System.err.printf("%s: referenced module service not specified BUT requested to activate.%n", toString());
    }
  }
  
  /**
   * @effects 
   *  if this is not valid w.r.t rules below
   *    throws {@link ConstraintViolationException}
   *  else
   *    do nothing
   * @rules <pre>
   *  (1) {@link #refCls} != null /\ this is not a control node => {@link #actSeq} != null
   *  (2) {@link #refCls} != null /\ this is not a control node => {@link #serviceCls} != null
   * </pre>
   *  
   */
  protected void validate() throws ConstraintViolationException {
    if (refCls != null && !isControlType()) {
      if (actSeq == null)
        throw new ConstraintViolationException(ConstraintViolationException.Code.OBJECT_STATE_VIOLATES_RULE, new Object[] {this, "(1)"}); // rule 1
      
      if (serviceCls == null)
        throw new ConstraintViolationException(ConstraintViolationException.Code.OBJECT_STATE_VIOLATES_RULE, new Object[] {this, "(2)"}); 
    }
    
  }

  /**
   * @effects 
   *  if this is a {@link ControlNode}
   *    return true
   *  else
   *    return false
   */
  private boolean isControlType() {
    return (this instanceof ControlNode);
  }

  
  /**
   * @effects set stopped = stopped
   */
  public void setStopped(boolean stopped) {
    this.stopped = stopped;
  }

  /**
   * @effects
   *  if {@link #exec(Node, ControllerBasic, Object...)} has completed
   *    return true
   *  else
   *    return false 
   */
  public boolean isStopped() {
    return stopped;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public String toString() {
    return getClass().getSimpleName()+String.format("(%d, %s)", id, label);
  }

  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Node other = (Node) obj;
    return id == other.id;
  }

}
