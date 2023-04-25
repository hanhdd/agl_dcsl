package org.jda.example.orderman.modules.order.model;

import org.jda.example.orderman.modules.product.model.Product;

import jda.modules.common.cache.StateHistory;
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

//@DomainClass(dependsOn={Order.class,Product.class})
public class OrderLine {

//  /** publicly visible (but not accessible) attribute names */
//  public static enum Attribute {
//    lineTotal
//  }
  
  private static final String AttributeName_Product = "product";
  private static final String AttributeName_UnitPrice = "unitPrice";
  private static final String AttributeName_Quantity = "quantity";
  private static final String AttributeName_LineTotal = "lineTotal";
  
  @DAttr(name="id",type=Type.Integer,auto=true,id=true,mutable=false,optional=false)
  private int id;
  
  @DAttr(name="order",type=Type.Domain,optional=false)
  @DAssoc(ascName="order-has-lines",role="line",
    ascType=AssocType.One2Many,endType=AssocEndType.Many,
    associate=@Associate(type=CustOrder.class,cardMin=1,cardMax=1), 
    dependsOn=true)
  private CustOrder order;
  
  @DAttr(name=AttributeName_Product,type=Type.Domain,optional=false)
  @DAssoc(ascName="product-has-lines",role="line",
    ascType=AssocType.One2Many,endType=AssocEndType.Many,
    associate=@Associate(type=Product.class,cardMin=1,cardMax=1), 
    dependsOn=true)
  private Product product;

  // derived
  @DAttr(name=AttributeName_UnitPrice,type=Type.Double,auto=true,mutable=false,serialisable=false,
      derivedFrom={AttributeName_Product})
  private double unitPrice;

  @DAttr(name=AttributeName_Quantity,type=Type.Integer,optional=false)
  private int quantity;
  
  // derived 
  @DAttr(name=AttributeName_LineTotal,type=Type.Double,mutable=false,
      derivedFrom={AttributeName_Product, AttributeName_Quantity})
  private double lineTotal;
  
  /* value cache */
  private StateHistory<String,Object> stateHist;
  
  private static int idCounter;
  
  public OrderLine(Integer id, CustOrder order, Product product, Integer quantity, Double lineTotal) {
    this.id = nextID(id);
    this.order = order;
    this.product = product;
    this.quantity=quantity;
    
    if (lineTotal != null)
      this.lineTotal = lineTotal;
    else
      this.lineTotal = getUnitPrice()*quantity;
    
    stateHist = new StateHistory<String,Object>();
  }

  public OrderLine(CustOrder order, Product product, Integer quantity, Double lineTotal) {
    this(null,order,product,quantity,lineTotal);
  }


  public CustOrder getOrder() {
    return order;
  }

//  /** derived attribute (used for viewing) */
//  @DomainConstraint(name="orderID",type=Type.Integer,auto=true,serialisable=false,mutable=false)
//  private int orderID;
  
  private int getOrderID() {
    return order.getOrderID();
  }

  public void setOrder(CustOrder order) {
    this.order = order;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    setProduct(product, false);
  }
  
  public void setProduct(Product product, boolean updateLineTotal) {
    this.product = product;
    
    /*v2.6.4b: 
    // cache line total 
    valueCache.put(AttributeName_LineTotal, lineTotal);
    
    // update line total
    lineTotal = getUnitPrice() * quantity;
    */
    if (updateLineTotal)
      updateLineTotal();
  }

  private int getProductID() {
    return product.getProductID();
  }

  public double getUnitPrice() {
    return product.getUnitPrice();
  }
  
  public int getId() {
    return id;
  }

  public int getQuantity() {
    return quantity;
  }

  // v2.6.4b
  public void setQuantity(int quantity) {
    setQuantity(quantity, false);
  }
  
  public void setQuantity(int quantity, boolean updateLineTotal) {
    this.quantity = quantity;
    
    /*v2.6.4b
    // cache line total 
    valueCache.put(AttributeName_LineTotal, lineTotal);
    
    // update line total
    lineTotal = getUnitPrice() * quantity;
    */
    if (updateLineTotal)
      updateLineTotal();
  }
  
  @DOpt(type=DOpt.Type.DerivedAttributeUpdater)
  @AttrRef(value=AttributeName_LineTotal)  
  public void updateLineTotal() {
    // cache line total 
    stateHist.put(AttributeName_LineTotal, lineTotal);
    
    // update line total
    lineTotal = getUnitPrice() * quantity;
  }
  
  /**
   * @effects 
   *  return the product of unit price and quantity 
   */
  public double getLineTotal() {
    return getLineTotal(false);
  }
  
  public double getLineTotal(boolean cached) throws IllegalStateException {
    if (cached) {
      Object val = stateHist.get(AttributeName_LineTotal);
      if (val == null)
        throw new IllegalStateException("OrderLine.getLineTotal: cached value is null");
      return (Double) val;
    } else {
      // clear cache entry (if any)
      //valueCache.remove(Attribute.lineTotal);
      return lineTotal;
    }
  }
  
  private static int nextID(Integer currID) {
    if (currID == null) { // generate one
      idCounter++;
      return idCounter;
    } else { // update
      int num;
      num = currID.intValue();
      
      if (num > idCounter) 
        idCounter=num;
      
      return currID;
    }
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
      if (attrib.name().equals("id")) {
        int maxIdNum = (Integer) maxVal;
        
        if (maxIdNum > idCounter) // extra check
          idCounter = maxIdNum;

      }
    }
  }

  @Override
  public String toString() {
    return "OrderLine (" + id + ", " + getOrderID() + ", " + getProductID()
        + ")";
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
    OrderLine other = (OrderLine) obj;
    if (id != other.id)
      return false;
    return true;
  }
  
}
