package org.jda.example.orderman.modules.invoice;

import org.jda.example.orderman.modules.customer.model.Customer;
import org.jda.example.orderman.modules.invoice.model.Invoice;
import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.util.model.StatusCode;

import jda.modules.mccl.conceptmodel.view.RegionName;
import jda.modules.mccl.conceptmodel.view.RegionType;
import jda.modules.mccl.syntax.MCCLConstants;
import jda.modules.mccl.syntax.ModuleDescriptor;
import jda.modules.mccl.syntax.model.ModelDesc;
import jda.modules.mccl.syntax.view.AttributeDesc;
import jda.modules.mccl.syntax.view.ViewDesc;
import jda.mosa.view.View;
import jda.mosa.view.assets.datafields.JTextField;
import jda.mosa.view.assets.datafields.list.JComboField;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
@ModuleDescriptor(
  name="Invoice",
  modelDesc=@ModelDesc(
      model=Invoice.class
  ),
  viewDesc=@ViewDesc(
      formTitle="Invoice",
      imageIcon="invoice.gif",
      viewType=RegionType.Data,
      view=View.class,
      parent=RegionName.Tools,
      topX=0.5,topY=0.0,widthRatio=0.5f,heightRatio=0.6f
      
  ),
  isPrimary=true
)
public class ModuleInvoice {
  
  @AttributeDesc(label="Invoice")
  private String title;

  @AttributeDesc(label="Invoice Id")
  private int id;

  @AttributeDesc(label="Customer",
      width = 20, height=MCCLConstants.STANDARD_FIELD_HEIGHT,
      type=JTextField.class, editable=false)
  private Customer customer;

  @AttributeDesc(label="Order",
      width = 20, height=MCCLConstants.STANDARD_FIELD_HEIGHT,
      type=JTextField.class, editable=false)
  private CustOrder order;
  
  @AttributeDesc(label="Status", type=JComboField.class)
  private StatusCode status;  
}
