package sonia.webapp.diva.crypto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import sonia.webapp.diva.Config;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
public class PasswordDeserializer extends StdDeserializer<String>
{
  private final AES256 CIPHER = new AES256(Config.getSECRET_KEY());
  
  public PasswordDeserializer()
  {
    this(null);
  }

  public PasswordDeserializer(Class<?> vc)
  {
    super(vc);
  }

  @Override
  public String deserialize(JsonParser jsonparser, DeserializationContext dc) throws
    IOException,
    JsonProcessingException
  {
    return CIPHER.decrypt(jsonparser.getText());
  }

}
