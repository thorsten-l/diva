package sonia.webapp.diva.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.Getter;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@Entity
public class DgcvRequestLog extends DgcvUuidObject
{
  private static final long serialVersionUID = 2377357483632195188l;

  public DgcvRequestLog()
  {
  }

  public DgcvRequestLog(
    String createdBy, String readerUuid, String action, String payload
  )
  {
    super(createdBy, false, false );
    this.readerUuid = readerUuid;
    this.action = action;
    this.payload = payload;
  }

  @Getter
  private String readerUuid;

  @Getter
  private String action;

  @Getter
  @Column(length=4096)
  private String payload;
}
