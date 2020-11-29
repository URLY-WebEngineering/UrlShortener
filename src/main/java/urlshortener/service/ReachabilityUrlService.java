package urlshortener.service;

import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.stereotype.Service;

@Service
public class ReachabilityUrlService {
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
}
