package org.jda.example.orderman.modules.sales;

import org.jda.example.orderman.modules.sales.model.SalesPerson;

import jda.modules.mccl.conceptmodel.view.RegionType;
import jda.modules.mccl.syntax.ModuleDescriptor;
import jda.modules.mccl.syntax.model.ModelDesc;
import jda.modules.mccl.syntax.view.AttributeDesc;
import jda.modules.mccl.syntax.view.ViewDesc;
import jda.mosa.view.View;

/**
 * @overview 
 *
 * @author Duc Minh Le (ducmle)
 *
 * @version 
 */
@ModuleDescriptor(
  name="Customer",
  modelDesc=@ModelDesc(
      model=SalesPerson.class
  ),
  viewDesc=@ViewDesc(
      formTitle="SalesPerson",
      imageIcon="salesperson.gif",
      viewType=RegionType.Data,
      view=View.class
  ),
  isPrimary=true
)
public class ModuleSalesPerson {
        
  @AttributeDesc(label="SalesPerson")
  private String title;
  
  @AttributeDesc(label="Id")
  private int id;

  @AttributeDesc(label="Name")
  private String name;
}
