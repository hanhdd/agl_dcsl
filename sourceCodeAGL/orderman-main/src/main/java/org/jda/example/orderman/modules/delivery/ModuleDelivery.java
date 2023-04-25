package org.jda.example.orderman.modules.delivery;

import java.util.Collection;

import org.jda.example.orderman.modules.delivery.model.CollectPayment;
import org.jda.example.orderman.modules.delivery.model.Delivery;
import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.modules.ship.model.ShipOrder;

import jda.modules.common.types.properties.PropertyDesc;
import jda.modules.common.types.properties.PropertyName;
import jda.modules.mccl.conceptmodel.view.RegionName;
import jda.modules.mccl.conceptmodel.view.RegionType;
import jda.modules.mccl.syntax.MCCLConstants;
import jda.modules.mccl.syntax.ModuleDescriptor;
import jda.modules.mccl.syntax.SetUpDesc;
import jda.modules.mccl.syntax.controller.ControllerDesc;
import jda.modules.mccl.syntax.controller.ControllerDesc.OpenPolicy;
import jda.modules.mccl.syntax.model.ModelDesc;
import jda.modules.mccl.syntax.view.AttributeDesc;
import jda.modules.mccl.syntax.view.ViewDesc;
import jda.modules.setup.commands.CopyResourceFilesCommand;
import jda.mosa.view.View;
import jda.mosa.view.assets.datafields.JTextField;
import jda.mosa.view.assets.layout.ForkedLayoutBuilder;
import jda.mosa.view.assets.panels.DefaultPanel;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */

@ModuleDescriptor(
    name="Delivery",
  modelDesc=@ModelDesc(
      model=Delivery.class
  ),
  viewDesc=@ViewDesc(
      formTitle="Delivery",
      imageIcon="delivery.gif",
      viewType=RegionType.Data,
      view=View.class,
      parent=RegionName.Tools,
      layoutBuilderType=ForkedLayoutBuilder.class,//TabLayoutBuilder.class,
      topX=0.5,topY=0.0,widthRatio=0.5f,heightRatio=0.7f
  ),
  isPrimary=true
  ,setUpDesc=@SetUpDesc(postSetUp=CopyResourceFilesCommand.class)
)
public class ModuleDelivery {
  @AttributeDesc(label="Delivery")
  private String title;
  
  @AttributeDesc(label="Delivery Id")
  private int id;
  
  @AttributeDesc(label="Received order",
      width = 20, height=MCCLConstants.STANDARD_FIELD_HEIGHT,
      type=JTextField.class, editable=false
      ,isVisible = false
      )
  private CustOrder receivedOrder;

  // collect payments
  @AttributeDesc(label="Collect payment"
    ,type=DefaultPanel.class
//    ,layoutBuilderType=TwoColumnLayoutBuilder.class
      ,controllerDesc=@ControllerDesc(
          openPolicy=OpenPolicy.I)
    ,props= {
        @PropertyDesc(name = PropertyName.tag, valueAsString = "branch", valueType = String.class)
    }
    )
  private Collection<CollectPayment> collectPayments;
  
  // ship order
  @AttributeDesc(label="Ship order"
    ,type=DefaultPanel.class
//      ,layoutBuilderType=TwoColumnLayoutBuilder.class
      ,controllerDesc=@ControllerDesc(
          openPolicy=OpenPolicy.I)
    ,props= {
        @PropertyDesc(name = PropertyName.tag, valueAsString = "branch", valueType = String.class)
  })
  private Collection<ShipOrder> shipOrders;
  
  // close order
  @AttributeDesc(label="Close Order"
      ,type=DefaultPanel.class
//      ,styleLabel=StyleName.Heading4DarkYellow
//      ,styleField=StyleName.DefaultOnLightYellow
      ,controllerDesc=@ControllerDesc(
          openPolicy=OpenPolicy.I))
  private Collection<CustOrder> closeOrders;
  
}
