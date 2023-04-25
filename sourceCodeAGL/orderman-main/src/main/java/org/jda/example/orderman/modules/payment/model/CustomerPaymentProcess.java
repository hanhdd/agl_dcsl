package org.jda.example.orderman.modules.payment.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.modules.product.model.Product;

/**
 * @overview Represents a process of requesting a designated bank of a a
 *           {@link CustOrder} to perform a {@link Payment}.
 * 
 * @author Duc Minh Le (ducmle)
 *
 * @version
 */
public class CustomerPaymentProcess {

  private static final int MAX_FEE_PER_MODULE = 10 ^ 6;

  private static final PaymentProfile Fixed_Payment_Profile = new PaymentProfile(
      "Bank of Vietnam", "123456789");

  private static CustomerPaymentProcess instance;

  private ExecutorService threadMan;

  private WaitRunnable waitRunnable;

  /**
   * @effects initialise {@link #instance} is not already, and return
   *          {@link #instance}
   */
  public static CustomerPaymentProcess getInstance() {
    if (instance == null) {
      instance = new CustomerPaymentProcess();
    }

    return instance;
  }

  /**
   * @effects
   * 
   *          <pre> 
   *   let amount be computed from the course modules in <tt>order.enrolments</tt>.
   *   request the designated bank of <tt>order</tt> to perform a payment for <tt>amount</tt>.
   *   return result as {@link Map}<String,Object>, whose keys are 
   *    {{@link Payment#A_payDetails}, {@link Payment#A_description}, {@link Payment#A_status}}
   *   </pre>
   */
  public Map<String, Object> execute(Payment payment) {
    // TODO: simulate the following payment process (e.g. showing the progress
    // on a GUI)

    // // (2) look up the payment details of order
     PaymentProfile payProf = lookUpPaymentProfile(payment);
    //
    // // (3) contact the bank to request for payment of the specified amount
     String desc = "Payment for: " + payment;
     PaymentStatus payResult = requestPayment(payProf, desc, payment.getAmount());
    //
    // // (4) return result
    Map<String, Object> result = null;
     if (payResult != null) {
       result = new HashMap<>();
       result.put(Payment.A_payDetails, payProf.getDetailsAsString());
       result.put(Payment.A_description, desc);
       result.put(Payment.A_status, payResult);
     }

    return result;
  }

  /**
   * @effects Request bank in <tt>payProf</tt> to perform payment for
   *          <tt>amount</tt>, with payment description <tt>desc</tt>. Return
   *          result as {@link PaymentStatus}.
   * 
   */
  private PaymentStatus requestPayment(PaymentProfile payProf, String desc,
      double amount) {
    // TODO perform electronic bank request here!!!
    // for now just simulate with a wait-thread and return with a random result

    if (threadMan == null)
      threadMan = Executors.newSingleThreadExecutor();
    if (waitRunnable == null)
      waitRunnable = new WaitRunnable(3, 5);

    try {
      threadMan.submit(waitRunnable).get();
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    PaymentStatus result = PaymentStatus.ACCEPTED;

    return result;
  }

  /**
   * @requires order != null
   * 
   * @effects retrieve and return the PaymentProfile of <tt>order</tt>
   * 
   */
  private PaymentProfile lookUpPaymentProfile(Payment payment) {
    // TODO store payment profile in data store and retrieve them
    // for now, assume the same profile for all orders
    return Fixed_Payment_Profile;
  }

  /**
   * @requires modules != null
   * 
   * @effects compute the sum of fees for all {@link Product}s in
   *          <tt>modules</tt>, return this sum
   */
  private double computePaymentByProducts(Collection<Product> modules) {
    // TODO: store module fees in data source and retrieve them
    // for now, just return a random amount for a module.
    double sumFee = 0;
    double fee;
    for (Product m : modules) {
      // use hashCode of m.toString
      fee = m.toString().hashCode() % MAX_FEE_PER_MODULE;
      sumFee += fee;
    }

    return sumFee;
  }

}
