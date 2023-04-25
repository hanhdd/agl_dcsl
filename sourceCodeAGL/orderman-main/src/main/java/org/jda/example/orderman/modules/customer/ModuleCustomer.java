package org.jda.example.orderman.modules.customer;

import java.util.List;

import org.jda.example.orderman.modules.customer.model.Customer;
import org.jda.example.orderman.modules.order.model.CustOrder;

import jda.modules.mccl.conceptmodel.view.RegionName;
import jda.modules.mccl.conceptmodel.view.RegionType;
import jda.modules.mccl.syntax.ModuleDescriptor;
import jda.modules.mccl.syntax.controller.ControllerDesc;
import jda.modules.mccl.syntax.controller.ControllerDesc.OpenPolicy;
import jda.modules.mccl.syntax.model.ModelDesc;
import jda.modules.mccl.syntax.view.AttributeDesc;
import jda.modules.mccl.syntax.view.ViewDesc;
import jda.mosa.view.View;
import jda.mosa.view.assets.layout.TwoColumnLayoutBuilder;
import jda.mosa.view.assets.panels.DefaultPanel;

@ModuleDescriptor(
name="Customer",
modelDesc=@ModelDesc(
    model=Customer.class
),
viewDesc=@ViewDesc(
    formTitle="Customer",
    imageIcon="customer.gif",
    viewType=RegionType.Data,
    view=View.class,
    parent=RegionName.Tools,
    layoutBuilderType=TwoColumnLayoutBuilder.class,
    topX=0.5,topY=0.0,widthRatio=0.5f,heightRatio = 0.5f
),
isViewer=false,
isPrimary=true
)
public class ModuleCustomer {
  
  @AttributeDesc(label="Customer")
  private String title;
  
  @AttributeDesc(label="Id")
  private int id;

  @AttributeDesc(label="Name")
  private String name;
  
//  @AttributeDesc(label="Customer address")
//  private String customerAddress;
  
//  @AttributeDesc(label="Balance",editable=false,type=JSimpleFormattedField.class)
//  private double balance;
  
  @AttributeDesc(label="Orders",type=DefaultPanel.class,
      controllerDesc=@ControllerDesc(openPolicy=OpenPolicy.O_C)  // v2.6.4b
  )
  private List<CustOrder> orders;  
}
