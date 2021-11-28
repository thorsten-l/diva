package sonia.diva.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@ToString
@JsonInclude(Include.NON_NULL)
public class DivaHealthStatusListEntry
{
  public DivaHealthStatusListEntry()
  {
  }

  @Getter
  @Setter
  private String id;

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
  private Date dateOfObservation;

  @Getter
  @Setter
  private String validatorUid;  // employeeType p,m

  @Getter
  @Setter
  private String validatorName;

  @Getter
  @Setter
  private Date dateOfValidation;

  @Getter
  @Setter
  private String validationStatus;

  @Getter
  @Setter
  private String certificateMediaType;
}
