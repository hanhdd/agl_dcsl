package jda.modules.mbsl.model.graph;

import jda.modules.common.exceptions.NotPossibleException;
import jda.modules.mbsl.model.util.Merge;
import jda.mosa.module.ModuleService;

/**
 * @overview 
 *  Represents merge nodes.
 *  
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 * - 4.0: created <br>
 * - 5.6: updated
 */
public class MergeNode extends ControlNode {

  private static Merge merge;

  /**
   * @effects 
   *
   * @version 
   */
  public MergeNode(String label, Class refCls, Class serviceCls) {
    super(label, refCls, serviceCls);
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see domainapp.modules.activity.model.graph.Node#exec(domainapp.basics.core.ControllerBasic, java.lang.Object[])
   */
  /**
   * @effects 
   *  invoke super.{@link #exec(Node, ModuleService, Object...)} /\ 
   *  (> out[0].exec())
   */
  @Override
  public void exec(Node src, ModuleService actMService, Object... args)
      throws NotPossibleException {
    /* v5.6: use a different logic
    setStopped(false);

    //(1) 
    validate();
    
    // (2)
    execReceive(src, actMService, args);
    
    // (3) & (4): execSelf, execOffer
    
    activateRefModuleService(actMService);
    
    setStopped(true);

    // execOffer: do out[0].exec()
    getOut().get(0).exec(actMService, args);
    */
    
    setStopped(false);

    //(1) 
    validate();
    
    // (2)
    execReceive(src, actMService, args);
    
    // (3) & (4): execSelf, execOffer
    
    // uncomment this if MergeNode has a view
    activateRefModuleService(actMService);

    // execSelf: requests refCls to evaluate the decision
    Merge merge = getMergeInstance(getMergeClass());
    
    Object[] offerings = merge.evaluate(this, src, args);
    /* alternatively: use this when Edge.guard is used instead
    for (Edge e : out) {
      if (e.isSat(args)) {
        e.exec(actMService, args);  
        break;  // at most one guard is checked to be true
      }
    }
    */

    setStopped(true);

    // execOffer
    if (offerings != null) {
      getOut().get(0).exec(actMService, offerings);
    }
  }  
  
  /**
   * @effects 
   *  return super.{@link #getRefCls()} casted to <tt>Class&lt;Merge&gt;</tt>
   */
  public Class<Merge> getMergeClass() {
    return (Class<Merge>) getRefCls();
  }

  /**
   * @effects <pre>
   *  if {@link #merge} has not been initialised
   *    initialise it
   *    throws {@link NotPossibleException} if failed
   *  
   *  return {@link #merge}
   *  </pre>
   */
  private static Merge getMergeInstance(final Class<Merge> mergeCls) throws NotPossibleException {
    if (merge == null) {
      try {
        merge = mergeCls.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        throw new NotPossibleException(NotPossibleException.Code.FAIL_TO_CREATE_OBJECT, new Object[] {mergeCls.getSimpleName(), ""});
      }
    }
    
    return merge;
  }
}
