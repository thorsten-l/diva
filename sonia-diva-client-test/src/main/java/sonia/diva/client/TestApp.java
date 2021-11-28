package sonia.diva.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import sonia.diva.api.DivaHealthStatusListEntry;
import sonia.diva.api.DivaHealthStatusListResponse;
import sonia.diva.api.DivaMediaDataResponse;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@ostfalia.de)
 */
public class TestApp
{
  private static final String O = "ostfalia";

  private static final String OU = "rechenzentrum";

  public static void main(String[] args) throws Exception
  {
    DivaClient client = DivaClientFactory.
      getClient("http://localhost:8088/api/", "halloApi123", O);

    File imageFile = new File("test.png");
    int fileLength = (int) imageFile.length();
    byte[] content = new byte[fileLength];
    FileInputStream input = new FileInputStream(imageFile);
    input.read(content);
    input.close();

    client.persistHealthStatus(OU, "Selbsttest", "id0815", "Max Mustermann",
      "id4711", "Der Überwacher", "image/png", content);

    // list all entries of an ou
    DivaHealthStatusListEntry firstEntry = null;
    DivaHealthStatusListResponse response = client.listHealthStatus(OU);

    if (response != null)
    {
      for (DivaHealthStatusListEntry entry : response.getEntries())
      {
        if (firstEntry == null)
        {
          firstEntry = entry;
        }
        System.out.println(entry.toString());
      }
    }

    if (firstEntry != null)
    {
      client.validateHealthStatus(OU, firstEntry.getId(), "id007", "James Bond",
        "killed");

      DivaMediaDataResponse mediaDataResponse = client.getMediaData(
        OU, firstEntry.getId());

      if (mediaDataResponse.getCode() == DivaMediaDataResponse.OK)
      {
        new File("private").mkdirs();
        try (FileOutputStream output = new FileOutputStream(
          "private/output.png"))
        {
          output.write(mediaDataResponse.getMediaData());
        }
      }

      // false positiv test
      client.getMediaData("GibtsNicht", firstEntry.getId());
    }
  }
}
