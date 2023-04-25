package jda.modules.mbsl.model.filter;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
public class NullFilter implements FilterType {

  /**
   * @effects 
   * 
   */
  @Override
  public Object[] eval(Object... args) {
    // does not do anything!
    return args;
  }

}
