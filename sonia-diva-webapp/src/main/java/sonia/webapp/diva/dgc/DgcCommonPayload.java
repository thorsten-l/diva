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
public class DgcCommonPayload
{
  /*
  Claim Key   Name    Description
  1           iss     Issuer of the DGC
  6           iat     Issuing Date of the DGC
  4           exp     Expiring Date of the DGC
  -260        hcert   Payload of the DGC (Vac,Tst,Rec)
  */
  
  @Getter
  @JsonProperty("1")
  private String issuer;

  @Getter
  @JsonProperty("6")
  private long issuingTimestamp;

  @Getter
  @JsonProperty("4")
  private long expiringTimestamp;
  
  @Getter
  @JsonProperty("-260")
  private DgcCertPayload payload;
}
