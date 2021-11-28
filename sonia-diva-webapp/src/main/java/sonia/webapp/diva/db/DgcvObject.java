package sonia.webapp.diva.db;

//~--- non-JDK imports --------------------------------------------------------
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------
import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@MappedSuperclass
public class DgcvObject implements Serializable
{
  private static final long serialVersionUID = 1575497541642600225l;

  /**
   * Field description
   */
  private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(
    DgcvObject.class.getName());

  //~--- constructors ---------------------------------------------------------
  /**
   * Constructs ...
   *
   */
  public DgcvObject()
  {
  }

  public DgcvObject(String createdBy)
  {
    this.createdBy = this.modifiedBy = createdBy;
  }

  public DgcvObject(String createdBy, boolean updateable, boolean removeable)
  {
    this(createdBy);
    this.updateable = updateable;
    this.removeable = removeable;
  }

  @PrePersist
  public void prePersist()
  {
    LOGGER.debug("prePersist " + this.getClass().getCanonicalName());
    this.createTimestamp = this.modifyTimestamp = new Date();
  }

  /**
   * Method description
   *
   */
  @PreRemove
  public void preRemove()
  {
    LOGGER.debug("preRemove " + this.getClass().getCanonicalName());
  }

  /**
   * Method description
   *
   */
  @PreUpdate
  public void preUpdate()
  {
    LOGGER.debug("preUpdate " + this.getClass().getCanonicalName());
    this.modifyTimestamp = new Date();
  }

  //~--- fields ---------------------------------------------------------------
  /**
   * Field description
   */
  @Getter
  @Setter
  private boolean updateable;

  @Getter
  @Setter
  private boolean removeable;

  /**
   * Field description
   */
  @Getter
  private String createdBy;

  /**
   * Field description
   */
  @Getter
  @Setter
  private String modifiedBy;

  /**
   * Field description
   */
  @Getter
  @JsonIgnore
  @Temporal(TemporalType.TIMESTAMP)
  protected Date createTimestamp;

  /**
   * Field description
   */
  @Getter
  @JsonIgnore
  @Temporal(TemporalType.TIMESTAMP)
  protected Date modifyTimestamp;
}
