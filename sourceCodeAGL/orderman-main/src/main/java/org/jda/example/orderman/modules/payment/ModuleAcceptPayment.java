package org.jda.example.orderman.modules.payment;

import java.util.Collection;

import org.jda.example.orderman.modules.invoice.model.Invoice;
import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.modules.payment.model.AcceptPayment;
import org.jda.example.orderman.modules.payment.model.Payment;

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
    name="AcceptPayment",
  modelDesc=@ModelDesc(
      model=AcceptPayment.class
  ),
  viewDesc=@ViewDesc(
      formTitle="Accept payment",
      imageIcon="accept-payment.gif",
      viewType=RegionType.Data,
      view=View.class,
      parent=RegionName.Tools,
      layoutBuilderType=SequentialLayoutBuilder.class,
      topX=0.5,topY=0.0,widthRatio=0.5f,heightRatio=0.7f
  ),
  isPrimary=true
)
public class ModuleAcceptPayment {
  @AttributeDesc(label="Accept Payment")
  private String title;
  
  @AttributeDesc(label="Invoice",
      width = 20, height=MCCLConstants.STANDARD_FIELD_HEIGHT,
      type=JTextField.class, editable=false, 
      isVisible=false)
  private Invoice invoice;

  // payment
  @AttributeDesc(label="Payments"
      ,type=DefaultPanel.class
      ,styleLabel=StyleName.Heading4DarkYellow
      ,styleField=StyleName.DefaultOnLightYellow
      ,layoutBuilderType=TwoColumnLayoutBuilder.class
      ,controllerDesc=@ControllerDesc(
          openPolicy=OpenPolicy.I))
  private Collection<Payment> payments;
  
  // orders
  @AttributeDesc(label="Update order"
      ,type=DefaultPanel.class
      ,styleLabel=StyleName.Heading4DarkYellow
      ,styleField=StyleName.DefaultOnLightYellow
      ,layoutBuilderType=TwoColumnLayoutBuilder.class
      ,controllerDesc=@ControllerDesc(
          openPolicy=OpenPolicy.I))
  private Collection<CustOrder> orders;

}
