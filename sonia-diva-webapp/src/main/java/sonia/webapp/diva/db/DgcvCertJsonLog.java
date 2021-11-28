package sonia.webapp.diva.db;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@Entity
public class DgcvCertJsonLog extends DgcvUuidObject
{
  private static final long serialVersionUID = 2377357483632195188l;

  public DgcvCertJsonLog()
  {
  }

  public DgcvCertJsonLog(
    String createdBy, String readerUuid, String uniqueCertificateIdentifier, String surname, 
    String givenname, Date dayOfBirth, String jsonPayload,
    boolean certValid
  )
  {
    super(createdBy, false, false);
    this.readerUuid = readerUuid;
    this.uniqueCertificateIdentifier = uniqueCertificateIdentifier;
    this.surname = surname;
    this.givenname = givenname;
    this.jsonPayload = jsonPayload;
    this.certValid = certValid;
    this.dayOfBirth = dayOfBirth;
  }

  @Getter
  private String readerUuid;

  @Getter
  private String uniqueCertificateIdentifier;

  @Getter
  private String surname;

  @Getter
  private String givenname;

  @Getter
  @Temporal(TemporalType.DATE)
  private Date dayOfBirth;

  @Getter
  private boolean certValid;

  @Getter
  @Column(length=4096)
  private String jsonPayload;
}
