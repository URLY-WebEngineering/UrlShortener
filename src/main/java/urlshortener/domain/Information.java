package urlshortener.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "description", "number"})
@Data
@AllArgsConstructor
public class Information {
  @JsonProperty("name")
  public String name;

  @JsonProperty("description")
  public String description;

  @JsonProperty("number")
  public Integer number;
}
