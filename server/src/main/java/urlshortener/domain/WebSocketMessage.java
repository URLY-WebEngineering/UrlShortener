package urlshortener.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"user", "hash"})
@Data
@AllArgsConstructor
public class WebSocketMessage {

  @JsonProperty("user")
  private String user;

  @JsonProperty("hash")
  private String hash;
}
