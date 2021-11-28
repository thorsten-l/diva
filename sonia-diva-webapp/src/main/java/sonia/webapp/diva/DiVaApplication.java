package sonia.webapp.diva;

import java.io.PrintWriter;
import java.util.Properties;
import org.apache.derby.drda.NetworkServerControl;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sonia.webapp.diva.crypto.AES256;
import sonia.webapp.diva.crypto.PasswordGenerator;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@SpringBootApplication
public class DiVaApplication
{
  private final static Logger LOGGER = LoggerFactory.getLogger(
    DiVaApplication.class.getName());

  private final static Options OPTIONS = new Options();

  private static NetworkServerControl server;

  public static void main(String[] args) throws Exception
  {
    CmdLineParser parser;

    parser = new CmdLineParser(OPTIONS);

    try
    {
      parser.parseArgument(args);
    }
    catch (CmdLineException ex)
    {
      LOGGER.error("Command line error\n");
      parser.printUsage(System.out);
      System.exit(-1);
    }

    if (OPTIONS.isDisplayHelp())
    {
      System.out.println("\nUsage: ./diva.jar [options]\n");
      parser.printUsage(System.out);
      System.exit(0);
    }


    BuildProperties build = BuildProperties.getInstance();
    LOGGER.info("Project Name    : " + build.getProjectName());
    LOGGER.info("Project Version : " + build.getProjectVersion());
    LOGGER.info("Build Timestamp : " + build.getTimestamp());
    LOGGER.info("Build Profile   : " + build.getProfile() + "\n");

    Config config = Config.getInstance();

    LOGGER.trace("\n\nwebappContextPath={}"
      + "\nwebappServerPort={}"
      + "\napiToken={}"
      + "\nportletApiToken={}"
      + "\ndbUser={}"
      + "\ndbPassword={}"
      + "\ndbBootPassword={}"
      + "\nmediaStorePath={}"
      + "\n",
      config.getWebappContextPath(),
      config.getWebappServerPort(),
      config.getApiToken(),
      config.getPortletApiToken(),
      config.getDbUser(),
      config.getDbPassword(),
      config.getDbBootPassword(),
      config.getMediaStorePath()
    );

    if (OPTIONS.getGeneratePasswordLength() > 0 )
    {
      AES256 cipher = new AES256(Config.getSECRET_KEY());
      String password = PasswordGenerator.generate(OPTIONS.getGeneratePasswordLength());
      System.out.println("password:  '" + password + "'" );
      System.out.println(  "encrypted: '" + cipher.encrypt(password) + "'\n" );
      System.exit(0);
    }
    
    if (OPTIONS.getPassword() != null )
    {
      AES256 cipher = new AES256(Config.getSECRET_KEY());
      System.out.println("password:  '" + OPTIONS.getPassword() + "'" );
      System.out.println(  "encrypted: '" + cipher.encrypt(OPTIONS.getPassword()) + "'\n" );
      System.exit(0);
    }
    
    LOGGER.debug("*** Start Application ***");
    Properties properties = System.getProperties();
    properties.put("derby.system.home", "derbydb");

    properties.put("spring.datasource.username", config.getDbUser());
    properties.put("spring.datasource.password", config.getDbPassword());

    // Start network server in an development environment only
    if ("development".equals(build.getProfile()))
    {
      String dbUrl = "jdbc:derby://localhost:1527/diva;create=true;dataEncryption=true"
        + ";bootPassword=" + config.getDbBootPassword()
        + ";user=" + config.getDbUser()
        + ";password=" + config.getDbPassword();

      // Start Apache Derby Network server
      properties.put("spring.datasource.url", dbUrl);

      properties.put("spring.datasource.driver-class-name",
        "org.apache.derby.jdbc.ClientDriver");

      server = new NetworkServerControl();
      LOGGER.info("*** Starting Apache Derby network server ***");
      server.start(new PrintWriter(System.out));

      Runtime.getRuntime().addShutdownHook(new Thread()
      {
        @Override
        public void run()
        {
          LOGGER.info("Shutdown hook is running!");
          try
          {
            if (server != null)
            {
              server.shutdown();
            }
          }
          catch (Exception ex)
          {
            LOGGER.error("Shutdown Apache Derby failed. ", ex);
          }
        }
      });
    }
    else
    {
      properties.put("spring.datasource.url",
        "jdbc:derby:diva;create=true;dataEncryption=true"
        + ";bootPassword=" + config.getDbBootPassword()
        + ";user=" + config.getDbUser()
        + ";password=" + config.getDbPassword()
      );
      properties.put("spring.datasource.driver-class-name",
        "org.apache.derby.jdbc.EmbeddedDriver");
      LOGGER.info("*** Starting embedded Apache Derby engine ***");
    }

    SpringApplication.run(DiVaApplication.class, args);
  }
}
