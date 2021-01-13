package urlshortener.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ToString
@AllArgsConstructor
public class ShortURLRepresentative {

  @JsonProperty private String hash;

  @JsonProperty private String information;
}
