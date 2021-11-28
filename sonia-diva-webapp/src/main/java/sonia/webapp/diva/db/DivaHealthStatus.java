package sonia.webapp.diva.db;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import sonia.diva.api.DivaHealthStatusRequest;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@Entity
public class DivaHealthStatus extends DgcvUuidObject
{
  private static final long serialVersionUID = 2377357483632195188l;

  public DivaHealthStatus()
  {
  }

  public DivaHealthStatus(DivaHealthStatusRequest request)
  {
    super(request.getSubjectUid(), true, false);
    this.healthStatus = request.getHealthStatus();
    this.organization = request.getOrganization();
    this.organizationUnit = request.getOrganizationUnit();
    this.subjectUid = request.getSubjectUid();
    this.subjectName = request.getSubjectName();
    this.observerUid = request.getObserverUid();
    this.observerName = request.getObserverName();
    this.dateOfObservation = new Date();
    this.certificateMediaType = request.getCertificateMediaType();
  }

  @Getter
  @Setter
  private String organization;
  
  @Getter
  @Setter
  private String organizationUnit;

  @Getter
  @Setter
  private String healthStatus;
  
  @Getter
  @Setter
  private String subjectUid;
  
  @Getter
  @Setter
  private String subjectName;
  
  @Getter
  @Setter
  private String observerUid;  // employeeType p,m
  
  @Getter
  @Setter
  private String observerName;

  @Getter
  @Setter
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateOfObservation;

  @Getter
  @Setter
  private String validatorUid;  // employeeType p,m
  
  @Getter
  @Setter
  private String validatorName;

  @Getter
  @Setter
  @Temporal(TemporalType.TIMESTAMP)
  private Date dateOfValidation;

  @Getter
  @Setter
  private String validationStatus;
  
  @Getter
  @Setter
  private String certificateMediaType;

}
