package org.jda.example.orderman.modules.delivery;

import java.util.Collection;

import org.jda.example.orderman.modules.delivery.model.CollectPayment;
import org.jda.example.orderman.modules.invoice.model.Invoice;
import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.modules.payment.model.AcceptPayment;

import jda.modules.mccl.conceptmodel.view.RegionName;
import jda.modules.mccl.conceptmodel.view.RegionType;
import jda.modules.mccl.conceptmodel.view.StyleName;
import jda.modules.mccl.syntax.MCCLConstants;
import jda.modules.mccl.syntax.ModuleDescriptor;
import jda.modules.mccl.syntax.controller.ControllerDesc;
import jda.modules.mccl.syntax.controller.ControllerDesc.OpenPolicy;
import jda.modules.mccl.syntax.model.ModelDesc;
import jda.modules.mccl.syntax.view.AttributeDesc;
import jda.modules.mccl.syntax.view.ViewDesc;
import jda.mosa.view.View;
import jda.mosa.view.assets.datafields.JTextField;
import jda.mosa.view.assets.layout.SequentialLayoutBuilder;
import jda.mosa.view.assets.layout.TwoColumnLayoutBuilder;
import jda.mosa.view.assets.panels.DefaultPanel;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */

@ModuleDescriptor(
    name="CollectPayment",
  modelDesc=@ModelDesc(
      model=CollectPayment.class
  ),
  viewDesc=@ViewDesc(
      formTitle="Collect payment",
      imageIcon="collect-payment.gif",
      viewType=RegionType.Data,
      view=View.class,
      parent=RegionName.Tools,
      layoutBuilderType=SequentialLayoutBuilder.class,
      topX=0.5,topY=0.0,widthRatio=0.5f,heightRatio=0.7f
  ),
  isPrimary=true
)
public class ModuleCollectPayment {
  @AttributeDesc(label="Collect Payment")
  private String title;
  
  @AttributeDesc(label="Collect Payment Id")
  private int id;
  
  @AttributeDesc(label="Received order",
      width = 20, height=MCCLConstants.STANDARD_FIELD_HEIGHT,
      type=JTextField.class, editable=false
      ,isVisible = false)
  private CustOrder receivedOrder;

  // invoice
  @AttributeDesc(label="Invoice"
      ,type=DefaultPanel.class
      ,styleLabel=StyleName.Heading4DarkYellow
      ,styleField=StyleName.DefaultOnLightYellow
      ,controllerDesc=@ControllerDesc(
          openPolicy=OpenPolicy.I))
  private Collection<Invoice> invoices;
  
  // orders
//  @AttributeDesc(label="Update order"
//    ,type=DefaultPanel.class
//    ,controllerDesc=@ControllerDesc(
//          openPolicy=OpenPolicy.I))
//  private Collection<CustOrder> orders;

  @AttributeDesc(label="Accept payment"
      ,type=DefaultPanel.class
      ,styleLabel=StyleName.Heading4DarkYellow
      ,styleField=StyleName.DefaultOnLightYellow
      ,controllerDesc=@ControllerDesc(
            openPolicy=OpenPolicy.I))
  private Collection<AcceptPayment> acceptPayments;
}
