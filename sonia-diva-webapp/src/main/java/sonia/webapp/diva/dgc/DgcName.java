package sonia.webapp.diva.dgc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class DgcName
{
  @Getter
  @JsonProperty("fn")
  private String surname;

  @Getter
  @JsonProperty("gn")
  private String givenname;
  
  @Getter
  @JsonProperty("fnt")
  private String normSurname;
  
  @Getter
  @JsonProperty("gnt")
  private String normGivenname;
}
