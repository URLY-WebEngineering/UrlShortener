package systemInformation.services;

import com.jayway.jsonpath.JsonPath;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KeycloakAccessService {

  @Value("${url.token.host}")
  private String TOKEN_URL;

  @Value("${url.count.host}")
  private String COUNT_URL;

  public KeycloakAccessService() {}

  /**
   * Ask for the number of users in the keycloak mysql server. To achieve its functionality first
   * gets the access token by request the server through a POST petition and then gets the number of
   * users by a GET request using the token
   *
   * @return the number of user
   */
  public int countNumberOfUser() {
    RestTemplate template = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setCacheControl(CacheControl.noCache());

    String data = "grant_type=password&username=admin&password=admin&client_id=admin-cli";

    HttpEntity<String> entity = new HttpEntity<>(data, headers);

    ResponseEntity<String> response =
        template.exchange(TOKEN_URL, HttpMethod.POST, entity, String.class);

    // check response
    if (response.getStatusCode() == HttpStatus.OK) {

      String json = response.getBody();

      List<String> matches = JsonPath.parse(json).read("$..access_token");

      String token = matches.get(0);
      // set custom header
      headers = new HttpHeaders();
      // set `accept` header
      headers.setBearerAuth(token);
      headers.setCacheControl(CacheControl.noCache());
      // build the request
      HttpEntity request = new HttpEntity(headers);

      // use `exchange` method for HTTP call
      response = template.exchange(COUNT_URL, HttpMethod.GET, request, String.class);

      if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        return Integer.parseInt(response.getBody());
      } else {
        return 0;
      }

    } else {
      return 0;
    }
  }
}
