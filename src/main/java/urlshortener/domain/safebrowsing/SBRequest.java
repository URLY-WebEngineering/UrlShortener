package urlshortener.domain.safebrowsing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"client", "threatInfo"})
public class SBRequest {

  @JsonProperty("client")
  public SBClient client;

  @JsonProperty("threatInfo")
  public SBThreatInfo threatInfo;

  public SBRequest() {}

  public SBRequest(SBClient client, SBThreatInfo threatInfo) {
    this.client = client;
    this.threatInfo = threatInfo;
  }

  public SBClient getClient() {
    return client;
  }

  public void setClient(SBClient client) {
    this.client = client;
  }

  public SBThreatInfo getThreatInfo() {
    return threatInfo;
  }

  public void setThreatInfo(SBThreatInfo threatInfo) {
    this.threatInfo = threatInfo;
  }

  @Override
  public String toString() {
    return "SBRequest{" + "client=" + client + ", threatInfo=" + threatInfo + '}';
  }
}
