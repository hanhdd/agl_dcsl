package org.jda.example.orderman.modules.ship;

import org.jda.example.orderman.modules.customer.model.Customer;
import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.modules.ship.model.Shipment;
import org.jda.example.orderman.util.model.StatusCode;

import jda.modules.mccl.conceptmodel.view.RegionName;
import jda.modules.mccl.conceptmodel.view.RegionType;
import jda.modules.mccl.conceptmodel.view.StyleName;
import jda.modules.mccl.syntax.MCCLConstants;
import jda.modules.mccl.syntax.ModuleDescriptor;
import jda.modules.mccl.syntax.controller.ControllerDesc;
import jda.modules.mccl.syntax.model.ModelDesc;
import jda.modules.mccl.syntax.view.AttributeDesc;
import jda.modules.mccl.syntax.view.ViewDesc;
import jda.mosa.controller.Controller;
import jda.mosa.view.View;
import jda.mosa.view.assets.datafields.JTextField;
import jda.mosa.view.assets.datafields.list.JComboField;
import jda.mosa.view.assets.layout.SequentialLayoutBuilder;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
@ModuleDescriptor(
  name="Shipment",
  modelDesc=@ModelDesc(
      model=Shipment.class
  ),
  viewDesc=@ViewDesc(
      formTitle="Shipment",
      imageIcon="shipment.gif",
      viewType=RegionType.Data,
      view=View.class,
      parent=RegionName.Tools,
      topX=0.5,topY=0.0,widthRatio=0.5f,heightRatio=0.7f
  ),
  isPrimary=true,
  controllerDesc=@ControllerDesc(
      controller=Controller.class,
      isDataFieldStateListener=true
  )
)
public class ModuleShipment {
  @AttributeDesc(label="Shipment")
  private String title;
  
  @AttributeDesc(label="Shipment Id")
  private int id;

  @AttributeDesc(label="Customer",
      width = 20, height=MCCLConstants.STANDARD_FIELD_HEIGHT,
      type=JTextField.class, editable=false)
  private Customer customer;

  @AttributeDesc(label="Order",
      width = 20, height=MCCLConstants.STANDARD_FIELD_HEIGHT,
      type=JTextField.class, editable=false)
  private CustOrder order;
  
  @AttributeDesc(label="Status"
      , type=JComboField.class
      ,isStateEventSource=true
      )
  private StatusCode status;  
}
