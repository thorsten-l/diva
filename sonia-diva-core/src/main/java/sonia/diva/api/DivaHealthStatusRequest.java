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
public class DivaHealthStatusRequest
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
  private String certificateMediaType;
  
  @Getter
  @Setter
  @ToString.Exclude
  private byte[] certificateMediaData;
  
}
