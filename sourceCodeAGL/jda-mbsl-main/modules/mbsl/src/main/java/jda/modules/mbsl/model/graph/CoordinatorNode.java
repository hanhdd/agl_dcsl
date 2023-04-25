package jda.modules.mbsl.model.graph;

import jda.modules.common.exceptions.NotPossibleException;
import jda.mosa.module.ModuleService;

/**
 * @overview 
 *  Coordinator Node is a Node that does not pass its data out to the outgoing edges. Instead, it passes the original input data out.
 *  
 *  <br>Coordinator node is used to represent a task group. The coordinator's UI serves as the container of those of the member tasks, so that user can have a whole picture of the group. The member tasks themselves interact with each other to perform the group's logic.
 *  
 *  <p>Example: Fulfill Order is a coordinator node for the following two tasks: Order Update Order Delivery. "Order Update" basic updates the Order status to "Fulfilled" after the order has actually been fulfilled by the warehouse. Once this has been done, "Order Delivery" is performed to deliver the ordered products to the customer and to collect payment.
 *  
 *   <p>In the above use case, Fulfill Order simply coordinates the other two tasks, ensuring that Order Update is performed first then Delivery. It does not contribute any data to this flow. However, it enables user to observe and perform the task flow on the UI.
 *  
 * @author Duc Minh Le (ducmle)
 *
 * @version 5.6
 */
public class CoordinatorNode extends Node {

  /**
   * @effects 
   *
   */
  public CoordinatorNode(String label, Class refCls, Class serviceCls) {
    super(label, refCls, serviceCls);
    // TODO Auto-generated constructor stub
  }

  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  protected void execOffer(Node src, ModuleService actMService, Object[] args,
      Object... results) throws NotPossibleException {
      // coordinator node does not pass its data out, it passes the original data out instead
      getOut().get(0).exec(actMService, args);
  }

}
