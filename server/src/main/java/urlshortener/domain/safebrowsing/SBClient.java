package urlshortener.domain.safebrowsing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"clientId", "clientVersion"})
@Data
@AllArgsConstructor
public class SBClient {

  @JsonProperty("clientId")
  public String clientId;

  @JsonProperty("clientVersion")
  public String clientVersion;
}
