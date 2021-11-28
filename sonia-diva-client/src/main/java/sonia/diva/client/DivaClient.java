package sonia.diva.client;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sonia.diva.api.DivaHealthStatusListRequest;
import sonia.diva.api.DivaHealthStatusListResponse;
import sonia.diva.api.DivaHealthStatusRequest;
import sonia.diva.api.DivaHealthStatusValidateRequest;
import sonia.diva.api.DivaMediaDataRequest;
import sonia.diva.api.DivaMediaDataResponse;
import sonia.diva.api.DivaResponse;
import sonia.diva.api.DivaScannerRequest;
import sonia.diva.api.DivaScannerResponse;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
public class DivaClient
{
  private final static Logger LOGGER = LoggerFactory.getLogger(DivaClient.class.
    getName());

  private final WebTarget target;

  private final String authToken;

  private final String organization;

  public DivaClient(WebTarget target, String authToken, String organization)
  {
    this.target = target;
    this.authToken = authToken;
    this.organization = organization;
  }

  public DivaScannerResponse validateQrCore(DivaScannerRequest request)
  {
    request.setApiToken(authToken);

    // LOGGER.debug(request.toString());
    DivaScannerResponse result = target.path("scanner").request(
      MediaType.APPLICATION_JSON).post(Entity.
        entity(request, MediaType.APPLICATION_JSON), DivaScannerResponse.class);

    // LOGGER.debug(result.toString());
    return result;
  }

  public DivaResponse persistHealthStatus(
    String organizationUnit,
    String healthStatus,
    String subjectUid,
    String subjectName,
    String observerUid,
    String observerName,
    String certificateMediaType,
    byte[] certificateMediaData
  )
  {
    DivaHealthStatusRequest request = new DivaHealthStatusRequest();
    request.setApiToken(authToken);
    request.setOrganization(organization);
    request.setOrganizationUnit(organizationUnit);

    request.setHealthStatus(healthStatus);
    request.setSubjectUid(subjectUid);
    request.setSubjectName(subjectName);
    request.setObserverUid(observerUid);
    request.setObserverName(observerName);
    request.setCertificateMediaType(certificateMediaType);
    request.setCertificateMediaData(certificateMediaData);

    // LOGGER.debug(request.toString());
    DivaResponse result = target.path("healthstatus").request(
      MediaType.APPLICATION_JSON).post(Entity.
        entity(request, MediaType.APPLICATION_JSON), DivaResponse.class);

    LOGGER.debug(result.toString());
    return result;
  }

  public DivaHealthStatusListResponse listHealthStatus(String organizationUnit)
  {
    DivaHealthStatusListRequest request = new DivaHealthStatusListRequest();
    request.setApiToken(authToken);
    request.setOrganization(organization);
    request.setOrganizationUnit(organizationUnit);

    // LOGGER.debug(request.toString());
    DivaHealthStatusListResponse result = target.path("healthstatuslist").
      request(MediaType.APPLICATION_JSON).post(Entity.
      entity(request, MediaType.APPLICATION_JSON),
      DivaHealthStatusListResponse.class);

    // LOGGER.debug(result.toString());
    return result;
  }

  public DivaResponse validateHealthStatus(
    String organizationUnit,
    String statusUuid,
    String validatorUid,
    String validatorName,
    String validationStatus
  )
  {
    DivaHealthStatusValidateRequest request = new DivaHealthStatusValidateRequest();

    request.setApiToken(authToken);
    request.setOrganization(organization);
    request.setOrganizationUnit(organizationUnit);
    request.setId(statusUuid);
    request.setValidatorUid(validatorUid);
    request.setValidatorName(validatorName);
    request.setValidationStatus(validationStatus);

    // LOGGER.debug(request.toString());
    DivaResponse result = target.path("healthstatusvalidate").
      request(MediaType.APPLICATION_JSON).post(Entity.
      entity(request, MediaType.APPLICATION_JSON),
      DivaResponse.class);

    LOGGER.debug(result.toString());
    return result;
  }

  public DivaMediaDataResponse getMediaData(
    String organizationUnit,
    String mediaUuid
  )
  {
    DivaMediaDataRequest request = new DivaMediaDataRequest();
    request.setApiToken(authToken);
    request.setOrganization(organization);
    request.setOrganizationUnit(organizationUnit);
    request.setId(mediaUuid);

    // LOGGER.debug(request.toString());
    DivaMediaDataResponse result = target.path("mediadata").
      request(MediaType.APPLICATION_JSON).post(Entity.
      entity(request, MediaType.APPLICATION_JSON),
      DivaMediaDataResponse.class);

    LOGGER.debug(result.toString());
    return result;
  }

}
