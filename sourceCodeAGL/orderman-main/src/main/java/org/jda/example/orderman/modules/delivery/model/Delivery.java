package org.jda.example.orderman.modules.delivery.model;

import java.util.Collection;
import java.util.Objects;

import org.jda.example.orderman.modules.fillorder.model.FillOrder;
import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.modules.ship.model.ShipOrder;

import jda.modules.dcsl.syntax.DAssoc;
import jda.modules.dcsl.syntax.DAssoc.AssocEndType;
import jda.modules.dcsl.syntax.DAssoc.AssocType;
import jda.modules.dcsl.syntax.DAssoc.Associate;
import jda.modules.dcsl.syntax.DAttr;
import jda.modules.dcsl.syntax.DAttr.Type;
import jda.modules.dcsl.syntax.DCSLConstants;
import jda.modules.dcsl.syntax.DClass;
import jda.modules.dcsl.syntax.Select;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
@DClass(serialisable=false)
public class Delivery {
      @DAttr(name="id",type=Type.Integer,id=true,auto=true,mutable=false,optional=false,min=1)
  private int id;
  
  private static int idCounter = 0;
  
  @DAttr(name="receivedOrder", type=Type.Domain, mutable=false)
  private CustOrder receivedOrder;
  
  // order 
  @DAttr(name="collectPayments", type=Type.Collection,filter=@Select(clazz=CollectPayment.class)
  ,serialisable=false)
  @DAssoc(ascName="collect-payment",role="mgmt",
    ascType=AssocType.One2Many,endType=AssocEndType.One,
    associate=@Associate(
        type=CollectPayment.class,cardMin=0,cardMax=DCSLConstants.CARD_MORE,
        updateLink=false))
  private Collection<CollectPayment> collectPayments;

  // ship order 
  @DAttr(name="shipOrders", type=Type.Collection,filter=@Select(clazz=ShipOrder.class),serialisable=false)
  @DAssoc(ascName="delivery",role="mgmt",
    ascType=AssocType.One2Many,endType=AssocEndType.One,
    associate=@Associate(
        type=ShipOrder.class,cardMin=0,cardMax=DCSLConstants.CARD_MORE,
        updateLink=false
     ))
  private Collection<ShipOrder> shipOrders;

  // end order
  @DAttr(name="closeOrders", type=Type.Collection,filter=@Select(clazz=CustOrder.class),serialisable=false)
  @DAssoc(ascName="end-order",role="mgmt",
    ascType=AssocType.One2Many,endType=AssocEndType.One,
    associate=@Associate(
        type=CustOrder.class,cardMin=0,cardMax=DCSLConstants.CARD_MORE,
        updateLink=false
    ))
  private Collection<CustOrder> closeOrders;
  
  // virtual link to FillOrder
  @DAttr(name="fillOrder",type=Type.Domain,serialisable=false,virtual=true)
  private FillOrder fillOrder;

  
  public Delivery(Integer id, CustOrder receivedOrder) {
    this.id = nextID(id);
    this.receivedOrder = receivedOrder;
  }
  
  public Delivery(CustOrder receivedOrder) {
    this(null,receivedOrder);
  }
  
  /**
   * @effects return id
   */
  public int getId() {
    return id;
  }

  private static int nextID(Integer currID) {
    if (currID == null) { // generate one
      idCounter++;
      return idCounter;
    } else { // update
      int num;
      num = currID.intValue();
      
      if (num > idCounter) {
        idCounter=num;
      }   
      return currID;
    }
  }  
  
  /**
   * @effects return receivedOrder
   */
  public CustOrder getReceivedOrder() {
    return receivedOrder;
  }
  
  /**
   * @effects set receivedOrder = receivedOrder
   */
  public void setReceivedOrder(CustOrder receivedOrder) {
    this.receivedOrder = receivedOrder;
  }
  

  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public String toString() {
    return "Delivery (" + id + ")";
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
    Delivery other = (Delivery) obj;
    return id == other.id;
  }
  
  
}
