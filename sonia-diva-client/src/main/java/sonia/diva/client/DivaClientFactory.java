package sonia.diva.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
public class DivaClientFactory
{
  
  private DivaClientFactory()
  {
  }

  public static DivaClient getClient( String url, String apiToken, String organization )
  {
    Client client = ClientBuilder.newClient();

    WebTarget target = client.target(url);

    return new DivaClient(target, apiToken, organization );
  }
}
