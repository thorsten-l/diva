package sonia.webapp.diva.crypto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import sonia.webapp.diva.Config;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
public class PasswordSerializer extends StdSerializer<String>
{
  private final AES256 CIPHER = new AES256(Config.getSECRET_KEY());

  public PasswordSerializer()
  {
    this(null);
  }

  public PasswordSerializer(Class<String> t)
  {
    super(t);
  }

  /**
   *
   * @param password
   * @param jsonGenerator
   * @param sp
   * @throws IOException
   */
  @Override
  public void serialize(String password, JsonGenerator jsonGenerator, SerializerProvider sp)
    throws IOException
  {
    jsonGenerator.writeString(CIPHER.encrypt(password));
  }


}
