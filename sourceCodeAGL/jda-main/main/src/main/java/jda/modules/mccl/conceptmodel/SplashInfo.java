package jda.modules.mccl.conceptmodel;

import java.util.Calendar;

import javax.swing.ImageIcon;

import jda.modules.common.exceptions.ConstraintViolationException;
import jda.modules.common.types.Tuple;
import jda.modules.dcsl.syntax.DAssoc;
import jda.modules.dcsl.syntax.DAttr;
import jda.modules.dcsl.syntax.DCSLConstants;
import jda.modules.dcsl.syntax.DClass;
import jda.modules.dcsl.syntax.DOpt;
import jda.modules.dcsl.syntax.DAssoc.AssocEndType;
import jda.modules.dcsl.syntax.DAssoc.AssocType;
import jda.modules.dcsl.syntax.DAssoc.Associate;
import jda.modules.dcsl.syntax.DAttr.Type;
import jda.modules.mccl.conceptmodel.Configuration.Organisation;

/**
 * @overview
 *  Represents the data needed for the splash screen module.
 *  
 * @author dmle
 */
@DClass(schema=DCSLConstants.CONFIG_SCHEMA)
public class SplashInfo {
  @DAttr(name = "id", id = true, auto = true, type = Type.Integer, length = 5, 
      optional = false, mutable = false)
  private int id;
  private static int idCounter = 0;
  
  /** a non-serialisable domain attribute to implement the dependency association with Configuration */
  @DAttr(name="config",type=Type.Domain,serialisable=false)
  @DAssoc(ascName="config-has-splashinfo",role="splashinfo",
      ascType=AssocType.One2One,endType=AssocEndType.One,
      associate=@Associate(type=Configuration.class,cardMin=1,cardMax=1,
      determinant=true 
      ))  
  private Configuration config;
  
  // derived attributes that provide data for the splash screen
  @DAttr(name="appLogo",type=Type.Image,mutable=false,serialisable=false)
  private ImageIcon appLogo;
  
  @DAttr(name="orgInfo",type=Type.String,mutable=false,length=255,serialisable=false)
  private String orgInfo;
  
  @DAttr(name="copyrightInfo",type=Type.String,mutable=false,length=255,serialisable=false)
  private String copyrightInfo;
  
  public SplashInfo(Integer id, Configuration config) {
    this.id = nextID(id);
    this.config = config;
  }

  public SplashInfo(Integer id) {
    this(id, null);
  }
  
  public SplashInfo(Configuration config) {
    this(null, config);
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
  
  public Configuration getConfig() {
    return config;
  }

  public void setConfig(Configuration config) {
    this.config = config;
  }

  public int getId() {
    return id;
  }

  public ImageIcon getAppLogo() {
    if (appLogo == null)
      appLogo = config.getAppLogo();
    
    return appLogo;
  }

  public String getOrgInfo() {
    if (orgInfo == null) {
      Organisation org = config.getOrganisation(); 
      orgInfo = org.getName() + ", <br>" + org.getContactDetails();
    }
    return orgInfo;
  }

  public String getCopyrightInfo() {
    if (copyrightInfo == null) {
      Calendar cal = Calendar.getInstance();
      int currYear = cal.get(Calendar.YEAR);
      Company company = config.getCompany();
      cal.setTime(company.getStartDate());
      int startYear = cal.get(Calendar.YEAR);
      
      copyrightInfo = '\u00A9'+ " " + company.getCompanyName() + " " + startYear + "-" + currYear;   
    }
    return copyrightInfo;
  }

  @Override
  public String toString() {
    return "SplashInfo (" + id + ", " + 
        ((config != null) ? getOrgInfo() + ", " + getCopyrightInfo() : "null, null") + 
         ")";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SplashInfo other = (SplashInfo) obj;
    if (id != other.id)
      return false;
    return true;
  }

  /**
   * @requires 
   *  minVal != null /\ maxVal != null
   * @effects 
   *  update the auto-generated value of attribute <tt>attrib</tt>, specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
   */
  @DOpt(type=DOpt.Type.AutoAttributeValueSynchroniser)
  public static void updateAutoGeneratedValue(
      DAttr attrib,
      Tuple derivingValue, 
      Object minVal, 
      Object maxVal) throws ConstraintViolationException {    
    if (minVal != null && maxVal != null) {
      // check the right attribute
      if (attrib.name().equals("id")) {
        int maxIdVal = (Integer) maxVal;
        if (maxIdVal > idCounter)  
          idCounter = maxIdVal;
      } 
      // TODO add support for other attributes here 
    }
  }
}