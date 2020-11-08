package urlshortener.domain.safebrowsing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "url"
})
public class SBThreatEntry {

  @JsonProperty("url")
  public String url;

  public SBThreatEntry(){}

  public SBThreatEntry(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "SBThreatEntry{" +
        "url='" + url + '\'' +
        '}';
  }
}