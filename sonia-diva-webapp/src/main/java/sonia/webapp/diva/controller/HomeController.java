package sonia.webapp.diva.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sonia.webapp.diva.BuildProperties;
import sonia.webapp.diva.Config;
import sonia.diva.api.DivaScannerRequest;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@Controller
public class HomeController
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    HomeController.class.getName());

  @GetMapping("/kaputt")
  public void kaputt()
  {
    throw new RuntimeException("kaputt");
  }

  @GetMapping("/")
  public String homeGET(Model model)
  {
    LOGGER.debug("homeGET");
    
    DivaScannerRequest formObject = new DivaScannerRequest();
    formObject.setAction("validate");
    formObject.setApiToken(Config.getInstance().getApiToken());
    formObject.setReaderUuid("e0824684-c89d-49c4-b110-0c0f7758d5eb");
    formObject.setOrganization("Ostfalia");
    formObject.setOrganizationUnit("Rechenzentum");
    formObject.setCameraIndex(0);
    
    model.addAttribute("formObject", formObject );
    model.addAttribute("readerUuid", "e0824684-c89d-49c4-b110-0c0f7758d5eb");
    model.addAttribute("apiToken", Config.getInstance().getApiToken());
    model.addAttribute("build", BuildProperties.getInstance());
    return "home";
  }
}
