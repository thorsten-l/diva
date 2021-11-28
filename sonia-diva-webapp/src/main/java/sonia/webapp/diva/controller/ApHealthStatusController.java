package sonia.webapp.diva.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sonia.diva.api.DivaHealthStatusRequest;
import sonia.diva.api.DivaResponse;
import sonia.webapp.diva.Config;
import sonia.webapp.diva.db.DivaHealthStatusRepository;
import com.google.common.base.Strings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import sonia.diva.api.DivaHealthStatusListEntry;
import sonia.diva.api.DivaHealthStatusListRequest;
import sonia.diva.api.DivaHealthStatusListResponse;
import sonia.diva.api.DivaHealthStatusValidateRequest;
import sonia.diva.api.DivaMediaDataRequest;
import sonia.diva.api.DivaMediaDataResponse;
import sonia.webapp.diva.crypto.AES256;
import sonia.webapp.diva.db.DivaHealthStatus;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@RestController
public class ApHealthStatusController
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    ApHealthStatusController.class.getName());

  @Autowired
  private DivaHealthStatusRepository divaHealthStatusRepository;

  private final static Config CONFIG = Config.getInstance();

  @PostMapping(path = "/api/healthstatus",
               consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DivaResponse apiHealthStatusPOST(
    @RequestBody DivaHealthStatusRequest request)
  {
    LOGGER.debug("apiHealthStatusPOST: {}", request);

    DivaResponse response = new DivaResponse(DivaResponse.ERROR, "unknown");

    if (request != null)
    {
      if (CONFIG.getPortletApiToken().equals(request.getApiToken()))
      {
        if (Strings.isNullOrEmpty(request.getOrganization()) || Strings.
          isNullOrEmpty(request.getOrganizationUnit()))
        {
          response = new DivaResponse(DivaResponse.ERROR,
            "Organization and OragnizationUnit must not be null or empty");
        }
        else
        {
          DivaHealthStatus healthStatus = new DivaHealthStatus(request);
          divaHealthStatusRepository.save(healthStatus);

          if (!Strings.isNullOrEmpty(request.getCertificateMediaType())
            && request.getCertificateMediaData() != null)
          {
            LOGGER.debug("healthStatus uuid = {}", healthStatus.getId());
            StringBuffer mediaFileName = new StringBuffer(healthStatus.getId());
            mediaFileName.insert(6, File.separator);
            mediaFileName.insert(4, File.separator);
            mediaFileName.insert(2, File.separator);
            LOGGER.debug("mediaFileName = {}", mediaFileName.toString());
            try
            {
              String path = CONFIG.getMediaStorePath() + File.separator
                + mediaFileName.toString().substring(0, 8);

              LOGGER.debug("path = {}", path);
              new File(path).mkdirs();

              try (FileOutputStream output = new FileOutputStream(CONFIG.
                getMediaStorePath() + File.separator + mediaFileName.toString()))
              {
                AES256 cipher = new AES256(Config.getSECRET_KEY());
                output.write(cipher.encrypt(request.getCertificateMediaData()));
              }
            }
            catch (IOException ex)
            {
              LOGGER.error("Writing media data ", ex);
            }
          }
          response = new DivaResponse(DivaResponse.OK, "Health status saved");
        }
      }
      else
      {
        response = new DivaResponse(DivaResponse.ERROR, "Wrong API token");
      }
    }

    return response;
  }

  @PostMapping(path = "/api/healthstatuslist",
               consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DivaHealthStatusListResponse apiHealthStatusListPOST(
    @RequestBody DivaHealthStatusListRequest request)
  {
    LOGGER.debug("apiHealthStatusPOST: {}", request);

    DivaHealthStatusListResponse response = new DivaHealthStatusListResponse(
      DivaResponse.ERROR, "unknown");

    if (request != null)
    {
      if (CONFIG.getPortletApiToken().equals(request.getApiToken()))
      {
        if (Strings.isNullOrEmpty(request.getOrganization()) || Strings.
          isNullOrEmpty(request.getOrganizationUnit()))
        {
          response = new DivaHealthStatusListResponse(DivaResponse.ERROR,
            "Organization and OragnizationUnit must not be null or empty");
        }
        else
        {
          ArrayList<DivaHealthStatusListEntry> list = new ArrayList<>();

          for (DivaHealthStatus status : divaHealthStatusRepository.
            findByOrganizationAndOrganizationUnitOrderByDateOfObservationDesc(
              request.getOrganization(), request.getOrganizationUnit()))
          {
            DivaHealthStatusListEntry entry = new DivaHealthStatusListEntry();
            entry.setId(status.getId());
            entry.setCertificateMediaType(status.getCertificateMediaType());
            entry.setDateOfObservation(status.getDateOfObservation());
            entry.setDateOfValidation(status.getDateOfValidation());
            entry.setHealthStatus(status.getHealthStatus());
            entry.setObserverName(status.getObserverName());
            entry.setObserverUid(status.getObserverUid());
            entry.setOrganization(status.getOrganization());
            entry.setOrganizationUnit(status.getOrganizationUnit());
            entry.setSubjectName(status.getSubjectName());
            entry.setSubjectUid(status.getSubjectUid());
            entry.setValidationStatus(status.getValidationStatus());
            entry.setValidatorName(status.getValidatorName());
            entry.setValidatorUid(status.getValidatorUid());
            list.add(entry);
          }

          if (list.size() > 0)
          {
            response = new DivaHealthStatusListResponse(DivaResponse.OK,
              "Entry List");
            response.setEntries(list);
          }
        }
      }
      else
      {
        response = new DivaHealthStatusListResponse(DivaResponse.ERROR,
          "Wrong API token");
      }
    }

    return response;
  }

  @PostMapping(path = "/api/healthstatusvalidate",
               consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DivaResponse apiHealthStatusValidatePOST(
    @RequestBody DivaHealthStatusValidateRequest request)
  {
    LOGGER.debug("apiHealthStatusValidatePOST: {}", request);

    DivaResponse response = new DivaResponse(DivaResponse.ERROR, "unknown");

    if (request != null)
    {
      if (CONFIG.getPortletApiToken().equals(request.getApiToken()))
      {
        if (Strings.isNullOrEmpty(request.getOrganization()) || Strings.
          isNullOrEmpty(request.getOrganizationUnit()))
        {
          response = new DivaResponse(DivaResponse.ERROR,
            "Organization and OragnizationUnit must not be null or empty");
        }
        else
        {
          Optional<DivaHealthStatus> optional = divaHealthStatusRepository
            .findByIdAndOrganizationAndOrganizationUnit(
              request.getId(), request.getOrganization(),
              request.getOrganizationUnit());
          if (optional.isPresent())
          {
            DivaHealthStatus status = optional.get();
            status.setValidationStatus(request.getValidationStatus());
            status.setValidatorName(request.getValidatorName());
            status.setValidatorUid(request.getValidatorUid());
            status.setDateOfValidation(new Date());
            divaHealthStatusRepository.save(status);
            response = new DivaResponse(DivaResponse.OK, "Validation saved");
          }
          else
          {
            response = new DivaResponse(DivaResponse.ERROR,
              "Unknown health status id");
          }
        }
      }
      else
      {
        response = new DivaResponse(DivaResponse.ERROR, "Wrong API token");
      }
    }

    return response;
  }

  @PostMapping(path = "/api/mediadata",
               consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DivaMediaDataResponse apiMediaDataPOST(
    @RequestBody DivaMediaDataRequest request)
  {
    LOGGER.debug("apiMediaDataPOST: {}", request);

    DivaMediaDataResponse response = new DivaMediaDataResponse(
      DivaMediaDataResponse.ERROR, "unknown");

    if (request != null)
    {
      if (CONFIG.getPortletApiToken().equals(request.getApiToken()))
      {
        if (Strings.isNullOrEmpty(request.getOrganization()) || Strings.
          isNullOrEmpty(request.getOrganizationUnit()))
        {
          response = new DivaMediaDataResponse(DivaMediaDataResponse.ERROR,
            "Organization and OragnizationUnit must not be null or empty");
        }
        else
        {
          Optional<DivaHealthStatus> optional = divaHealthStatusRepository
            .findByIdAndOrganizationAndOrganizationUnit(
              request.getId(), request.getOrganization(),
              request.getOrganizationUnit());
          if (optional.isPresent())
          {
            DivaHealthStatus status = optional.get();

            StringBuffer mediaFileName = new StringBuffer(status.getId());
            mediaFileName.insert(6, File.separator);
            mediaFileName.insert(4, File.separator);
            mediaFileName.insert(2, File.separator);
            LOGGER.debug("mediaFileName = {}", mediaFileName.toString());

            try
            {
              File inputFile = new File(CONFIG.
                getMediaStorePath() + File.separator + mediaFileName.toString());
              byte[] content = new byte[(int) inputFile.length()];
              try (FileInputStream input = new FileInputStream(inputFile))
              {
                input.read(content);
              }

              response = new DivaMediaDataResponse(DivaResponse.OK, "media data");
              response.setMediaType(status.getCertificateMediaType());
              AES256 cipher = new AES256(Config.getSECRET_KEY());
              response.setMediaData(cipher.decrypt(content));
            }
            catch (IOException ex)
            {
              LOGGER.error("media data not found ", ex);
              response = new DivaMediaDataResponse(DivaResponse.ERROR,
                "media data not found");
            }
          }
          else
          {
            response = new DivaMediaDataResponse(DivaResponse.ERROR,
              "Unknown health status id");
          }
        }
      }
      else
      {
        response = new DivaMediaDataResponse(DivaMediaDataResponse.ERROR,
          "Wrong API token");
      }
    }

    return response;
  }

}
