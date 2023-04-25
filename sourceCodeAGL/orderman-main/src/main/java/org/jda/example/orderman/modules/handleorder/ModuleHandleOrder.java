package org.jda.example.orderman.modules.handleorder;

import static jda.modules.mccl.conceptmodel.view.RegionName.Chart;
import static jda.modules.mccl.conceptmodel.view.RegionName.Delete;
import static jda.modules.mccl.conceptmodel.view.RegionName.Export;
import static jda.modules.mccl.conceptmodel.view.RegionName.First;
import static jda.modules.mccl.conceptmodel.view.RegionName.Last;
import static jda.modules.mccl.conceptmodel.view.RegionName.Next;
import static jda.modules.mccl.conceptmodel.view.RegionName.ObjectScroll;
import static jda.modules.mccl.conceptmodel.view.RegionName.Open;
import static jda.modules.mccl.conceptmodel.view.RegionName.Previous;
import static jda.modules.mccl.conceptmodel.view.RegionName.Print;
import static jda.modules.mccl.conceptmodel.view.RegionName.Update;

import java.util.Collection;

import org.jda.example.orderman.modules.delivery.model.CollectPayment;
import org.jda.example.orderman.modules.delivery.model.Delivery;
import org.jda.example.orderman.modules.fillorder.ModuleFillOrder;
import org.jda.example.orderman.modules.fillorder.model.FillOrder;
import org.jda.example.orderman.modules.handleorder.model.HandleOrder;
import org.jda.example.orderman.modules.order.ModuleCustOrder;
import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.modules.payment.model.AcceptPayment;

import jda.modules.common.CommonConstants;
import jda.modules.common.types.properties.PropertyDesc;
import jda.modules.common.types.properties.PropertyName;
import jda.modules.mbsl.controller.command.ExecActivityCommand;
import jda.modules.mccl.conceptmodel.view.RegionName;
import jda.modules.mccl.conceptmodel.view.RegionType;
import jda.modules.mccl.conceptmodel.view.StyleName;
import jda.modules.mccl.syntax.ModuleDescriptor;
import jda.modules.mccl.syntax.SetUpDesc;
import jda.modules.mccl.syntax.containment.CEdge;
import jda.modules.mccl.syntax.containment.CTree;
import jda.modules.mccl.syntax.containment.ScopeDesc;
import jda.modules.mccl.syntax.controller.ControllerDesc;
import jda.modules.mccl.syntax.controller.ControllerDesc.OpenPolicy;
import jda.modules.mccl.syntax.model.ModelDesc;
import jda.modules.mccl.syntax.view.AttributeDesc;
import jda.modules.mccl.syntax.view.ViewDesc;
import jda.modules.setup.commands.CopyResourceFilesCommand;
import jda.mosa.controller.Controller;
import jda.mosa.view.View;
import jda.mosa.view.assets.datafields.JSimpleFormattedField;
import jda.mosa.view.assets.layout.DecisionalLayoutBuilder;
import jda.mosa.view.assets.layout.TwoColumnLayoutBuilder;
import jda.mosa.view.assets.panels.DefaultPanel;

@ModuleDescriptor(
    name="ModuleHandleOrder",
    modelDesc=@ModelDesc(
        model=HandleOrder.class
    ),
    viewDesc=@ViewDesc(
        formTitle="Handle customer orders",
        domainClassLabel="Order Handling",    
        imageIcon="order-handling.jpg",
        view=View.class,
        viewType=RegionType.Data,
        layoutBuilderType=DecisionalLayoutBuilder.class, //TabLayoutBuilder.class,
        topX=0.5,topY=0.0,widthRatio = 0.5f, heightRatio = 0.5f,
        parent=RegionName.Tools,
        excludeComponents={
          // general actions
          Export, Print, Chart,
          // object-related actions
          Open, Update, Delete, //New,
          First, Previous, Next, Last, ObjectScroll,
        }
    ),
    controllerDesc=@ControllerDesc(
        controller=Controller.class
        /*customise createNew command to execute the activity model*/
        ,props= {
          @PropertyDesc(name=PropertyName.controller_dataController_new, valueIsClass=ExecActivityCommand.class, valueType=Class.class, valueAsString=CommonConstants.NullString)
        }
    )
    ,containmentTree=@CTree(
        root=HandleOrder.class
        ,edges = {
            @CEdge(parent=FillOrder.class, child=CustOrder.class, 
                scopeDesc = @ScopeDesc(
                    stateScope = {"orderID", "orderDate", "status"}
                    ,attribDescs = {                        @AttributeDesc(id="orderDate",editable=false,type=JSimpleFormattedField.class),
                        @AttributeDesc(id="orderID",editable = false)
                    }
                ))
            , @CEdge(parent=CollectPayment.class, child=CustOrder.class, 
                scopeDesc = @ScopeDesc(
                    stateScope = {"orderID", "orderDate", "status"}
                    ,attribDescs = {                        @AttributeDesc(id="orderDate",editable=false,type=JSimpleFormattedField.class),
                        @AttributeDesc(id="orderID",editable = false)
                    }
                ))
            ,@CEdge(parent=AcceptPayment.class, child=CustOrder.class, 
            scopeDesc = @ScopeDesc(
                stateScope = {"orderID", "orderDate", "status"}
                ,attribDescs = {                        @AttributeDesc(id="orderDate",editable=false,type=JSimpleFormattedField.class),
                    @AttributeDesc(id="orderID",editable = false)
                }
            ))
            ,@CEdge(parent=Delivery.class, child=CustOrder.class, 
            scopeDesc = @ScopeDesc(
                stateScope = {"orderID", "orderDate", "status"}
                ,attribDescs = {                        @AttributeDesc(id="orderDate",editable=false,type=JSimpleFormattedField.class),
                    @AttributeDesc(id="orderID",editable = false)
                }
            ))
        }        
    )
    ,isPrimary=true
    ,childModules={ModuleCustOrder.class, ModuleFillOrder.class }
    ,setUpDesc=@SetUpDesc(postSetUp=CopyResourceFilesCommand.class)
)
public class ModuleHandleOrder {
  @AttributeDesc(label="Order Handling")
  private String title;

  // receive order 
  @AttributeDesc(label="Receive order",
      type=DefaultPanel.class,
      layoutBuilderType=TwoColumnLayoutBuilder.class
      ,styleLabel=StyleName.Heading4DarkYellow
      ,styleField=StyleName.DefaultOnLightYellow
      ,controllerDesc=@ControllerDesc(
          openPolicy=OpenPolicy.I))
  private Collection<CustOrder> orders;
  
  // fill order
  @AttributeDesc(label="Fill Order"
      ,type=DefaultPanel.class)
  private Collection<FillOrder> fillOrders;
  
}
