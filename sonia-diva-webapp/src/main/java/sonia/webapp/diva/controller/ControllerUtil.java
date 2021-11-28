package sonia.webapp.diva.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import sonia.webapp.diva.BuildProperties;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
public class ControllerUtil
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    ControllerUtil.class.getName());

  public static String addGeneralAttributes(Model model)
  {
    String userId = null;
    model.addAttribute("errorMessage", null);
    model.addAttribute("build", BuildProperties.getInstance());
    return userId;
  }
}
