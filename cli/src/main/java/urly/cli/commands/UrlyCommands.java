package urly.cli.commands;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@ShellComponent
public class UrlyCommands {

  /**
   * Shorts a URL requesting to URLY backend server
   * @param url
   * @param qr optional, true if wanted to recevice a QR code
   * @param backhalf optional, text to replace the default hash in a URL shorten
   * @return
   */
  @ShellMethod("Short a Url.")
  public Map<String, String> shortUrl(
      String url, boolean qr, @ShellOption(defaultValue = "") String backhalf) {
    UriComponentsBuilder uriComponentsBuilder =
        UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .path("/link")
            .port(8080)
            .queryParam("url", url);
    if (qr) {
      uriComponentsBuilder.queryParam("qrfeature", "on");
    }
    if (!backhalf.equals("")) {
      uriComponentsBuilder.queryParam("custombackhalf", backhalf);
    }

    System.out.println("Tu peticion es:\n" + uriComponentsBuilder.toUriString() + "\n");
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<String> response =
        restTemplate.postForEntity(uriComponentsBuilder.toUriString(), "", String.class);

    String json = response.getBody();
    Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

    Map<String, String> result = new HashMap<>();
    String uri = JsonPath.read(document, "$.uri");
    result.put("uri", uri);
    if (qr) {
      String qrUri = JsonPath.read(document, "$.qr");
      result.put("qrUri", qrUri);
    }
    return result;
  }
}
