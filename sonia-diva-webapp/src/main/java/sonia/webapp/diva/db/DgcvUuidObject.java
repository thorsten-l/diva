package sonia.webapp.diva.db;

//~--- JDK imports ------------------------------------------------------------
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@MappedSuperclass
public class DgcvUuidObject extends DgcvObject
{

  /**
   * Field description
   */
  private static final long serialVersionUID = 5770231001269848079l;

  //~--- constructors ---------------------------------------------------------
  /**
   * Constructs ...
   *
   */
  public DgcvUuidObject()
  {
  }

  public DgcvUuidObject(String createdBy, boolean updateable, boolean removeable)
  {
    super(createdBy, updateable, removeable);
    this.id = UUID.randomUUID().toString();
  }

  //~--- methods --------------------------------------------------------------
  @Override
  public boolean equals(Object obj)
  {
    boolean same = false;

    if (this == obj)
    {
      return true;
    }

    if ((obj != null) && (obj instanceof DgcvUuidObject))
    {
      same = this.getId().compareTo(((DgcvUuidObject) obj).getId()) == 0;
    }

    return same;
  }

  @Override
  public int hashCode()
  {
    int hash = 5;

    hash = 37 * hash + Objects.hashCode(this.id);

    return hash;
  }

  @Id
  @Column(length = 36)
  @Setter
  @Getter
  private String id;
}
