package sonia.webapp.diva.db;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@Repository
public interface DivaHealthStatusRepository extends
  PagingAndSortingRepository<DivaHealthStatus, String>
{
  List<DivaHealthStatus> findByOrganizationAndOrganizationUnitOrderByDateOfObservationDesc(
    String organization, String organizationUnit);
  
  Optional<DivaHealthStatus> findByIdAndOrganizationAndOrganizationUnit(
    String id, String organization, String organizationUnit);
}
