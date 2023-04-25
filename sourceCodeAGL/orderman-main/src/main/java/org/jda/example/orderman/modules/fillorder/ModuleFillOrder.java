package org.jda.example.orderman.modules.fillorder;

import java.util.Collection;

import org.jda.example.orderman.modules.delivery.model.Delivery;
import org.jda.example.orderman.modules.fillorder.model.FillOrder;
import org.jda.example.orderman.modules.order.model.CustOrder;

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

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */

@ModuleDescriptor(
    name="FillOrder",
  modelDesc=@ModelDesc(
      model=FillOrder.class
  ),
  viewDesc=@ViewDesc(
      formTitle="Fill order",
      imageIcon="fulfillment.gif",
      viewType=RegionType.Data,
      view=View.class,
      parent=RegionName.Tools,
      layoutBuilderType=SequentialLayoutBuilder.class,
      topX=0.5,topY=0.0,widthRatio=0.5f,heightRatio=0.7f
  ),
  isPrimary=true
)
public class ModuleFillOrder {
  @AttributeDesc(label="Fill Order")
  private String title;
  
  @AttributeDesc(label="Fullfill Id")
  private int id;
  
  @AttributeDesc(label="Fulfill order",
      width = 20, height=MCCLConstants.STANDARD_FIELD_HEIGHT,
      type=JTextField.class, editable=false)
  private CustOrder receivedOrder;

  // receive order 
  @AttributeDesc(label="Update order"
    ,styleLabel=StyleName.Heading4DarkYellow
    ,styleField=StyleName.DefaultOnLightYellow
    ,layoutBuilderType=TwoColumnLayoutBuilder.class
      ,controllerDesc=@ControllerDesc(
          openPolicy=OpenPolicy.I))
  private Collection<CustOrder> orders;
  
  // fill order
  @AttributeDesc(label="Delivery"
      ,styleLabel=StyleName.Heading4DarkYellow
      ,styleField=StyleName.DefaultOnLightYellow
      ,controllerDesc=@ControllerDesc(
          openPolicy=OpenPolicy.I)
      )
  private Collection<Delivery> deliveries;
}
