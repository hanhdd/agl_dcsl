package jda.modules.mbsl.model.util;

import jda.modules.common.exceptions.NotPossibleException;
import jda.modules.mbsl.model.graph.Node;

/**
 * @overview 
 *  Represents domain classes that serve as the merge class. 
 *  
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 * - 5.6: created<br>
 */
public interface Merge {

  /**
   * @requirse
   *  <tt>srcNode</tt> is the source node of the in-edge of <tt>mergeNode</tt> 
   *  
   * @effects 
   *  Perform the merge logic on the input <tt>args</tt> 
   *  to return an {@link Object}[] to be used as the token to be offered to the out-going edges.
   *  
   *  <p>If no suitable merge logic is found suitable for <tt>args</tt> then return <tt>args</tt> itself as the result.
   *  
   */
  Object[] evaluate(Node mergeNode, Node srcNode, Object[] args) throws NotPossibleException;

}
