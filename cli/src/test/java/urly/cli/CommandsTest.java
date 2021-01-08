package urly.cli;

import java.net.URISyntaxException;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import urly.cli.commands.UrlyCommands;

public class CommandsTest {

  UrlyCommands urlyCommands;

  @Before
  public void setup() {
    urlyCommands = new UrlyCommands();
  }

  @Test
  public void shortUrlTest() {
    Map<String, String> result = urlyCommands.shortUrl("https://ejemplo1.com/", false, "");
    Assert.assertNotNull(result);
    Assert.assertTrue(result.containsKey("uri"));
  }

  @Test
  public void shortUrlWithQRTest() {
    Map<String, String> result = urlyCommands.shortUrl("https://ejemplo2.com/", true, "");
    Assert.assertNotNull(result);
    Assert.assertTrue(result.containsKey("qrUri"));
  }

  @Test
  public void shortUrlWithBackhalfTest() {
    Map<String, String> result =
        urlyCommands.shortUrl("https://ejemplo3.com/", false, "mibackhalf1");
    Assert.assertNotNull(result);
    Assert.assertTrue(result.get("uri").contains("mibackhalf"));
  }

  @Test
  public void shortUrlWithAllArgs() {
    Map<String, String> result =
        urlyCommands.shortUrl("https://ejemplo4.com/", true, "mibackhalf2");
    Assert.assertNotNull(result);
    Assert.assertTrue(result.containsKey("qrUri"));
    Assert.assertTrue(result.get("uri").contains("mibackhalf"));
  }
}
