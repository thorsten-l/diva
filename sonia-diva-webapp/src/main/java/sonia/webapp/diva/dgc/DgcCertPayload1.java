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
public class DgcCertPayload1
{
  @Getter
  @JsonProperty("v")
  private DgcVaccine[] vaccine;
  
  @Getter
  @JsonProperty("dob")
  private Date dayOfBirth;

  @Getter
  @JsonProperty("nam")
  private DgcName name;
  
  @Getter
  @JsonProperty("ver")
  private String version;
}
