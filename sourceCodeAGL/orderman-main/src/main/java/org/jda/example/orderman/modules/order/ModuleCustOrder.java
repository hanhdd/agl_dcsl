package org.jda.example.orderman.modules.order;

import java.util.Date;
import java.util.List;

import org.jda.example.orderman.modules.customer.model.Customer;
import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.modules.order.model.OrderLine;
import org.jda.example.orderman.modules.order.model.OrderStatus;
import org.jda.example.orderman.modules.payment.model.Payment;

import jda.modules.mccl.conceptmodel.view.RegionName;
import jda.modules.mccl.conceptmodel.view.RegionType;
import jda.modules.mccl.syntax.MCCLConstants.AlignmentX;
import jda.modules.mccl.syntax.ModuleDescriptor;
import jda.modules.mccl.syntax.controller.ControllerDesc;
import jda.modules.mccl.syntax.controller.ControllerDesc.OpenPolicy;
import jda.modules.mccl.syntax.model.ModelDesc;
import jda.modules.mccl.syntax.view.AttributeDesc;
import jda.modules.mccl.syntax.view.ViewDesc;
import jda.mosa.controller.Controller;
import jda.mosa.view.View;
import jda.mosa.view.assets.datafields.JSimpleFormattedField;
import jda.mosa.view.assets.datafields.datetime.JDateFieldSimple;
import jda.mosa.view.assets.datafields.list.JComboField;
import jda.mosa.view.assets.layout.TwoColumnLayoutBuilder;
import jda.mosa.view.assets.tables.JObjectTable;

@ModuleDescriptor(
name="Order",
modelDesc=@ModelDesc(
    model=CustOrder.class
),
viewDesc=@ViewDesc(
    formTitle="Customer order",
    imageIcon="order.gif",
    viewType=RegionType.Data,
    view=View.class,
    parent=RegionName.Tools,
    layoutBuilderType=TwoColumnLayoutBuilder.class,
    topX=0.5,topY=0.0,widthRatio=0.5f,heightRatio=0.5f
),
controllerDesc=@ControllerDesc(
    controller=Controller.class,
    isDataFieldStateListener=true
)
,isPrimary=true)
public class ModuleCustOrder {
  
  @AttributeDesc(label="Order")
  private String title;
  
  @AttributeDesc(label="Order id")
  private int orderID;

  @AttributeDesc(label="Order date",type=JDateFieldSimple.class)
  private Date orderDate;

  @AttributeDesc(label="Customer")
  private Customer customer;

  @AttributeDesc(label="Payment", isVisible = false)
  private Payment payment;
  
  @AttributeDesc(label="Order lines",type=JObjectTable.class,
      controllerDesc=@ControllerDesc(openPolicy=OpenPolicy.O)  // v2.6.4b
  )
  private List<OrderLine> lines;

  @AttributeDesc(label="Status", type=JComboField.class,
      alignX=AlignmentX.Center
      ,isStateEventSource=true)
  private OrderStatus status;
  
  @AttributeDesc(label="Total",type=JSimpleFormattedField.class)
  private double orderTotal;
}
