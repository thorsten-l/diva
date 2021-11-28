package sonia.webapp.diva.db;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@Repository
public interface DgcvRequestLogRepository extends
  PagingAndSortingRepository<DgcvRequestLog, String>
{
}
