package sonia.webapp.diva;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sonia.webapp.diva.crypto.AES256;
import sonia.webapp.diva.crypto.PasswordDeserializer;
import sonia.webapp.diva.crypto.PasswordGenerator;
import sonia.webapp.diva.crypto.PasswordSerializer;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Config
{
  private final static Logger LOGGER = LoggerFactory.getLogger(Config.class.
    getName());

  public final static String CONFIG_FILE = "config.json";

  public final static String SECRET_FILE = "secret.bin";

  private final static Config SINGLETON;

  @Getter
  @JsonIgnore
  private static byte[] SECRET_KEY = new byte[48];

  @Getter
  @JsonIgnore
  private final static long startTimestamp = System.currentTimeMillis();

  private Config()
  {
  }

  static
  {
    ObjectMapper objectMapper = new ObjectMapper();
    Config config = null;

    try
    {
      File configFile = new File(CONFIG_FILE);
      File secretFile = new File(SECRET_FILE);

      if (secretFile.exists() && configFile.exists())
      {
        LOGGER.info("Loading secret file");
        try (FileInputStream input = new FileInputStream(secretFile)) {
          input.read(SECRET_KEY);
        }

        LOGGER.trace("secret key = {}", SECRET_KEY);
        
        LOGGER.info("Loading config file");
        config = objectMapper.readValue(configFile, Config.class);
      }
      else
      {
        LOGGER.info("Writing secret file");

        try (FileOutputStream output = new FileOutputStream(secretFile))
        {
          AES256 aes256 = new AES256();
          SECRET_KEY = aes256.getSecret();
          output.write(SECRET_KEY);
        }
        
        // file permissions - r-- --- ---
        secretFile.setExecutable(false, false);
        secretFile.setWritable(false, false);
        secretFile.setReadable(false, false);
        secretFile.setReadable(true, true);
        
        LOGGER.trace("secret key = {}", SECRET_KEY);

        LOGGER.info("Writing config file");
        config = new Config();

        config.mediaStorePath = "store";
        config.dbUser = "diva";
        config.dbPassword = PasswordGenerator.generate(32);
        config.dbBootPassword = PasswordGenerator.generate(64);
        config.apiToken = PasswordGenerator.generate(64);
        config.portletApiToken = PasswordGenerator.generate(64);
        config.webappContextPath = "";
        config.webappServerPort = 8080;

        objectMapper.writerWithDefaultPrettyPrinter()
          .writeValue(configFile, config);
        
        // file permissions - rw- --- ---
        configFile.setExecutable(false, false);
        configFile.setWritable(false, false);
        configFile.setReadable(false, false);
        configFile.setReadable(true, true);        
        configFile.setWritable(true, true);
      }
    }
    catch (IOException e)
    {
      LOGGER.error("ERROR: Config Properties ", e);
      System.exit(-1);
    }
    catch (NoSuchAlgorithmException ex)
    {
      java.util.logging.Logger.getLogger(Config.class.getName()).
        log(Level.SEVERE, null, ex);
    }

    SINGLETON = config;
  }

  public static Config getInstance()
  {
    return SINGLETON;
  }

  @Getter
  private String webappContextPath;

  @Getter
  private int webappServerPort;

  @Getter
  private String mediaStorePath;

  @Getter
  @JsonSerialize(using = PasswordSerializer.class)
  @JsonDeserialize(using = PasswordDeserializer.class)
  private String apiToken;

  @Getter
  @JsonSerialize(using = PasswordSerializer.class)
  @JsonDeserialize(using = PasswordDeserializer.class)
  private String portletApiToken;

  @Getter
  private String dbUser;

  @Getter
  @JsonSerialize(using = PasswordSerializer.class)
  @JsonDeserialize(using = PasswordDeserializer.class)
  private String dbPassword;

  @Getter
  @JsonSerialize(using = PasswordSerializer.class)
  @JsonDeserialize(using = PasswordDeserializer.class)
  private String dbBootPassword;
}
