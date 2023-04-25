package jda.modules.mbsl.model.graph;

/**
 * @overview 
 *  Represents different types of activity nodes
 *  
 * @author Duc Minh Le (ducmle)
 */
public enum NodeType {
  Action,
  Decision,
  Fork,
  Join,
  Merge,
  // v5.6
  /** coordinates between other nodes, passing along their data. It does not pass its own data (if any) along*/
  Coordinator
  ;

  /**
   * @effects 
   *  if this is a control node type
   *    return true
   *  else
   *    return false
   * @version 5.6
   */
  public boolean isControl() {
    return this.equals(Decision) || 
        this.equals(Fork) ||
        this.equals(Join) ||
        this.equals(Merge);
  }
  
  /**
   * @effects 
   *  if this is a decision node
   *    return true
   *  else
   *    return false
   * @version 5.6
   */
  public boolean isDecision() {
    return this.equals(Decision);
  }
}
