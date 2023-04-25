package org.jda.example.orderman.modules.payment.model;

import java.util.Map;

import org.jda.example.orderman.modules.customer.model.Customer;
import org.jda.example.orderman.modules.invoice.model.Invoice;

import jda.modules.common.exceptions.ConstraintViolationException;
import jda.modules.common.types.Tuple;
import jda.modules.dcsl.syntax.AttrRef;
import jda.modules.dcsl.syntax.DAttr;
import jda.modules.dcsl.syntax.DAttr.Type;
import jda.modules.dcsl.syntax.DClass;
import jda.modules.dcsl.syntax.DOpt;

/**
 * @overview 
 *  Represents payment for intuition fee. 
 *  
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
@DClass()
public class Payment {

  public static final String A_student = "student";

  public static final String A_status = "status";

  public static final String A_payDetails = "payDetails";

  public static final String A_description = "description";
  
  public static final String A_enrolmentProc = "enrolmentProc";
  
    /*** STATE SPACE **/
    // attributes
    @DAttr(name = "id", type = Type.Integer, id = true, auto = true, optional = false, mutable = false, min = 1)
    private int id;
    private static int idCounter;

//    @DAttr(name="acceptPayment", type=Type.Domain, mutable=false, auto=true, serialisable=false)
//    private AcceptPayment acceptPayment;

    @DAttr(name = A_payDetails, type = Type.String, length = 255, auto=true, mutable=false)
    private String payDetails;

    @DAttr(name = A_description, type = Type.String, length = 255, auto=true, mutable=false)
    private String description;

    @DAttr(name="invoice", type=Type.Domain, mutable=false)
    private Invoice invoice;

    // derived from invoice
    @DAttr(name="customer", type=Type.Domain, mutable=false, auto=true, serialisable=false)
    private Customer customer;
    
    @DAttr(name = "amount", type = Type.Double)
    private double amount;
    
    @DAttr(name = A_status, type = Type.Domain
        // not supported for Domain-typed attribute: auto=true
        , mutable=false
        )
    private PaymentStatus status;

    /***
     * derived from {@link #status}
     */
    @DAttr(name = "statusStr", type = Type.String, length=20, auto=true, mutable=false, serialisable=false)
    private String statusStr;

    //  virtual link
    @DAttr(name="acceptPayment",type=Type.Domain,serialisable=false,virtual=true)
    private AcceptPayment acceptPayment;
    
    /*** END: state space**/
    
    @DOpt(type = DOpt.Type.Getter)
    @AttrRef(value = "id")
    public int getId() {
        return this.id;
    }

    @DOpt(type = DOpt.Type.AutoAttributeValueGen)
    @AttrRef(value = "id")
    private static int genId(Integer id) {
        Integer val;
        if (id == null) {
            idCounter++;
            val = idCounter;
        } else {
            if (id > idCounter) {
                idCounter = id;
            }
            val = id;
        }
        return val;
    }

//    @DOpt(type = DOpt.Type.Setter)
//    @AttrRef(name = "student")
//    public void setOrder(Order student) {
//        this.student = student;
//    }

    /**
     * This constructor is used for loading objects from data source and for creating a new one 
     * from the object form. In the latter case, it executes the Payment process.
     *  
     * @effects 
     *  initialises this 
     *  
     *  if creating new object from the form
     *    executes the payment process 
     *  
     *  obtain the result and initialise {@link #payDetails}, {@link #description}, {@link #status}.
     */
    @DOpt(type = DOpt.Type.DataSourceConstructor)
    public Payment(Integer id, Invoice invoice, String paymentDetails, String description, Double amount, PaymentStatus status) throws ConstraintViolationException {
      if (id == null) {
        // object from
        this.id = genId(null);
        
        this.invoice = invoice;
        this.customer = invoice.getCustomer();
        
        // executes the payment process
        Map<String, Object> result = executePayment();
        
        // initialise rest of the attributes according to the result obtained
        if (result != null) {
          this.payDetails = (String) result.get(A_payDetails);
          this.description = (String) result.get(A_description);
          this.status = (PaymentStatus) result.get(A_status);
          this.statusStr = this.status.name();
        }
        
        this.amount = (amount != null) ? amount : 0;
      } else {
        // data source
        this.id = genId(id);
        this.invoice = invoice;
        this.payDetails = paymentDetails;
        this.description = description;
        this.status = status;
        this.amount = (amount != null) ? amount : 0;
        this.statusStr = status.name();
      }
    }
    
    public Payment(Invoice invoice, Double amount) {
      this(null, invoice, null, null, amount, null);
    }
    
    @DOpt(type = DOpt.Type.Getter)
    @AttrRef(value = "paymentDetails")
    public String getPayDetails() {
        return this.payDetails;
    }

//    @DOpt(type = DOpt.Type.Setter)
//    @AttrRef(name = "paymentDetails")
//    public void setPayDetails(String paymentDetails) {
//        this.payDetails = paymentDetails;
//    }

    @DOpt(type = DOpt.Type.Getter)
    @AttrRef(value = "description")
    public String getDescription() {
        return this.description;
    }

//    @DOpt(type = DOpt.Type.Setter)
//    @AttrRef(name = "description")
//    public void setDescription(String description) {
//        this.description = description;
//    }

    /**
     * @effects 
     * 
     * @version 
     * 
     */
    @DOpt(type=DOpt.Type.Getter)
    @AttrRef(value="status")
    public PaymentStatus getStatus() {
      return status;
    }

//    @DOpt(type=DOpt.Type.Setter)
//    @AttrRef(name="status")
//    public void setStatus(PaymentStatus status) {
//      this.status = status;
//      
//    }
    
    
    /**
     * @effects return statusStr
     */
    public String getStatusStr() {
      return statusStr;
    }

    
    
    
//    /**
//     * @effects return enrolmentProc
//     */
//    public EnrolmentProcessing getEnrolmentProc() {
//      return enrolmentProc;
//    }
//
//    /**
//     * @effects set enrolmentProc = enrolmentProc
//     */
//    public void setEnrolmentProc(EnrolmentProcessing enrolmentProc) {
//      this.enrolmentProc = enrolmentProc;
//    }
   

    /**
     * @effects return invoice
     */
    public Invoice getInvoice() {
      return invoice;
    }

    /**
     * @effects return customer
     */
    public Customer getCustomer() {
      return customer;
    }

    /**
     * This constructor is exclusively used to execute the Payment process. 
     * All it needs to know is either a Order or 
     * an EnrolmentProcessing object for ({@link #enrolmentProc}) that contains a link to a Order.
     *  
     * @effects <pre>
     *  if student is neq null
     *    initialises this with {@link #student} = <tt>student</tt>
     *  else 
     *    initialises this with {@link #student} = <tt>enrolmentProc.student</tt>
     *    
     *  executes the payment process 
     *  obtain the result and initialise {@link #payDetails}, {@link #description}, {@link #status}.
     *  </pre>
     */
//    @DOpt(type = DOpt.Type.ObjectFormConstructor)
//    public Payment(Order student, // ignored but needs to be present as student is mapped to a data field
//        EnrolmentProcessing enrolmentProc) throws ConstraintViolationException {
//      this.id = genId(null);
//      
//      if (student != null) {
//        this.student = student;
//      } else {
//        this.student = enrolmentProc.getOrder();
//        this.enrolmentProc = enrolmentProc;
//      }
//      
//      // executes the payment process
//      Map<String, Object> result = executeEnrolPayment(this.student);
//      
//      // initialise rest of the attributes according to the result obtained
//      if (result != null) {
//        this.payDetails = (String) result.get(A_payDetails);
//        this.description = (String) result.get(A_description);
//        this.status = (PaymentStatus) result.get(A_status);
//        
//        this.statusStr = status.name();
//      }
//    }
    
    /**
     * @effects 
     *  executes the enrolment payment process for <tt>student</tt> 
     *  return result as {@link Map}<String,Object>, whose keys are 
     *    {{@link #A_payDetails}, {@link #A_description}, {@link #A_status}}
     */
    private Map<String, Object> executePayment() {
      return CustomerPaymentProcess.getInstance().execute(this);
    }

//    @DOpt(type = DOpt.Type.ObjectFormConstructor)
//    public Payment(Order student, String paymentDetails, String description, PaymentStatus status) throws ConstraintViolationException {
//        this.id = genId(null);
//        this.student = student;
//        this.payDetails = paymentDetails;
//        this.description = description;
//        this.status = status;
//    }

//    @DOpt(type = DOpt.Type.RequiredConstructor)
//    public Payment(Order student, String paymentDetails, PaymentStatus status) throws ConstraintViolationException {
//        this.id = genId(null);
//        this.student = student;
//        this.payDetails = paymentDetails;
//        this.description = null;
//        this.status = status;
//    }

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
     */
    public boolean isCompleted() {
      return status.equals(PaymentStatus.ACCEPTED);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    /**
     * @effects 
     * 
     * @version 
     */
    @Override
    public String toString() {
      return "Payment (" + id + ", " + customer + ", " + payDetails + ")";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    /**
     * @effects 
     * 
     * @version 
     */
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + id;
      return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
      Payment other = (Payment) obj;
      if (id != other.id)
        return false;
      return true;
    }

    /**
     * @effects 
     * 
     */
    public double getAmount() {
      return amount;
    }
    
    public void setAmount(double amount) {
      this.amount = amount;
    }
}
