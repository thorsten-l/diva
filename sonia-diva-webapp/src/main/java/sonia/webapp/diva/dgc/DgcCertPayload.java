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
public class DgcCertPayload
{
  @Getter
  @JsonProperty("1")
  private DgcCertPayload1 payload1;
}
