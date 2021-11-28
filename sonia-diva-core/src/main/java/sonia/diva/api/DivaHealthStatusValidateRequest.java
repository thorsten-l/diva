package sonia.diva.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class DivaHealthStatusValidateRequest
{
  @Getter
  @Setter
  private String apiToken;

  @Getter
  @Setter
  private String organization;
  
  @Getter
  @Setter
  private String organizationUnit;
  
  @Getter
  @Setter
  private String id;
  
  @Getter
  @Setter
  private String validatorUid;  // employeeType p,m
  
  @Getter
  @Setter
  private String validatorName;
  
  @Getter
  @Setter
  private String validationStatus;
}
