package sonia.diva.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@ToString
@JsonInclude(Include.NON_NULL)
public class DivaScannerResponse
{
  public final static int OK = 0;

  public final static int ERROR = 1;

  public final static int INVALID_ACCESS = 2;

  public DivaScannerResponse(int code, String message )
  {
    this.code = code;
    this.message = message;
  }

  @Getter
  private final int code;

  @Getter
  private final String message;
  
  @Setter
  @Getter
  private boolean valid;
  
  @Setter
  @Getter
  private String until;
  
  @Setter
  @Getter
  private String dateOfVaccination;
  
  @Setter
  @Getter
  private String owner;  
}
