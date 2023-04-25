package org.jda.example.orderman.modules.payment.model;

import java.util.Collection;
import java.util.Objects;

import org.jda.example.orderman.modules.delivery.model.CollectPayment;
import org.jda.example.orderman.modules.invoice.model.Invoice;
import org.jda.example.orderman.modules.order.model.CustOrder;

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
public class AcceptPayment {
  @DAttr(name = "id", id = true, auto = true, type = Type.Integer, length = 5, 
      optional = false, mutable = false)
  private int id;
  private static int idCounter = 0;
  
  @DAttr(name="invoice", type=Type.Domain, mutable=false)
  private Invoice invoice;
  
  // payment
  @DAttr(name="payments", type=Type.Collection,filter=@Select(clazz=Payment.class),serialisable=false)
  @DAssoc(ascName="payment",role="mgmt",
    ascType=AssocType.One2Many,endType=AssocEndType.One,
    associate=@Associate(
        type=Payment.class,cardMin=0,cardMax=DCSLConstants.CARD_MORE,
        updateLink=false
     ))
  private Collection<Payment> payments;

  // order 
  @DAttr(name="orders", type=Type.Collection,filter=@Select(clazz=CustOrder.class)
  ,serialisable=false)
  @DAssoc(ascName="payment-order",role="mgmt",
    ascType=AssocType.One2Many,endType=AssocEndType.One,
    associate=@Associate(
        type=CustOrder.class,cardMin=0,cardMax=DCSLConstants.CARD_MORE,
        updateLink=false
     ))
  private Collection<CustOrder> orders;
  
  // virtual link
  @DAttr(name="collectPayment",type=Type.Domain,serialisable=false,virtual=true)
  private CollectPayment collectPayment;
  
  
  public AcceptPayment(Integer id, Invoice invoice) {
    this.id = nextID(id);
    this.invoice = invoice;
  }

  public AcceptPayment(Invoice invoice) {
    this(null,invoice);
  }
  
  /**
   * @effects return id
   */
  public int getId() {
    return id;
  }

  
  /**
   * @effects return invoice
   */
  public Invoice getInvoice() {
    return invoice;
  }
  
  /**
   * @effects set this.invoice = invoice
   */
  public void setInvoice(Invoice invoice) {
    this.invoice = invoice;
  }
  
  
  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public String toString() {
    return "AcceptPayment (" + id + ")";
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
    AcceptPayment other = (AcceptPayment) obj;
    return id == other.id;
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
}

