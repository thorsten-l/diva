package sonia.webapp.diva.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sonia.diva.api.DivaScannerRequest;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@Controller
public class ScannerController
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    ScannerController.class.getName());

  @GetMapping("/scanner")
  public String scannerGET(
    @RequestParam(name = "readerUuid", required = true) String readerUuid,
    @RequestParam(name = "apiToken", required = true) String apiToken,
    @RequestParam(name = "organization", required = true) String organization,
    @RequestParam(name = "organizationUnit", required = true) String organizationUnit,
    Model model)
  {
    LOGGER.debug("scannerGET");
    model.addAttribute("apiToken", apiToken );
    model.addAttribute("readerUuid", readerUuid );
    model.addAttribute("organization", organization );
    model.addAttribute("organizationUnit", organizationUnit );
    return "scanner";
  }
  
  @PostMapping("/scanner")
  public String scannerPOST( DivaScannerRequest formObject,
    Model model)
  {
    LOGGER.debug("scannerPOST: {}", formObject.toString());
    model.addAttribute("apiToken", formObject.getApiToken() );
    model.addAttribute("readerUuid", formObject.getReaderUuid() );
    model.addAttribute("organization", formObject.getOrganization() );
    model.addAttribute("organizationUnit", formObject.getOrganizationUnit() );
    return (formObject.getCameraIndex() == 0 ) ? "scanner" : "scanner_rear";
  }
}
