package urlshortener.domain.safebrowsing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "clientId",
    "clientVersion"
})
public class SBClient {

  @JsonProperty("clientId")
  public String clientId;
  @JsonProperty("clientVersion")
  public String clientVersion;

  public SBClient() {
  }

  public SBClient(String clientId, String clientVersion) {
    this.clientId = clientId;
    this.clientVersion = clientVersion;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientVersion() {
    return clientVersion;
  }

  public void setClientVersion(String clientVersion) {
    this.clientVersion = clientVersion;
  }

  @Override
  public String toString() {
    return "SBClient{" +
        "clientId='" + clientId + '\'' +
        ", clientVersion='" + clientVersion + '\'' +
        '}';
  }
}
