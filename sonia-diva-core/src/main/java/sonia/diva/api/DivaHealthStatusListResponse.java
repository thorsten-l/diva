package sonia.diva.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@ToString
@JsonInclude(Include.NON_NULL)
public class DivaHealthStatusListResponse
{
  public final static int OK = 0;

  public final static int ERROR = 1;

  public final static int INVALID_ACCESS = 2;

  public DivaHealthStatusListResponse() {}
  
  public DivaHealthStatusListResponse(int code, String message )
  {
    this.code = code;
    this.message = message;
  }

  @Getter
  private int code;

  @Getter
  private String message;
  
  @Getter
  @Setter
  private List<DivaHealthStatusListEntry> entries;
}
