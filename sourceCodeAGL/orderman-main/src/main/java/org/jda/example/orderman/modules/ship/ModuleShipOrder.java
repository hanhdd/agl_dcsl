package org.jda.example.orderman.modules.ship;

import java.util.Collection;

import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.modules.ship.model.ShipOrder;
import org.jda.example.orderman.modules.ship.model.Shipment;

import jda.modules.common.types.properties.PropertyDesc;
import jda.modules.common.types.properties.PropertyName;
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
import jda.mosa.controller.Controller;
import jda.mosa.view.View;
import jda.mosa.view.assets.datafields.JTextField;
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
    name="ShipOrder",
  modelDesc=@ModelDesc(
      model=ShipOrder.class
  ),
  viewDesc=@ViewDesc(
      formTitle="Ship order",
      imageIcon="ship-order.gif",
      viewType=RegionType.Data,
      view=View.class,
      parent=RegionName.Tools,
      layoutBuilderType=TwoColumnLayoutBuilder.class,
      topX=0.5,topY=0.0,widthRatio=0.5f,heightRatio=0.7f
  ),
  controllerDesc = @ControllerDesc(
      controller = Controller.class
      ),
  isPrimary=true
)
public class ModuleShipOrder {
  @AttributeDesc(label="Ship Order")
  private String title;
  
  @AttributeDesc(label="Ship Order Id")
  private int id;
  
  @AttributeDesc(label="Received order",
      width = 20, height=MCCLConstants.STANDARD_FIELD_HEIGHT,
      type=JTextField.class, editable=false
      ,isVisible = false)
  private CustOrder receivedOrder;

  // invoice
  @AttributeDesc(label="Shipment"
      ,type=DefaultPanel.class
      ,styleLabel=StyleName.Heading4DarkYellow
      ,styleField=StyleName.DefaultOnLightYellow
      ,props = {
          @PropertyDesc(name = PropertyName.view_objectForm_autoActivate, valueType = Boolean.class, valueAsString = "true")
      }
      ,controllerDesc=@ControllerDesc(
          openPolicy=OpenPolicy.I))
  private Collection<Shipment> shipments;
  
//  // orders
//  @AttributeDesc(label="Update order"
//    ,layoutBuilderType=TwoColumnLayoutBuilder.class
//      ,controllerDesc=@ControllerDesc(
//          openPolicy=OpenPolicy.I))
//  private Collection<CustOrder> orders;

}
