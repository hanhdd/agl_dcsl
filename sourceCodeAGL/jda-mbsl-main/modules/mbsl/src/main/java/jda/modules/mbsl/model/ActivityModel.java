package jda.modules.mbsl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jda.modules.common.CommonConstants;
import jda.modules.common.Toolkit;
import jda.modules.common.exceptions.ConstraintViolationException;
import jda.modules.common.exceptions.NotFoundException;
import jda.modules.common.exceptions.NotPossibleException;
import jda.modules.common.types.Null;
import jda.modules.dodm.dsm.DSMBasic;
import jda.modules.mbsl.exceptions.DomainMessage;
import jda.modules.mbsl.model.appmodules.ModuleAct;
import jda.modules.mbsl.model.appmodules.meta.MAct;
import jda.modules.mbsl.model.filter.NullFilter;
import jda.modules.mbsl.model.graph.ActivityGraph;
import jda.modules.mbsl.model.graph.Edge;
import jda.modules.mbsl.model.graph.JoinNode;
import jda.modules.mbsl.model.graph.Node;
import jda.modules.mbsl.model.graph.NodeFactory;
import jda.modules.mbsl.model.graph.NodeType;
import jda.modules.mbsl.model.graph.meta.AGraph;
import jda.modules.mbsl.model.graph.meta.ANode;
import jda.mosa.module.ModuleService;

/**
 * @overview 
 *  Represents an activity model.
 *  
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 * - 5.4: created<br>
 * - 5.6: updated to use multi-map for node and edge maps
 */
public class ActivityModel {
  
  private static final boolean debug = Toolkit.getDebug(ActivityGraph.class);
  
  /**name of the activity model*/
  private String name;
  /**activity domain class*/
  private Class activityCls;
  /**referenced domain classes*/
  private List<Class> refClses;

  /**initial classes
   **/
// v5.6: private List<Class> initClses;
  
  /* v5.6 */
  private List<String> initNodes;
  
  /**the domain schema to which all the domain classes contained in this are registered*/
  private DSMBasic dsm;

  ///// helper data structures 
  /** keys = {@link #refClses} /\ values = {a | exists k in keys. a = assign(ANode,k)} */
  private Map<Object,ANode> graphNodeCfgMap;

  /** keys = {@link #refClses} /\ values = {o | exists k in keys. o = assign(OutCls,k)} */
//  private Map<Object,String[]> graphOutClsCfgMap;
  
  private ActivityGraph graph;

  /**
   * @effects 
   *  initialise this with <tt>name</tt> and <tt>dsm</tt>
   */
  public ActivityModel(String name, DSMBasic dsm) {
    this.name = name;
    this.dsm = dsm;
    refClses = new ArrayList<>();
    //initClses = new ArrayList<>();
    initNodes = new ArrayList<>();
  }


  /**
   * @modifies {@link #activityCls}, {@link #graphNodeCfgMap}, {@link #graphOutClsCfgMap}
   * @effects 
   *  set the activity class to <tt>actCls</tt>, 
   *  updates {@link #graphNodeCfgMap}, {@link #graphOutClsCfgMap} with activity graph configuration from <tt>actCls</tt>
   */
  public void setActivityCls(Class actCls) throws NotFoundException, ConstraintViolationException {
    this.activityCls = actCls;
    
    AGraph[] agraphs = (AGraph[]) actCls.getAnnotationsByType(AGraph.class);

    if (agraphs == null || agraphs.length == 0) {
      // error: no graph config
      throw new NotFoundException(DomainMessage.ERR_GRAPH_CONFIGURATION_NOT_FOUND_WHEN_REQUIRED, 
          new Object[] {actCls});
    }

    AGraph agraph = agraphs[0];
    ANode[] graphCfgs = agraph.nodes();

    if (graphCfgs.length == 0) {
      // error: no graph config
      throw new NotFoundException(DomainMessage.ERR_GRAPH_CONFIGURATION_NOT_FOUND_WHEN_REQUIRED, 
          new Object[] {actCls});
    }
    
    String label; String[] outNodes;
    for (ANode ncfg : graphCfgs) {
      
      Class c = ncfg.refCls();
      
      label = ncfg.label();
      
      if (ncfg.init()) {
        //initClses.add(c);
        initNodes.add(label);
      }

      if (graphNodeCfgMap == null) graphNodeCfgMap = new LinkedHashMap<>();
      graphNodeCfgMap.put(label, ncfg);
      
//      outNodes = ncfg.outNodes();
//      
//      if (graphOutClsCfgMap == null) graphOutClsCfgMap = new LinkedHashMap<>();
//      
//      if (outNodes.length > 0)
//        graphOutClsCfgMap.put(label, outNodes);
    }
    
    // if initClses is empty: error
    if (initNodes.isEmpty())
      throw new ConstraintViolationException(DomainMessage.ERR_GRAPH_HAS_NO_INITIAL_NODES, new Object[] {actCls});
  }
  
  /**
   * @effects 
   *  return the activity class
   */
  public Class getActivityCls() {
    return activityCls;    
  }
  
//  /**
//   * @modifies {@link #refClses}, {@link #graphCfgMap}
//   * @effects 
//   *  add <tt>c</tt> to this as a referenced domain class <br>
//   *  if exists {@link ANode} assignment <tt>a</tt> of <tt>c</tt>
//   *     add <tt>(c,a))</tt> to {@link #graphNodeCfgMap}
//   *  <br>
//   *  if exists {@link OutCls} assignment <tt>o</tt> of <tt>c</tt>
//   *     add <tt>(c,o))</tt> to {@link #graphOutClsCfgMap}  
//   */
//  public void addRefCls(Class c) {
//    refClses.add(c);        
//    
//    ANode a = (ANode)c.getAnnotation(ANode.class);
//    if (a != null) {
//      if (graphNodeCfgMap == null) graphNodeCfgMap = new HashMap<>();
//      graphNodeCfgMap.put(c, a);
//    }
//    
//    OutCls o = (OutCls) c.getAnnotation(ANode.class);
//    if (o != null) {
//      if (graphOutClsCfgMap == null) graphOutClsCfgMap = new HashMap<>();
//      graphOutClsCfgMap.put(c, o);
//    }
//  }
  
  /**
   * @effects 
   *  return all the referenced domain classes
   */
  public List<Class> getRefClses() {
    return refClses;
  }
  
//  /**
//   * @effects 
//   *  set this.{@link #initClses} = <tt>initClses</tt> 
//   */
//  public void setInitClses(Class[] initClses) {
//    this.initClses = initClses;
//  }
  
  /**
   * @effects 
   *  return the initial classes (in the activity flow order)
   *  @deprecated as of v5.6, use {@link #getInitNodes()} instead
   */
//  public List<Class> getInitClses() {
//    return initClses;
//  }
//  
  /**
   * @effects 
   *  return the initial nodes (in the activity flow order)
   */
  public List<String> getInitNodes() {
    return initNodes;
  }
  
  /**
   * @effects 
   *  return the sequence of the domain classes C1,...,Cn that are at the receiving ends of 
   *  <tt>n</tt> activity edges from class <tt>source</tt>,
   *  or return <tt>null</tt> if no such class exists
   *  
   */  
  public List<Class> getNSeqClses(Class source) {
    // TODO
    return null;
  }

  /**
   * @effects 
   *  return all the referenced domain classes, whose corresponding nodes have nodeTypes ≠ t
   */    
  public List<Class> getRefClsesExcl(NodeType t) {
    // TODO
    return null;
  }
    
  /**
   * @effects 
   *  return the control class Ck that is at the receiving end of an activity edge from class <tt>source</tt>,
   *  or return <tt>null</tt> if no such class exists
   */ 
  public Class getCkCls(Class source) {
    //TODO
    return null;
  }

  /**
   * @effects 
   *  return the sequence of classes that are the out classes of a given class <tt>c</tt>
   */ 
  public List<Class> getOutClses(Class c) {
    //TODO
    return null;
  }

  /**
   * @effects 
   *  return the first referenced domain class, whose corresponding node has nodeType = t
   */ 
  public Class getRefCls(NodeType t) {
    //TODO
    return null;
  }
  
  /**
   * @effects 
   *  return the last referenced domain class (in the activity flow order)
   */ 
  public Class getLastRefCls() {
    //TODO
    return null;
  }
  
  /**
   * @effects
   *  if {@link #graph} is null
   *    generate {@link #graph}, using actMService to initialise the corresponding modules of the nodes
   *    
   *  return {@link #graph}
   */
  public ActivityGraph getGraph(ModuleService actMService) throws ConstraintViolationException, NotFoundException {
    if (graph == null) {
      // generate graph (once)
      genGraph(actMService);
      
      // debug
      if (debug == true)
        System.out.println(graph);

    }
    
    return graph;
  }

  /**
   * @modifies this.{@link #graph}, this.{@link #graphNodeCfgMap}
   * @effects 
   *  create {@link #graph} as a new {@link ActivityGraph}, from the graph configurations defined in {@link #activityCls}.
   *  
   *  Elements of {@link #graphNodeCfgMap} are popped out for processing.
   *  
   *  @version 
   *  - 5.6: improved to use ANode.label as node key and to support MAct.zone attribute
   */
  private void genGraph(ModuleService actMService) throws ConstraintViolationException, NotFoundException {
    graph = new ActivityGraph();
    
    // start from the initial node
    String initNLabel = initNodes.get(0);
    ANode aNode = graphNodeCfgMap.get(initNLabel);
    
    Map<String, Node> nodeMap = new HashMap<>();
        
    ModuleService refModuleService = actMService.getModule().getChildDataService(actMService, aNode.refCls());
    Node n = genSubgraph(aNode, actMService, refModuleService, nodeMap);
    
    /* v5.6
    Class c, s; ANode a;
    String l; NodeType t; MAct[] P; Node n; String[] attribNames;
    Map<Object,Node> nodeMap = new LinkedHashMap<>();
    List<ModuleAct> optSeq = null;
    
    // create nodes without edges
    for (Entry<Object,ANode> e : graphNodeCfgMap.entrySet()) {
      Object label = e.getKey();
      a = e.getValue();
      c = a.refCls();
      if (!a.label().equals(CommonConstants.NullString))
        l = a.label();//5.6: "M"+c.getSimpleName();
      else 
        l = "M"+c.getSimpleName();
        
      t = a.nodeType();
      P = a.actSeq();
      s = a.serviceCls();
      if (s == Null.class) s = null;
      
      // create node based on t
      n = NodeFactory.createNode(t, l, c, s);
      
      // create ModuleOpt sequence of n
      if (P.length > 0) {
        if (optSeq == null) optSeq = new ArrayList<>(); else optSeq.clear();
        
        for (MAct p : P) {
          optSeq.add(new ModuleAct(p.actName(), 
              p.endStates().length == 0 ? null : p.endStates(),
              p.attribNames().length == 0 ? null : p.attribNames(),    
              n));
        }
        
        n.setActSeq(optSeq);
      }
      
      // add n to graph
      graph.addNode(n);
      nodeMap.put(l, n);
      
      // if c is initial class then n is the initial node
      if (initClses.contains(c)) {
        graph.addInitNode(n);
      }
    }
    
    // create out-edges of each node, and edges of g
    // v5.6: use actMService to find the exact referenced module of each node
    // trace through the path of each node from an init node to ensure that
    // the referenced module is correct

    String[] outNodes; // o
    Node no; Edge edge;
    for (Entry<Object,Node> e : nodeMap.entrySet()) {
      Object label = e.getKey();
      n = e.getValue();
      outNodes = graphOutClsCfgMap.get(label);
      
      if (outNodes != null) {
        for (String outNode : outNodes) {
          no = nodeMap.get(outNode);
          edge = new Edge(n, no);
          n.addOutEdge(edge);
          
          // if no is a join node the update its pre
          if (no instanceof JoinNode) {
            ((JoinNode) no).addPreNode(n);
          }
          
          graph.addEdge(edge);
        }
      }
    }
    */
  }

  /**
   * @effects 
   *    Depth-first traverse the graph from aNode to create every node in the path with 
   *    the suitable referenced module 
   *    
   * @version 5.6
   */
  private Node genSubgraph(final ANode aNode, final ModuleService actMService, 
      final ModuleService refModuleService, final Map<String, Node> nodeMap) throws NotFoundException {
    
    
    Node n = createNode(aNode);
    
    // create action sequence
    createModuleActSeq(n, aNode.actSeq());
    
    n.setRefModuleService(refModuleService);
    
    // add n to graph
    graph.addNode(n);
    
    if (aNode.init()) {
      graph.addInitNode(n);
    } 
    
    if (debug)
      System.out.println(String.format("Node(%s): refModuleService = %s%n", aNode.label(), refModuleService));
    
    // put n in the stack
    nodeMap.put(aNode.label(), n);
    
    // now traverse depth-first search from this node to create other nodes and the edges
    String[] outNodes = aNode.outNodes();
    if (outNodes.length > 0) {
      for (String outNodeLabel: outNodes) {
        if (debug)
          System.out.println("  -> Out node: " + outNodeLabel);
        
        Node nout = nodeMap.get(outNodeLabel);

        if (nout == null) {
          // create this out node
          ANode outNode = graphNodeCfgMap.get(outNodeLabel);
          String zone = outNode.zone();
          ModuleService childModuleService = null;
          
          if (!zone.equals(CommonConstants.NullString)) {
            ModuleService currModuleService;
            if (zone.equals("top")) {
              currModuleService = actMService;
            } else {
              Node zoneNode = nodeMap.get(zone);
              if (zoneNode == null) {
                throw new NotFoundException(NotFoundException.Code.NODE_NOT_FOUND, new Object[] {zone});
              }
              
              currModuleService = zoneNode.getRefModuleService();
            }
            
            if (currModuleService != null) {
              jda.mosa.module.Module currModule = currModuleService.getModule();
              
              childModuleService = currModule.
                 getChildDataService(currModuleService, outNode.refCls());
            }
          }
          
          nout = genSubgraph(outNode, actMService, childModuleService, nodeMap);
        }
        
        
        // create edge (n, nout)
        Edge edge = new Edge(n, nout);
        // if no is a join node the update its pre
        if (nout instanceof JoinNode) {
          ((JoinNode) nout).addPreNode(n);
        }
        
        n.addOutEdge(edge);
        graph.addEdge(edge);
      }
    }
    
    return n;
  }


  /**
   * @effects 
   *  creates and return a {@link Node} show configuration is specified in <code>aNode</code>
   *  
   * @version 5.6
   */
  private Node createNode(ANode aNode) {
    Class c = aNode.refCls();
    
    String l;
    if (!aNode.label().equals(CommonConstants.NullString))
      l = aNode.label();//5.6: "M"+c.getSimpleName();
    else 
      l = "M"+c.getSimpleName();
      
    NodeType t = aNode.nodeType();
    Class s = aNode.serviceCls();
    if (s == Null.class) s = null;
    
    // create node based on t
    Node n = NodeFactory.createNode(t, l, c, s);
    
    return n;
  }


  /**
   * @modifies n 
   * 
   * @effects 
   *  creates and sets into n {@link ModuleAct} objects that correspond to <code>actSeq</code>
   * 
   * @version 5.6
   * 
   */
  private void createModuleActSeq(Node n, MAct[] actSeq) {
    if (actSeq.length > 0) {
      List<ModuleAct> optSeq = new ArrayList<>();

      for (MAct p : actSeq) {
        optSeq.add(new ModuleAct(p.actName(), 
            p.endStates().length == 0 ? null : p.endStates(),
            p.attribNames().length == 0 ? null : p.attribNames(),
            p.filterType() == NullFilter.class ? null: p.filterType(), 
            n));
      }
      
      n.setActSeq(optSeq);
    }
  }


  /**
   * @effects 
   *  executes this {@link #graph} using <tt>actMService</tt> as module service of the activity module of {@link #activityCls}.  
   *  
   *  <p>throws NotPossibleException if failed  
   * @version 
   * - 5.6: improved getGraph() to use actMService to initialise its nodes
   */
  public void exec(ModuleService actMService, Object...args) throws NotPossibleException, NotFoundException {
    
    // v5.6: getGraph(); // make sure that activity graph is generated
    getGraph(actMService);
    
    graph.exec(actMService, args);
  }
  
  /**
   * @effects 
   *  if this is empty (i.e. containing no referenced domain classes)
   *    return true
   *  else
   *    return false 
   */
  public boolean isEmpty() {
    return refClses.isEmpty();
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
    return "ActivityModel (" + name + ", " + activityCls + ")";
  }


  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((activityCls == null) ? 0 : activityCls.getName().hashCode());
    return result;
  }


  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
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
    ActivityModel other = (ActivityModel) obj;
    if (activityCls == null) {
      if (other.activityCls != null)
        return false;
    } else if (!activityCls.equals(other.activityCls))
      return false;
    return true;
  }
}
