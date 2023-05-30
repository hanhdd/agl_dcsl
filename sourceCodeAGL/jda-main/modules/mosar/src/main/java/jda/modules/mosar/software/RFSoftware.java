package jda.modules.mosar.software;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import jda.modules.mosar.config.RFSGenConfig;
import jda.modules.mosar.software.backend.BEApp;
import jda.modules.mosar.software.backend.BESoftware;
import jda.modules.mosar.software.frontend.FEApp;
import jda.modules.mosar.software.frontend.FESoftware;
import jda.modules.mosar.utils.RFSGenTk;

/**
 * @overview 
 *  Automatically generates an RFS with front-end and back-end modules.
 *  
 * @author Duc Minh Le (ducmle)
 *
 * @version 5.4.1
 */
public class RFSoftware {
  
  private Class<?> scc;
  private RFSGenConfig cfg;
  
  private FESoftware feSw;
  private BESoftware beSw;

  private static Logger logger = (Logger) 
      LoggerFactory.getLogger("module.restfstool."+RFSoftware.class.getSimpleName());
  
  static {
    logger.setLevel(Level.INFO);
  }
  /**
   * @effects 
   *  Initialises this with a configuration defined in <code>scc</code>
   */
  public RFSoftware(Class<?> scc) {
    this.scc = scc;
  }
  
  /**
   * @effects 
   *  initialises resources necessary for the software 
   */
  public RFSoftware init() {
    logger.info("Initiating...");
    
    cfg = RFSGenTk.parseRFSGenConfig(scc);
    
    // initialisation
    RFSGenTk.init(cfg.getDomainModel());
    
    return this;
  }
  
  /**
   * @effects 
   *  executes the generator logic, which consists in 2 main steps: 
   *  (1) generates the front end
   *  (2) generates the back end
   */
  public RFSoftware generate() {
    logger.info("Generating...");

    // generate front-end
    if (cfg.getStackSpec().includesFE()) {
      feSw = new FESoftware(cfg)
      .init()
      .generate();
    }
    
    // generate back-end
    if (cfg.getStackSpec().includesBE()) {
      beSw = new BESoftware(cfg)
        .init()
        .generate();
    }
    
    return this;
  }
  
  /**
   * @effects 
   *   Runs this software based on {@link #cfg}. 
   *   This could be the software that has just been generated by {@link #generate()}. 
   */
  public RFSoftware run() {
    logger.info("Running...");

    if (cfg.getStackSpec().includesFE()) {
      Class<? extends FEApp> appCls = cfg.getFeAppClass();

      if (appCls != null) {
        // run front end
        if (feSw != null) {
          feSw.run();
        } else {
          feSw = new FESoftware(cfg)
              .init()
              .run();
        }
      }
    }
    
    if (cfg.getStackSpec().includesBE()) {
      // run back end
      Class<? extends BEApp> appCls = cfg.getBeAppClass();
      
      if (appCls != null) {
        if (beSw != null) {  // if BE was generated
          beSw.run();
        } else {  // BE already generated
          beSw = new BESoftware(cfg)
            .init()
            .runLater();
        }
      }
    }
    
    return this;
  }
  
//  @Deprecated
//  @Override
//  public Object exec(Object... args) throws NotPossibleException {
//    String frontEndOutputPath = (String) args[0];
//    Class<?>[] model = (Class<?>[]) args[1];
//    Class<?>[] auxModel = (Class<?>[]) args[2];
//    Class<?> scc = (Class) args[3];
//    Class<?> mainMCC = (Class<?>) args[4];
//    Class<?>[] funcMCCs = (Class<?>[]) args[5];
//    //
//    String backendTargetPackage = (String) args[6];
//    String backendOutputPath = (String) args[7];
//    Consumer<List<Class>> runCallBack = (Consumer<List<Class>> ) args[8];
//    
////    FrontendGenerator.setupAndGen();
////    BackendApp.setupAndRun();
//    
//    run(frontEndOutputPath, model, auxModel, scc, mainMCC, funcMCCs, 
//        backendTargetPackage, backendOutputPath, runCallBack
//        );
//    return null;
//  }

//  /**
//   * @effects 
//   *  executes the generator logic, which consists in 2 main steps: 
//   *  (1) generates the front end
//   *  (2) generates the back end
//   * @deprecated
//   */
//  public void run(String frontEndOutputPath, Class<?>[] model, Class<?>[] auxModel, 
//      Class<?> scc,
//      Class<?> mainMCC, Class<?>[] funcMCCs, String backendTargetPackage,
//      String backendOutputPath, Consumer<List<Class>> runCallBack) {
//    // initialisation
//    
//    init(model, auxModel);
//    
//    // run front-end
//    new FEGen().run(frontEndOutputPath, model, scc, mainMCC, funcMCCs);
//    
//    // run back-end
//    new BEGen().run(backendTargetPackage, backendOutputPath, model,
//        runCallBack);    
//  }

//  /**
//   * @effects 
//   * @deprecated
//   */
//  private void init(Class<?>[] model, Class<?>[] auxModel) {
//    DomainTypeRegistry regist = DomainTypeRegistry.getInstance();
//    regist.addDomainTypes(model);
//    for (Class<?> other : auxModel) {
//      regist.addDomainType(other);
//    }
//  }
}
