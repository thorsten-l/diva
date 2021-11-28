package sonia.webapp.diva.dgc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class DgcVaccine
{
  @Getter
  @JsonProperty("ci")
  private String uniqueCertificateIdentifier;
  
  @Getter
  @JsonProperty("co")
  private String countryOfVaccination;

  @Getter
  @JsonProperty("dn")
  private int doseNumber;
  
  @Getter
  @JsonProperty("dt")
  private Date dateOfVaccination;
  
  @Getter
  @JsonProperty("is")
  private String certificateIssuer;
  
  @Getter
  @JsonProperty("mp")
  private String medicalProduct;
  
  @Getter
  @JsonProperty("ma")
  private String marketingAuthorizationHolder;
  
  @Getter
  @JsonProperty("sd")
  private int totalSeriesOfDoses;

  @Getter
  @JsonProperty("tg")
  private String diseaseTarget;
  
  @Getter
  @JsonProperty("vp")
  private String vaccineProphylaxis;   
}
