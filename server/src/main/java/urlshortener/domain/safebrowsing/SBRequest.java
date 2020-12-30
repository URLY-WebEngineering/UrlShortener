package urlshortener.domain.safebrowsing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"client", "threatInfo"})
@Data
@AllArgsConstructor
public class SBRequest {

  @JsonProperty("client")
  public SBClient client;

  @JsonProperty("threatInfo")
  public SBThreatInfo threatInfo;
}
