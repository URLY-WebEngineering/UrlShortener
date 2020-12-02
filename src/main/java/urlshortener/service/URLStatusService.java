package urlshortener.service;

import com.jayway.jsonpath.JsonPath;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import urlshortener.domain.ShortURL;
import urlshortener.domain.safebrowsing.SBClient;
import urlshortener.domain.safebrowsing.SBRequest;
import urlshortener.domain.safebrowsing.SBThreatEntry;
import urlshortener.domain.safebrowsing.SBThreatInfo;
import urlshortener.repository.ShortURLRepository;
import urlshortener.repository.impl.ShortURLRepositoryImpl;

@Service
public class URLStatusService {

  private final String API_KEY = System.getenv("GSB_API_KEY");

  private final ShortURLRepository shortURLRepository;

  public URLStatusService(ShortURLRepositoryImpl shortURLRepository) {
    this.shortURLRepository = shortURLRepository;
  }

  public boolean isReachable(String urlToCheck) {
    try {
      URL url = new URL(urlToCheck);
      HttpURLConnection huc = (HttpURLConnection) url.openConnection(); // NOSONAR
      huc.setRequestMethod("HEAD"); // Only HEAD request to reduce response time and bandwidth
      int responseCode = huc.getResponseCode();
      return responseCode == HttpURLConnection.HTTP_OK;
    } catch (Exception e) { // All HTTP Exceptions
      return false;
    }
  }

  public boolean isSafe(String url) {
    // Create request through domain classes
    SBClient sbClient = new SBClient("urly", "1.0");
    List<String> threatTypes = Arrays.asList("MALWARE", "SOCIAL_ENGINEERING", "UNWANTED_SOFTWARE");
    List<String> platformTypes = Collections.singletonList("ALL_PLATFORMS");
    List<String> threatEntryTypes = Collections.singletonList("URL");
    SBThreatEntry threatEntry = new SBThreatEntry(url);
    List<SBThreatEntry> threatEntries = Collections.singletonList(threatEntry);
    SBThreatInfo sbThreatInfo =
        new SBThreatInfo(threatTypes, platformTypes, threatEntryTypes, threatEntries);
    SBRequest sbRequest = new SBRequest(sbClient, sbThreatInfo);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Send request to Google Safe Browsing API (Lookup API)
    HttpEntity<SBRequest> request = new HttpEntity<>(sbRequest, headers);
    RestTemplate restTemplate = new RestTemplate();
    String sbUrl = "https://safebrowsing.googleapis.com/v4/threatMatches:find?key=" + API_KEY;
    ResponseEntity<String> response = restTemplate.postForEntity(sbUrl, request, String.class);

    // Decide if it's safe
    if (response.getStatusCode() != HttpStatus.OK) {
      return false;
    } else {
      String json = response.getBody();
      List<String> matches = JsonPath.parse(json).read("$..matches");
      return matches.isEmpty();
    }
  }

  @Async
  public void checkStatus(ShortURL shortURL) throws InterruptedException {
    String url = shortURL.getTarget();
    ShortURL updatedShortURL = new ShortURL();
    BeanUtils.copyProperties(shortURL, updatedShortURL);
    if (isReachable(url) && isSafe(url)) {
      updatedShortURL.setReachable(true);
      updatedShortURL.setSafe(true);
    }
    updatedShortURL.setChecked(true);
    shortURLRepository.update(updatedShortURL);
  }
}
