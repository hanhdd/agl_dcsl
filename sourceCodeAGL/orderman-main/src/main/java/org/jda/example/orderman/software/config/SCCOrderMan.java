package org.jda.example.orderman.software.config;


import org.jda.example.orderman.modules.customer.ModuleCustomer;
import org.jda.example.orderman.modules.delivery.ModuleCollectPayment;
import org.jda.example.orderman.modules.delivery.ModuleDelivery;
import org.jda.example.orderman.modules.fillorder.ModuleFillOrder;
import org.jda.example.orderman.modules.handleorder.ModuleHandleOrder;
import org.jda.example.orderman.modules.invoice.ModuleInvoice;
import org.jda.example.orderman.modules.order.ModuleCustOrder;
import org.jda.example.orderman.modules.order.ModuleOrderLine;
import org.jda.example.orderman.modules.payment.ModuleAcceptPayment;
import org.jda.example.orderman.modules.payment.ModulePayment;
import org.jda.example.orderman.modules.ship.ModuleShipOrder;
import org.jda.example.orderman.modules.ship.ModuleShipment;
import org.jda.example.orderman.software.ModuleMain;

import jda.modules.dodm.dom.DOM;
import jda.modules.dodm.dsm.DSM;
import jda.modules.dodm.osm.postgresql.PostgreSQLOSM;
import jda.modules.jdatool.DomainAppTool;
import jda.modules.mccl.conceptmodel.Configuration.Language;
import jda.modules.mccl.conceptmodel.dodm.OsmConfig.ConnectionType;
import jda.modules.sccl.syntax.DSDesc;
import jda.modules.sccl.syntax.OrgDesc;
import jda.modules.sccl.syntax.SecurityDesc;
import jda.modules.sccl.syntax.SysSetUpDesc;
import jda.modules.sccl.syntax.SystemDesc;
import jda.modules.setup.model.SetUpConfig;

/**
 * @overview 
 *  The system class of the CourseMan application that is used by {@link DomainAppTool}.
 *  
 * @author dmle
 * @version 1.3
 */
@SystemDesc(
    appName="CourseMan",
    splashScreenLogo="orderman.gif",
    language=Language.English,
    orgDesc=@OrgDesc(
        name="My Company",
        address="Hanoi", 
        logo="orderman.gif", 
        url="http://mycompany.com"
    ), 
    dsDesc=@DSDesc(
        type="postgresql", 
        dsUrl="//localhost:5432/orderman", 
        user="postgres",
        password="password",
        dsmType=DSM.class,
        domType=DOM.class,
        osmType=PostgreSQLOSM.class,
        connType=ConnectionType.Client
    ), 
    modules={         
        ModuleMain.class,  // main
        // data
        ModuleHandleOrder.class,
        ModuleCustOrder.class,
        ModuleFillOrder.class,
        ModuleOrderLine.class,
        ModuleCustomer.class,
        ModuleDelivery.class,
        ModuleCollectPayment.class,
        ModuleShipOrder.class,
        ModuleInvoice.class,
        ModuleShipment.class,
        ModuleAcceptPayment.class,
        ModulePayment.class,
    },
    sysModules={}, 
    setUpDesc=@SysSetUpDesc(
      setUpConfigType=SetUpConfig.class
    )
    ,securityDesc=@SecurityDesc(
      isEnabled=false // true: to initialise security schema
    )
)
public class SCCOrderMan {
  // empty
}
