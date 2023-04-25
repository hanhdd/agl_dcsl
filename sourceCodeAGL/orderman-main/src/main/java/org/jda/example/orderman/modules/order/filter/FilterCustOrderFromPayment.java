package org.jda.example.orderman.modules.order.filter;

import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.modules.payment.model.Payment;

import jda.modules.mbsl.model.filter.FilterType;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
public class FilterCustOrderFromPayment implements FilterType {

  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public Object[] eval(Object... args) {
    if (args != null) {
      Payment pay = (Payment) args[0];
      CustOrder order = pay.getInvoice().getOrder();
      
      args[0] = order;
      
      return args;
    } else {
      return null;
    }
  }

}
