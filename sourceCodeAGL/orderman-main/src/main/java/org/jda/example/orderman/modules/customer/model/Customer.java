package org.jda.example.orderman.modules.customer.model;

import java.util.ArrayList;
import java.util.Collection;

import org.jda.example.orderman.modules.order.model.CustOrder;

import jda.modules.common.exceptions.ConstraintViolationException;
import jda.modules.common.types.Tuple;
import jda.modules.dcsl.syntax.AttrRef;
import jda.modules.dcsl.syntax.DAssoc;
import jda.modules.dcsl.syntax.DAssoc.AssocEndType;
import jda.modules.dcsl.syntax.DAssoc.AssocType;
import jda.modules.dcsl.syntax.DAssoc.Associate;
import jda.modules.dcsl.syntax.DAttr;
import jda.modules.dcsl.syntax.DAttr.Type;
import jda.modules.dcsl.syntax.DOpt;
import jda.modules.dcsl.syntax.Select;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
public class Customer {
  public static final String A_id = "id";
  public static final String A_name = "name";
  //public static final String A_address = "address";
  public static final String A_modules = "modules";
  public static final String A_orders = "orders";

  
  public static final String A_helpRequested = "helpRequested";

  /*** STATE SPACE **/
  @DAttr(name="id",type=Type.Integer,id=true,auto=true,mutable=false,optional=false,min=1)
  private int id;
  
  @DAttr(name=A_name,type=Type.String,length=30,optional=false)
  private String name;
  
  @DAttr(name=A_orders,type=Type.Collection,optional=false,serialisable=false,
      filter=@Select(clazz=CustOrder.class))
  @DAssoc(ascName="std-has-orders",role="std",
    ascType=AssocType.One2Many,endType=AssocEndType.One,
    associate=@Associate(type=CustOrder.class,cardMin=0,cardMax=30))
  private Collection<CustOrder> orders;  
  
  // derived
  private int orderCount;
  ///// End Association to CourseModule

  /*** BEHAVIOUR SPACE **/
  // static variable to keep track of student id
  private static int idCounter = 0;

  // constructor methods
  @DOpt(type=DOpt.Type.RequiredConstructor)
  @DOpt(type=DOpt.Type.ObjectFormConstructor)
  public Customer(String name) {
    this(null, name);
  }

  @DOpt(type=DOpt.Type.DataSourceConstructor)
  public Customer(Integer id, String name) {
    // generate an id
    this.id = nextID(id);
    this.name = name;
    
    this.orders = new ArrayList<>();
  }
  
  @DOpt(type=DOpt.Type.AutoAttributeValueGen)
  private static int nextID(Integer currID) {
  // automatically generate the next student id
    if (currID == null) { // generate one
      idCounter++;
      return idCounter;
    } else { // update
      int num = currID.intValue();
      
      if (num > idCounter) {
        idCounter=num;
      }   
      return currID;
    }
  }
  
  // setter methods
  @DOpt(type=DOpt.Type.Setter)@AttrRef(value="name")
  public void setName(String name) {
    this.name = name;
  }

  /**Association {@link #orders} */
  @DOpt(type=DOpt.Type.Getter)@AttrRef(value="orders")
  public Collection<CustOrder> getOrders() {
    return orders;
  }

  @DOpt(type=DOpt.Type.Setter)@AttrRef(value="orders")
  public void setOrders(Collection<CustOrder> en) {
    this.orders = en;
    orderCount = en.size();
  }

  
  @DOpt(type=DOpt.Type.LinkAdderNew)  @AttrRef("orders")
  public boolean addNewOrder(CustOrder e) {
    orders.add(e);
    
    orderCount++;
    
    // no other attributes changed (average mark is not serialisable!!!)
    return false; 
  }
  
  @DOpt(type=DOpt.Type.LinkAdderNew)  @AttrRef("orders")
  public boolean addNewOrder(Collection<CustOrder> orders) {
    orders.addAll(orders);
    
    orderCount+=orders.size();
    
    // no other attributes changed (average mark is not serialisable!!!)
    return false; 
  }
  
  @DOpt(type=DOpt.Type.LinkAdder)  @AttrRef("orders")
  public boolean addOrder(CustOrder e) {
    if (!orders.contains(e)) {
      orders.add(e);
    
      orderCount++;
    }
    
    // no other attributes changed
    return false; 
  }
  
  @DOpt(type=DOpt.Type.LinkAdder)  @AttrRef("orders")
  public boolean addOrder(Collection<CustOrder> orders) {
    for (CustOrder e : orders) {
      if (!orders.contains(e)) {
        orders.add(e);
        
        orderCount++;
      }
    }

    // no other attributes changed
    return false; 
  }

  @DOpt(type=DOpt.Type.LinkRemover)  @AttrRef("orders")
  public boolean removeOrder(CustOrder e) throws ConstraintViolationException {
    boolean removed = orders.remove(e);
    
    if (removed) {
      orderCount--;
    }
    
    // no other attributes changed
    return false; 
  }

  @DOpt(type=DOpt.Type.LinkCountGetter)  @AttrRef("orders")
  public Integer getOrdersCount() {
    return orderCount;
  }

  @DOpt(type=DOpt.Type.LinkCountSetter)  @AttrRef("orders")
  public void setOrdersCount(int count) {
    orderCount = count;
  }
  
  /**END Association {@link #orders} */

  
  // getter methods
  @DOpt(type=DOpt.Type.Getter)@AttrRef(value="id")
  public int getId() {
    return id;
  }

  @DOpt(type=DOpt.Type.Getter)@AttrRef(value="name")
  public String getName() {
    return name;
  }

  /**
   * @effects return {@link #toString(boolean)}<tt> -> (true)</tt>
   */
  @Override
  public String toString() {
    return toString(true);
  }

  /**
   * @effects returns <code>Customer(id,name,dob,address,email)</code>.
   */
  public String toString(boolean full) {
    if (full)
      return "Customer(" + id + "," + name + ")";
    else
      return "Customer(" + id + ")";
  }

  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Customer other = (Customer) obj;
    if (id != other.id)
      return false;
    return true;
  }

  /**
   * @requires 
   *  minVal != null /\ maxVal != null
   * @effects 
   *  update the auto-generated value of attribute <tt>attrib</tt>, specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
   */
  @DOpt(type=DOpt.Type.AutoAttributeValueSynchroniser)
  public static void updateAutoGeneratedValue(
      DAttr attrib,
      Tuple derivingValue, 
      Object minVal, 
      Object maxVal) throws ConstraintViolationException {
    if (minVal != null && maxVal != null) {
      // check the right attribute
      if (attrib.name().equals("id")) {
        int maxIdVal = (Integer) maxVal;
        if (maxIdVal > idCounter)  
          idCounter = maxIdVal;
      } 
      // TODO add support for other attributes here 
    }    
  }
}
