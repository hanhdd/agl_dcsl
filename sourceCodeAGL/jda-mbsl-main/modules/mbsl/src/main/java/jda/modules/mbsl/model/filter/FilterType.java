package jda.modules.mbsl.model.filter;

/**
 * @overview 
 *  
 * @author Duc Minh Le (ducmle)
 *
 * @version 5.6 
 */
public interface FilterType {
  
  /**
   * @effects 
   *  return the filtered input from <tt>args</tt> that are needed to perform a module action. 
   */
  Object[] eval(Object...args);
}
