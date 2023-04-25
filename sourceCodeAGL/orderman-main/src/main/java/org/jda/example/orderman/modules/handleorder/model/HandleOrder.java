package org.jda.example.orderman.modules.handleorder.model;

import static jda.modules.mbsl.model.graph.NodeType.Coordinator;
import static jda.modules.mbsl.model.graph.NodeType.Decision;
import static jda.modules.mbsl.model.graph.NodeType.Fork;
import static jda.modules.mbsl.model.graph.NodeType.Join;
import static jda.modules.mbsl.model.graph.NodeType.Merge;
import static jda.mosa.controller.assets.util.AppState.Created;
import static jda.mosa.controller.assets.util.AppState.NewObject;
import static jda.mosa.controller.assets.util.AppState.Updated;
import static jda.mosa.controller.assets.util.MethodName.activateView;
import static jda.mosa.controller.assets.util.MethodName.createObject;
import static jda.mosa.controller.assets.util.MethodName.filterInput;
import static jda.mosa.controller.assets.util.MethodName.newObject;
import static jda.mosa.controller.assets.util.MethodName.setDataFieldValues;
import static jda.mosa.controller.assets.util.MethodName.showObject;
import static jda.mosa.controller.assets.util.MethodName.updateObject;

import java.util.Collection;

import org.jda.example.orderman.modules.delivery.model.CollectPayment;
import org.jda.example.orderman.modules.delivery.model.Delivery;
import org.jda.example.orderman.modules.fillorder.model.FillOrder;
import org.jda.example.orderman.modules.handleorder.control.model.AcceptOrNot;
import org.jda.example.orderman.modules.handleorder.control.model.CompleteOrder;
import org.jda.example.orderman.modules.handleorder.control.model.EndOrder;
import org.jda.example.orderman.modules.invoice.model.Invoice;
import org.jda.example.orderman.modules.order.filter.FilterCustOrderFromPayment;
import org.jda.example.orderman.modules.order.model.CustOrder;
import org.jda.example.orderman.modules.payment.model.AcceptPayment;
import org.jda.example.orderman.modules.payment.model.Payment;
import org.jda.example.orderman.modules.ship.model.ShipOrder;
import org.jda.example.orderman.modules.ship.model.Shipment;

import jda.modules.dcsl.syntax.DAssoc;
import jda.modules.dcsl.syntax.DAssoc.AssocEndType;
import jda.modules.dcsl.syntax.DAssoc.AssocType;
import jda.modules.dcsl.syntax.DAssoc.Associate;
import jda.modules.dcsl.syntax.DAttr;
import jda.modules.dcsl.syntax.DAttr.Type;
import jda.modules.dcsl.syntax.DCSLConstants;
import jda.modules.dcsl.syntax.DClass;
import jda.modules.dcsl.syntax.Select;
import jda.modules.mbsl.model.appmodules.meta.MAct;
import jda.modules.mbsl.model.graph.meta.AGraph;
import jda.modules.mbsl.model.graph.meta.ANode;
import jda.mosa.controller.ControllerBasic.DataController;


/**
 * @overview
 *  Order handling.
 *  
 * @author ducmle
 */
/**Activity graph configuration */
@AGraph(nodes={
/* 1 */    
@ANode(label="1:CustOrder", zone="top",init=true, 
      refCls=CustOrder.class, serviceCls=DataController.class, 
      outNodes= {"2:AcceptOrNot"},
      actSeq={
        // create new and wait until a new object is created
        @MAct(actName=newObject, endStates={Created})
        }),
/* 2: decisional */    
@ANode(label="2:AcceptOrNot", 
      refCls=AcceptOrNot.class, nodeType=Decision, 
      outNodes= {"3:FillOrder", "4:EndOrder"}),
/* 3 */    
@ANode(label="3:FillOrder", zone="top", refCls=FillOrder.class, serviceCls=DataController.class,
    outNodes= {"5:CustOrder"},
    nodeType=Coordinator,
    actSeq = {
      @MAct(actName=newObject, endStates={NewObject}),
      @MAct(actName=setDataFieldValues, attribNames={"receivedOrder"}),
      @MAct(actName=createObject, endStates={Created})
    }),
/* 4 */    
@ANode(label="4:EndOrder", refCls=EndOrder.class, 
      nodeType=Merge, outNodes= {"7:CustOrder"}),
/* 5 */    
@ANode(label="5:CustOrder", zone="3:FillOrder",refCls=CustOrder.class, serviceCls=DataController.class,
    outNodes = {"6:Delivery"},
    actSeq = {
      @MAct(actName=showObject, endStates = {Updated})
    }),
/* 6: Fork */    
@ANode(label="6:Delivery", zone="3:FillOrder",refCls=Delivery.class, 
      nodeType=Fork, outNodes= {"8:CollectPayment", "9:ShipOrder"},
    actSeq = {
      @MAct(actName=newObject, endStates={NewObject}),
      @MAct(actName=createObject, endStates={Created})      
}
    ),
/* 7 */    
@ANode(label="7:CustOrder",zone="6:Delivery", refCls=CustOrder.class, serviceCls=DataController.class, 
      actSeq={
        @MAct(actName=activateView),
        @MAct(actName=showObject),
        @MAct(actName=updateObject, endStates = {Updated})
        }),
/* 8 */    
@ANode(label="8:CollectPayment", zone="6:Delivery",refCls=CollectPayment.class, 
      serviceCls=DataController.class,
      nodeType=Coordinator,
      outNodes= {"10:Invoice"},
      actSeq={
        @MAct(actName=newObject, endStates={NewObject}),
        @MAct(actName=setDataFieldValues, attribNames = {"receivedOrder"}),
        @MAct(actName=createObject, endStates={Created})
        }),
/* 9 */    
@ANode(label="9:ShipOrder", zone="6:Delivery",refCls=ShipOrder.class, serviceCls=DataController.class,
      nodeType=Coordinator,
      outNodes= {"12:Shipment"},
      actSeq={
        @MAct(actName=newObject, endStates={NewObject}),
        @MAct(actName=setDataFieldValues, attribNames = {"receivedOrder"}, endStates={Created})
        }),
/* 10 */    
@ANode(label="10:Invoice", zone="8:CollectPayment",refCls=Invoice.class, serviceCls=DataController.class, 
      outNodes = {"11:AcceptPayment"},
      actSeq={
        // create new and wait until a new object is created
        @MAct(actName=newObject, endStates={NewObject}),
        @MAct(actName=setDataFieldValues, attribNames = {"order"}, endStates={Created})
        }),
/* 11 */    
@ANode(label="11:AcceptPayment", zone="8:CollectPayment",refCls=AcceptPayment.class, serviceCls=DataController.class,
      outNodes= {"14:Payment"},
      nodeType=Coordinator,      
      actSeq={
        @MAct(actName=newObject, endStates={NewObject}),
        @MAct(actName=setDataFieldValues, attribNames = {"invoice"}),
        @MAct(actName=createObject, endStates={Created})
        }),
/* 12 */    
@ANode(label="12:Shipment", zone="9:ShipOrder",refCls=Shipment.class, serviceCls=DataController.class,
      outNodes = { "13:CompleteOrder" },
      actSeq={
        // create new and wait until a new object is created
        @MAct(actName=newObject, endStates={NewObject}),
        @MAct(actName=setDataFieldValues, attribNames = {"order"}, endStates={Created})
        }),
/* 13 */    
@ANode(label="13:CompleteOrder",refCls=CompleteOrder.class, nodeType=Join, 
      outNodes= {"4:EndOrder"}),
/* 14 */    
@ANode(label="14:Payment", zone="11:AcceptPayment",refCls=Payment.class, serviceCls=DataController.class, 
       outNodes= { "15:CustOrder" },
actSeq={
  @MAct(actName=newObject, endStates = {NewObject}),
  @MAct(actName=setDataFieldValues, attribNames = {"invoice"}, endStates = {Created}),
}),
/* 15 */    
@ANode(label="15:CustOrder", zone="11:AcceptPayment",refCls=CustOrder.class,serviceCls=DataController.class,
outNodes= { "13:CompleteOrder" },
actSeq={
  @MAct(actName=filterInput, filterType=FilterCustOrderFromPayment.class),
  @MAct(actName=showObject, endStates = {Updated}),
  }),
})
/**END: activity graph configuration */
@DClass(serialisable=false, singleton=true)
public class HandleOrder {
  @DAttr(name = "id", id = true, auto = true, type = Type.Integer, length = 5, 
      optional = false, mutable = false)
  private int id;
  private static int idCounter = 0;

  // order 
  @DAttr(name="orders", type=Type.Collection,filter=@Select(clazz=CustOrder.class),serialisable=false)
  @DAssoc(ascName="create-order",role="mgmt",
    ascType=AssocType.One2Many,endType=AssocEndType.One,
    associate=@Associate(
        type=CustOrder.class,cardMin=0,cardMax=DCSLConstants.CARD_MORE,
        updateLink=false
     ))
  private Collection<CustOrder> orders;

  // fill order
  @DAttr(name="fillOrders", type=Type.Collection,filter=@Select(clazz=FillOrder.class),serialisable=false)
  @DAssoc(ascName="fill-order",role="mgmt",
    ascType=AssocType.One2Many,endType=AssocEndType.One,
    associate=@Associate(
        type=FillOrder.class,cardMin=0,cardMax=DCSLConstants.CARD_MORE,
        updateLink=false
    ))
  private Collection<FillOrder> fillOrders;
  
  // not used at the moment
  public HandleOrder(Integer id) {
    this.id = nextID(id);
  }

  // for use by object form
  public HandleOrder() {
    this(null);
  }

  public int getId() {
    return id;
  }

  private static int nextID(Integer currID) {
    if (currID == null) { // generate one
      idCounter++;
      return idCounter;
    } else { // update
      int num;
      num = currID.intValue();
      
      if (num > idCounter) {
        idCounter=num;
      }   
      return currID;
    }
  }
}
