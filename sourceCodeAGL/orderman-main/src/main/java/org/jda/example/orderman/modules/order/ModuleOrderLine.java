package org.jda.example.orderman.modules.order;

import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.modules.order.model.OrderLine;
import org.jda.example.orderman.modules.product.model.Product;

import jda.modules.dcsl.syntax.Select;
import jda.modules.mccl.conceptmodel.view.RegionName;
import jda.modules.mccl.conceptmodel.view.RegionType;
import jda.modules.mccl.syntax.ModuleDescriptor;
import jda.modules.mccl.syntax.controller.ControllerDesc;
import jda.modules.mccl.syntax.model.ModelDesc;
import jda.modules.mccl.syntax.view.AttributeDesc;
import jda.modules.mccl.syntax.view.ViewDesc;
import jda.mosa.controller.Controller;
import jda.mosa.view.View;
import jda.mosa.view.assets.datafields.JSimpleFormattedField;
import jda.mosa.view.assets.layout.TwoColumnLayoutBuilder;

@ModuleDescriptor(
name="OrderLine",
modelDesc=@ModelDesc(
    model=OrderLine.class
),
viewDesc=@ViewDesc(
    formTitle="Order Line",
    imageIcon="orderline.gif",
    viewType=RegionType.Data,
    view=View.class,
    parent=RegionName.Tools,
    layoutBuilderType=TwoColumnLayoutBuilder.class,
    topX=0.5,topY=0.0,widthRatio=0.5f,heightRatio = 0.5f
)
,
controllerDesc=@ControllerDesc(
    controller=Controller.class,
    isDataFieldStateListener=true
)
,isPrimary=true)
public class ModuleOrderLine {
  @AttributeDesc(label="Order Line")
  private String title;
  
  @AttributeDesc(label="Id")
  private int id;
  
  @AttributeDesc(label="Order")
  private CustOrder order;
  
  @AttributeDesc(label="Product",
      ref=@Select(clazz=Product.class,attributes={"productDescr"}))
  private Product product;

  @AttributeDesc(label="Unit price",editable=false,type=JSimpleFormattedField.class)
  private double unitPrice;

  @AttributeDesc(label="Quantity",type=JSimpleFormattedField.class)
  private int quantity;

  @AttributeDesc(label="Line Total",editable=false,type=JSimpleFormattedField.class)
  private int lineTotal;
}
