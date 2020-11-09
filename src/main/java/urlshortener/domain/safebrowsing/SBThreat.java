package urlshortener.domain.safebrowsing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"url"})
public class SBThreat {

  @JsonProperty("url")
  public String url;

  public SBThreat() {}

  public SBThreat(String url) {
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
    return "SBThreat{" + "url='" + url + '\'' + '}';
  }
}
