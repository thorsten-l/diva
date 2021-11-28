package sonia.webapp.diva.db;

//~--- JDK imports ------------------------------------------------------------
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import sonia.webapp.diva.dgc.DgcCertPayload1;
import sonia.webapp.diva.dgc.DgcCommonPayload;
import sonia.webapp.diva.dgc.DgcName;
import sonia.webapp.diva.dgc.DgcVaccine;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@Entity
public class DgcvCertificate extends DgcvObject
{

  /**
   * Field description
   */
  private static final long serialVersionUID = 5770231001269848079l;

  //~--- constructors ---------------------------------------------------------
  /**
   * Constructs ...
   *
   */
  public DgcvCertificate()
  {
  }

  public DgcvCertificate(String createdBy, DgcCommonPayload payload,
    String uid, String organization, String organizationUnit
  )
  {
    super(createdBy, false, false);

    this.uid =  uid;
    this.organization = organization;
    this.organizationUnit = organizationUnit;
    
    DgcCertPayload1 payload1 = payload.getPayload().getPayload1();
    DgcVaccine vaccine = payload1.getVaccine()[0];
    DgcName name = payload1.getName();

    this.issuer = payload.getIssuer();
    this.issuingTimestamp = new Date(payload.getIssuingTimestamp() * 1000);
    this.expiringTimestamp = new Date(payload.getExpiringTimestamp() * 1000);

    this.dayOfBirth = payload1.getDayOfBirth();
    this.certVersion = payload1.getVersion();

    // Id
    this.uniqueCertificateIdentifier = vaccine.getUniqueCertificateIdentifier();

    this.countryOfVaccination = vaccine.getCountryOfVaccination();
    this.doseNumber = vaccine.getDoseNumber();
    this.dateOfVaccination = vaccine.getDateOfVaccination();
    this.certificateIssuer = vaccine.getCertificateIssuer();
    this.medicalProduct = vaccine.getMedicalProduct();
    this.marketingAuthorizationHolder = vaccine.
      getMarketingAuthorizationHolder();
    this.totalSeriesOfDoses = vaccine.getTotalSeriesOfDoses();
    this.diseaseTarget = vaccine.getDiseaseTarget();
    this.vaccineProphylaxis = vaccine.getVaccineProphylaxis();

    this.surname = name.getSurname();
    this.givenname = name.getGivenname();
    this.normSurname = name.getNormSurname();
    this.normGivenname = name.getNormGivenname();
  }

  //~--- methods --------------------------------------------------------------
  @Override
  public boolean equals(Object obj)
  {
    boolean same = false;

    if (this == obj)
    {
      return true;
    }

    if ((obj != null) && (obj instanceof DgcvCertificate))
    {
      same = this.getUniqueCertificateIdentifier().compareTo(
        ((DgcvCertificate) obj).
          getUniqueCertificateIdentifier()) == 0;
    }

    return same;
  }

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 37 * hash + Objects.hashCode(this.uniqueCertificateIdentifier);
    return hash;
  }

  @Getter
  private String issuer;

  @Getter
  @Temporal(TemporalType.DATE)
  private Date issuingTimestamp;

  @Getter
  @Temporal(TemporalType.DATE)
  private Date expiringTimestamp;

  @Getter
  @Temporal(TemporalType.DATE)
  private Date dayOfBirth;

  @Getter
  private String certVersion;

  @Id
  @Getter
  private String uniqueCertificateIdentifier;

  @Getter
  private String countryOfVaccination;

  @Getter
  private int doseNumber;

  @Getter
  @Temporal(TemporalType.DATE)
  private Date dateOfVaccination;

  @Getter
  private String certificateIssuer;

  @Getter
  private String medicalProduct;

  @Getter
  private String marketingAuthorizationHolder;

  @Getter
  private int totalSeriesOfDoses;

  @Getter
  private String diseaseTarget;

  @Getter
  private String vaccineProphylaxis;

  @Getter
  private String surname;

  @Getter
  private String givenname;

  @Getter
  private String normSurname;

  @Getter
  private String normGivenname;
  
  @Getter
  private String uid;
  
  @Getter
  private String organization;
  
  @Getter
  private String organizationUnit;
}
