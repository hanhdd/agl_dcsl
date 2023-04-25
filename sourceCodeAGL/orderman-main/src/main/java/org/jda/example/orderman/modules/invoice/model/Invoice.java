package org.jda.example.orderman.modules.invoice.model;

import java.util.Objects;

import org.jda.example.orderman.modules.customer.model.Customer;
import org.jda.example.orderman.modules.delivery.model.CollectPayment;
import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.util.model.StatusCode;

import jda.modules.common.exceptions.ConstraintViolationException;
import jda.modules.common.types.Tuple;
import jda.modules.dcsl.syntax.DAttr;
import jda.modules.dcsl.syntax.DAttr.Type;
import jda.modules.dcsl.syntax.DOpt;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
public class Invoice {
  @DAttr(name="id",type=Type.Integer,id=true,auto=true,mutable=false,optional=false,min=1)
  private int id;
  
  private static int idCounter = 0;
  
  @DAttr(name="order", type=Type.Domain, mutable=false)
  private CustOrder order;

  // derived from order
  @DAttr(name="customer", type=Type.Domain, mutable=false, auto=true, serialisable=false)
  private Customer customer;
  
  @DAttr(name = "status", type = Type.Domain
      // not supported for Domain-typed attribute: auto=true
      , mutable=false
      )
  private StatusCode status;

  //  virtual link
  @DAttr(name="collectPayment",type=Type.Domain,serialisable=false,virtual=true)
  private CollectPayment collectPayment;
  
  
  
  /**
   * @effects 
   *
   * @version 
   */
  public Invoice(Integer id, CustOrder order, StatusCode status) {
    this.id = nextID(id);
    this.order = order;
    this.status = status;
  }
  
  public Invoice(CustOrder order, StatusCode status) {
    this(null, order, status);
  }

  /**
   * @effects return status
   */
  public StatusCode getStatus() {
    return status;
  }

  /**
   * @effects set status = status
   */
  public boolean setStatus(StatusCode status) {
    this.status = status;
    
    // update order as well
    order.invoiced();
    
    return true;
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
  public CustOrder getOrder() {
    return order;
  }
  
  /**
   * @effects set this.order = order
   */
  public boolean setOrder(CustOrder order) {
    this.order = order;
    this.customer = order.getCustomer();
    return true;
  }
  
  
  /**
   * @effects return customer
   */
  public Customer getCustomer() {
    return customer;
  }

  @DOpt(type = DOpt.Type.AutoAttributeValueSynchroniser)
  public static void synchWithSource(DAttr attrib, Tuple derivingValue, Object minVal, Object maxVal) throws ConstraintViolationException {
      String attribName = attrib.name();
      if (attribName.equals("id")) {
          int maxIdVal = (Integer) maxVal;
          if (maxIdVal > idCounter)
              idCounter = maxIdVal;
      }
  }
  
  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public String toString() {
    return "Invoice (" + id + ", " + status + ")";
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
    Invoice other = (Invoice) obj;
    return id == other.id;
  }
  
}
