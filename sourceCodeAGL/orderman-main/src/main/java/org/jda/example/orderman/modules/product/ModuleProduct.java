package org.jda.example.orderman.modules.product;

import java.util.List;

import org.jda.example.orderman.modules.order.model.OrderLine;
import org.jda.example.orderman.modules.product.model.Product;

import jda.modules.mccl.conceptmodel.view.RegionType;
import jda.modules.mccl.syntax.ModuleDescriptor;
import jda.modules.mccl.syntax.controller.ControllerDesc;
import jda.modules.mccl.syntax.controller.ControllerDesc.OpenPolicy;
import jda.modules.mccl.syntax.model.ModelDesc;
import jda.modules.mccl.syntax.view.AttributeDesc;
import jda.modules.mccl.syntax.view.ViewDesc;
import jda.mosa.view.View;
import jda.mosa.view.assets.datafields.JSimpleFormattedField;

@ModuleDescriptor(
name="Product",
modelDesc=@ModelDesc(
    model=Product.class
),
viewDesc=@ViewDesc(
    formTitle="Product",
    imageIcon="product.gif",
    viewType=RegionType.Data,
    view=View.class
),
isPrimary=true
)
public class ModuleProduct {
  
  @AttributeDesc(label="Product")
  private String title;
  
  @AttributeDesc(label="Id")
  private int productID;
  
  @AttributeDesc(label="Description")
  private String productDescr;

  @AttributeDesc(label="Unit price",type=JSimpleFormattedField.class)
  private double unitPrice;
  
  @AttributeDesc(label="Order lines",
      controllerDesc=@ControllerDesc(openPolicy=OpenPolicy.O)  // v2.6.4b
  )
  private List<OrderLine> orderLines;
  
  
  @AttributeDesc(label="Total sales",type=JSimpleFormattedField.class)
  private Double totalSales;

//  @AttributeDesc(label="Số lượng bán của tháng",type=JSimpleFormattedField.class)
//  private Double quantityByMonth;
//  
//  @AttributeDesc(label="Tổng bán của tháng",type=JSimpleFormattedField.class)
//  private Double totalSalesByMonth;

}