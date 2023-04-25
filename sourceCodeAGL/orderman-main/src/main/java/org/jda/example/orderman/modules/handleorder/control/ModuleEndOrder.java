package org.jda.example.orderman.modules.handleorder.control;

import java.util.Collection;

import org.jda.example.orderman.modules.handleorder.control.model.AcceptOrNot;
import org.jda.example.orderman.modules.handleorder.control.model.CompleteOrder;
import org.jda.example.orderman.modules.handleorder.control.model.EndOrder;

import jda.modules.common.types.properties.PropertyDesc;
import jda.modules.common.types.properties.PropertyName;
import jda.modules.mccl.syntax.ModuleDescriptor;
import jda.modules.mccl.syntax.SetUpDesc;
import jda.modules.mccl.syntax.model.ModelDesc;
import jda.modules.mccl.syntax.view.AttributeDesc;
import jda.modules.mccl.syntax.view.ViewDesc;
import jda.modules.setup.commands.CopyResourceFilesCommand;
import jda.mosa.view.assets.panels.DefaultPanel;

@ModuleDescriptor(
    name="ModuleEndOrder",
    modelDesc=@ModelDesc(
        model=EndOrder.class
    ),
    viewDesc=@ViewDesc(
        formTitle="-",
        domainClassLabel="EndOrder",    
        imageIcon="endOrder.jpg"
        //view=View.class,
        //viewType=Region.Type.Data,
        //layoutBuilderType=TabLayoutBuilder.class
        //topX=0.5,topY=0.0,//widthRatio=0.9f,heightRatio=0.9f,
        //parent=RegionName.Tools,
    )
    ,isPrimary=true,
//    childModules={ModuleCompleteOrder.class},
    setUpDesc=@SetUpDesc(postSetUp=CopyResourceFilesCommand.class)
)
public class ModuleEndOrder {
  @AttributeDesc(label="End Order")
  private String title;

  @AttributeDesc(label="Reject Order"
      ,type=DefaultPanel.class
      ,props={
        @PropertyDesc(name=PropertyName.view_objectForm_autoActivate, valueType=Boolean.class, valueAsString="true")
      }
  )
  private Collection<AcceptOrNot> acceptOrNots;
  
  // complete orders 
  @AttributeDesc(label="Complete Order"
      ,type=DefaultPanel.class
      ,props={
        @PropertyDesc(name=PropertyName.view_objectForm_autoActivate, valueType=Boolean.class, valueAsString="true")
      }
  )
  private Collection<CompleteOrder> completedOrders;
  
}
