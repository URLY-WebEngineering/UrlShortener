package urly.cli.commands;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.springframework.http.HttpEntity;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@ShellComponent
public class UrlyCommands {

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
