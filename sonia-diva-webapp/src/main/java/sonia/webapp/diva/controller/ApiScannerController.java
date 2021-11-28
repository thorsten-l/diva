package sonia.webapp.diva.controller;

import COSE.CoseException;
import COSE.Message;
import COSE.MessageTag;
import COSE.Sign1Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upokecenter.cbor.CBORObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import nl.minvws.encoding.Base45;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import static org.apache.commons.compress.utils.IOUtils.toByteArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sonia.webapp.diva.Config;
import sonia.diva.api.DivaScannerRequest;
import sonia.diva.api.DivaScannerResponse;
import sonia.webapp.diva.db.DgcvCertJsonLog;
import sonia.webapp.diva.db.DgcvCertJsonLogRepository;
import sonia.webapp.diva.db.DgcvCertificate;
import sonia.webapp.diva.db.DgcvCertificateRepository;
import sonia.webapp.diva.db.DgcvRequestLog;
import sonia.webapp.diva.db.DgcvRequestLogRepository;
import sonia.webapp.diva.dgc.DgcCommonPayload;
import sonia.webapp.diva.dgc.DgcName;
import sonia.webapp.diva.dgc.DgcVaccine;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@RestController
public class ApiScannerController
{

  private final static Logger LOGGER = LoggerFactory.getLogger(ApiScannerController.class.getName());

  private final static SimpleDateFormat DATE_FORMAT
    = new SimpleDateFormat("yyyy-MM-dd");

  private final static Config CONFIG = Config.getInstance();
  
  @Autowired
  private DgcvRequestLogRepository requestLogRepository;

  @Autowired
  private DgcvCertJsonLogRepository certJsonLogRepository;

  @Autowired
  private DgcvCertificateRepository certificateRepository;

  @PostMapping(path = "/api/scanner",
               consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DivaScannerResponse apiScannerPOST(
    @RequestBody DivaScannerRequest request)
  {
    ObjectMapper objectMapper = new ObjectMapper();

    LOGGER.debug("apiScannerPOST");
    LOGGER.trace("api request = " + request.toString());

    DivaScannerResponse response = new DivaScannerResponse(
      DivaScannerResponse.ERROR,
      "Fehler!");

    if (request != null && CONFIG.getApiToken().equals(request.getApiToken()))
    {
      if (request.getPayload() != null && request.getPayload().length() > 0)
      {
        requestLogRepository.save(new DgcvRequestLog(
          "reader", request.getReaderUuid(), request.getAction(),
          request.getPayload()));
      }

      if (request.getPayload() != null
        && request.getPayload().startsWith("HC1:")
        && "validate".equals(request.getAction()))
      {
        CompressorInputStream compressedStream;
        try
        {
          String base45String = request.getPayload().substring(4);
          byte[] zipByteArray = Base45.getDecoder().decode(base45String);

          compressedStream = new CompressorStreamFactory()
            .createCompressorInputStream(CompressorStreamFactory.DEFLATE,
              new ByteArrayInputStream(zipByteArray));
          byte[] cose = toByteArray(compressedStream);

          Sign1Message msg = (Sign1Message) Message.DecodeFromBytes(cose,
            MessageTag.Sign1);

          CBORObject cborObject = CBORObject.DecodeFromBytes(msg.GetContent());
          ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          cborObject.WriteJSONTo(byteArrayOutputStream);

          String jsonString = byteArrayOutputStream.toString("UTF-8");

          LOGGER.debug(jsonString);

          DgcCommonPayload dgcCommonPayload = objectMapper.readValue(jsonString,
            DgcCommonPayload.class);
          LOGGER.debug(dgcCommonPayload.toString());

          Date date = new Date(dgcCommonPayload.getExpiringTimestamp() * 1000);
          LOGGER.debug("technical expiration date = {}", DATE_FORMAT.
            format(date));

          DgcVaccine vaccine = dgcCommonPayload.getPayload().getPayload1().
            getVaccine()[0];

          boolean certValid = true;
          
          if (System.currentTimeMillis() > (dgcCommonPayload.getExpiringTimestamp()*1000)) {
            response = new DivaScannerResponse(DivaScannerResponse.OK,
              "Zertifikat abgelaufen");
            certValid = false;
          } 
          
          if (certValid && vaccine.getDoseNumber() < vaccine.getTotalSeriesOfDoses())
          {
            response = new DivaScannerResponse(DivaScannerResponse.OK,
              "Unvollständige Impfung");
            certValid = false;
          }
          
          if ( certValid )
          {
            response = new DivaScannerResponse(DivaScannerResponse.OK, "Success");
          }

          response.setValid(certValid);

          DgcName name = dgcCommonPayload.getPayload().getPayload1().
            getName();

          String uniqueCertificateIdentifier = vaccine.getUniqueCertificateIdentifier();
          
          certJsonLogRepository.save(
            new DgcvCertJsonLog("reader",
              request.getReaderUuid(), uniqueCertificateIdentifier,
              name.getSurname(), name.getGivenname(),
              dgcCommonPayload.getPayload().getPayload1().getDayOfBirth(),
              jsonString, certValid));

          if ( ! certificateRepository.existsById(uniqueCertificateIdentifier))
          {
            LOGGER.debug( "Saving new certificate: {}", uniqueCertificateIdentifier );
            certificateRepository.save(new DgcvCertificate( "reader", dgcCommonPayload,
             "unknown", request.getOrganization(), request.getOrganizationUnit()
            ));
          }
          
          response.setDateOfVaccination(DATE_FORMAT.format(vaccine.getDateOfVaccination()));
          response.setUntil(DATE_FORMAT.format(date));

          response.setOwner(name.getGivenname() + " " + name.getSurname());
        }
        catch (CompressorException | IOException | CoseException ex)
        {
          LOGGER.error("Decoding payload ", ex);

          response = new DivaScannerResponse(
            DivaScannerResponse.ERROR, "Fehlerhaftes Zertifikat.");
        }
      }
      else
      {
        response = new DivaScannerResponse(
          DivaScannerResponse.ERROR, "Unbekanntes QR-Code Format.");
      }
    }

    return response;
  }

}
