package org.jda.example.orderman.modules.product.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.modules.order.model.OrderLine;

import jda.modules.common.cache.StateHistory;
import jda.modules.common.exceptions.ConstraintViolationException;
import jda.modules.common.types.Tuple;
import jda.modules.dcsl.syntax.DAssoc;
import jda.modules.dcsl.syntax.DAssoc.AssocEndType;
import jda.modules.dcsl.syntax.DAssoc.AssocType;
import jda.modules.dcsl.syntax.DAssoc.Associate;
import jda.modules.dcsl.syntax.DAttr;
import jda.modules.dcsl.syntax.DAttr.Type;
import jda.modules.dcsl.syntax.DOpt;
import jda.modules.dcsl.syntax.Select;

public class Product {

  /**
   * @author dmle
   */
  public static enum Attribute {
    /**{@link Product#orderLines}*/
    orderLines,
    totalSales
    ;
  }
  
  /* value cache */
  private StateHistory<Attribute,Object> stateHist;
  
  @DAttr(name="productID",type=Type.Integer,auto=true,id=true,mutable=false,optional=false)
  private int productID;
  private static int idCounter;

  @DAttr(name="productDescr",type=Type.String,length=30,optional=false)
  private String productDescr;

  @DAttr(name="productFinish",type=Type.String,length=20,optional=false)
  private String productFinish;
  
  @DAttr(name="unitPrice",type=Type.Double,optional=false)
  private double unitPrice;

  @DAttr(name="orderLines",type=Type.Collection,
      filter=@Select(clazz=OrderLine.class),//role="product",
      serialisable=false)
  //v2.6.4b: @Update(add="addOrderLine",delete="removeOrderLine",update="updateOrderLine")
  @DAssoc(ascName="product-has-lines",role="product",
    ascType=AssocType.One2Many,endType=AssocEndType.One,
    associate=@Associate(type=OrderLine.class,cardMin=1,
        //cardMax=Association.CARD_MORE
        // use this for testing
        cardMax=4
    ))
  private List<OrderLine> orderLines;
  
  // derived
  private Integer orderLinesCount; 
  
  // derived (total sales)
  @DAttr(name="totalSales",type=Type.Double,
      //auto=true,
      mutable=false
      /*v2.6.4b: make serialisable
      serialisable=false,
      derivedFrom={"orderLines"}
       */
  )
  private Double totalSales;

  // derived
  @DAttr(name="quantityByMonth",type=Type.Integer,auto=true,mutable=false,serialisable=false)
  private Integer quantityByMonth;

  // derived 
  @DAttr(name="totalSalesByMonth",type=Type.Double,auto=true,mutable=false,serialisable=false)
  private Double totalSalesByMonth;

  public Product(Integer productID, String productDescr, String productFinish,
      Double unitPrice, 
      //v2.6.4b: added totalSales
      Double totalSales,
      //TODO: is this OrderLine?
      List<CustOrder> orders) {
    this.productID = nextID(productID);
    
    this.productDescr = productDescr;
    this.productFinish = productFinish;
    this.unitPrice = unitPrice;

    // v2.6.4b:
    this.totalSales = totalSales;

    stateHist = new StateHistory<Attribute,Object>();
  }

  public Product(String productDescr, String productFinish,
      Double unitPrice) {
    this(null, productDescr, productFinish, unitPrice, null, null);
  }

  public String getProductDescr() {
    return productDescr;
  }

  public void setProductDescr(String productDescr) {
    this.productDescr = productDescr;
  }

  public String getProductFinish() {
    return productFinish;
  }

  public void setProductFinish(String productFinish) {
    this.productFinish = productFinish;
  }

  public double getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(double unitPrice) {
    this.unitPrice = unitPrice;
  }

  public List<OrderLine> getOrderLines() {
    return orderLines;
  }

//  public Integer getOrderLinesCount() {
//    return (orderLines != null) ? orderLines.size() : 0;
//  }
  @DOpt(type=DOpt.Type.LinkCountGetter)
  public Integer getOrderLinesCount() {
    return orderLinesCount;
  }

  @DOpt(type=DOpt.Type.LinkCountSetter)
  public void setOrderLinesCount(int count) {
    orderLinesCount = count;
  }
  
  @DOpt(type=DOpt.Type.LinkAdder)
  public boolean addOrderLine(List<OrderLine> lines) {
    boolean changed = false, updated;
    for (OrderLine line : lines) {
      updated = addOrderLine(line);
      if (!changed) changed = updated;
    }
    
    
    return changed;
  }
  
  @DOpt(type=DOpt.Type.LinkAdder)
  public boolean addOrderLine(OrderLine orderLine) {
    if (orderLines == null)
      orderLines = new ArrayList();

    if (!orderLines.contains(orderLine)) { // v2.7.3
      orderLines.add(orderLine);
  
//      if (totalSales == null)
//        totalSales = 0d;
//      
//      if (orderLinesCount == null) orderLinesCount = 0;
//      orderLinesCount++;
//      
//      totalSales += orderLine.getLineTotal();
//  
//      return true;
//    } else {
//      return false;
    }
    return false;
  }

  @DOpt(type=DOpt.Type.LinkAdderNew)
  public boolean addNewOrderLine(OrderLine orderLine) {
    if (orderLines == null)
      orderLines = new ArrayList();

    orderLines.add(orderLine);

    if (totalSales == null)
      totalSales = 0d;
    
    if (orderLinesCount == null) orderLinesCount = 0;
    orderLinesCount++;
    
    totalSales += orderLine.getLineTotal();

    return true;
  }

  @DOpt(type=DOpt.Type.LinkRemover)
  public boolean removeOrderLine(OrderLine orderLine) {
    boolean changed = false;
    if (orderLines != null) {
      orderLines.remove(orderLine);
    }
    
    if (orderLinesCount != null && orderLinesCount > 0) {
      changed = true;
      orderLinesCount--;
    }
    
    if (totalSales != null && totalSales > 0) {
      totalSales -= orderLine.getLineTotal();
      changed = true;
    }
    
    return changed;
  }

  @DOpt(type=DOpt.Type.LinkUpdater)
  public boolean updateOrderLine(OrderLine line) {
    // update totalSales based on the change in line total
    double oldLineTotal = line.getLineTotal(true);
    
    double diff = line.getLineTotal() - oldLineTotal;
    
    // cache old value
    stateHist.put(Attribute.totalSales, totalSales);

    totalSales += diff;
    
    // totalSales is changed
    return true;
  }
  
  // IMPORTANT: this method returns boolean because the derived attribute
  // totalSales is changed by this method
  public boolean setOrderLines(List<OrderLine> orderLines) {
    this.orderLines = orderLines;
    
    orderLinesCount = (orderLines != null) ? orderLines.size() : null;
    
    // compute total sales (because it is not stored)
    if (orderLines != null) {
      totalSales = 0D;
      for (OrderLine ol : orderLines) {
        totalSales += ol.getLineTotal();
      }
      
      // because totalSales is updated
      return true;
    }
    
    return false;
  }

  /**
   * @effects 
   *  if this contains <tt>line</tt>
   *    return true
   *  else 
   *    return false
   */
  public boolean contains(OrderLine line) {
    return (orderLines != null && orderLines.contains(line));
  }
  
  public int getProductID() {
    return productID;
  }

  /**
   * @effects
   *  if totalSales already computed
   *    return totalSales
   *  else
   *    if (orderLines != null) 
   *      compute and return the total sales of this product from 
   *      all of its order lines
   */
  public double getTotalSales() {
    if (totalSales == null) {
      totalSales = 0D;
    }
    return totalSales;
  }

  
  /**
   * @effects 
   *  deinitialise total sales
   * @version 
   *  2.6.4.b: added  
   */
  public void resetTotalSales() {
    if (totalSales != null)
      totalSales = null;
  }

//  /**
//   * @effects 
//   *  invoke {@link #getTotalSales()} and return the result 
//   */
//  public Object deriveTotalSales(List vals) {
//    // we can ignore the arguments because we know it 
//    // contains orderLines
//    return getTotalSales();
//  }
  
  /**
   * @effects
   *  if (orderLines != null) 
   *    compute and return the total sales of this product from 
   *    all of its order lines whose month is equal to the specified month
   *  else
   *    return -1;
   */
  public double totalSalesByMonth(int month) {
    totalSalesByMonth = 0D;
    quantityByMonth = 0;
    
    if (orderLines != null) {
      Date orderDate;
      int orderMonth;
      int lineQty;
      Calendar cal = Calendar.getInstance();
      for (OrderLine line: orderLines) {
        orderDate = line.getOrder().getOrderDate();
        cal.setTime(orderDate);
        orderMonth = cal.get(Calendar.MONTH)+1;
        if (orderMonth == month) {
          lineQty = line.getQuantity();
          totalSalesByMonth += lineQty * unitPrice;
          quantityByMonth += lineQty;
        }
      }
    }
    
    if (totalSalesByMonth <= 0) {
      totalSalesByMonth = -1D;
      quantityByMonth = -1;
    }
    
    return totalSalesByMonth;
  }

  /**
   * @effects
   *  if (orderLines != null) 
   *    compute and return the total sales of this product from 
   *    all of its order lines whose month is equal to the specified month
   *    and whose total sales is withing the specified <tt>[min,max]</tt> range
   *    (min or max or both may be <tt>null</tt>) 
   *  else
   *    return -1;
   */
  public double totalSalesByMonthAndRange(int month, Double min, Double max) {
    //double totalSales = totalSalesByMonth(month);
    
    totalSalesByMonth(month);
    
    boolean inRange = false;
    if (min != null && max != null) {
      inRange = (totalSalesByMonth >= min && totalSalesByMonth <= max);
    } else if (min == null && max != null) {
      inRange = totalSalesByMonth <= max;
    } else if (min != null && max == null) {
      inRange = totalSalesByMonth >= min;
    } else {
      // min = null && max = null
      inRange = true;
    }
    
    if (inRange) {
      return totalSalesByMonth;
    } else {
      return -1;
    }
  }

  // v2.6.4b: added to use by report
  public void resetTotalSalesByMonth() {
    if (totalSalesByMonth != null) {
      totalSalesByMonth = null;
    }
  }

  // v2.6.4b: added to use by report
  public void setTotalSalesByMonth(double salesByMonth) {
    totalSalesByMonth = salesByMonth;
  }
  
  public double getTotalSalesByMonth() throws InternalError {
    // unlike total sales, totalSalesByMonth must have already computed
    // by totalSalesbyMonth() method before invoking this method
    if (totalSalesByMonth == null) {
      /*v2.6.4b: 
      throw new InternalError("Total by month have not been computed");
      */
      totalSalesByMonth = 0D;
    }
    return totalSalesByMonth;
  }
  
  // v2.6.4b: added to use by report
  public void resetQuantityByMonth() {
    if (quantityByMonth != null)
      quantityByMonth = null;
  }

  // v2.6.4b: added to use by report
  public void setQuantityByMonth(int qtyByMonth) {
    quantityByMonth = qtyByMonth;
  }
  
  public int getQuantityByMonth() throws InternalError {
    if (quantityByMonth == null) {
      /*v2.6.4b: 
       *throw new InternalError("Quantity by month have not been computed");
       */
      quantityByMonth = 0;
    }
    
    return quantityByMonth;
  }
  
  
  @Override
  public String toString() {
    return "Product (" + productID + ")";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + productID;
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
    Product other = (Product) obj;
    if (productID != other.productID)
      return false;
    return true;
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
      if (attrib.name().equals("productID")) {
        int maxIdNum = (Integer) maxVal;
        
        if (maxIdNum > idCounter) // extra check
          idCounter = maxIdNum;
      }
    }
  }
}
