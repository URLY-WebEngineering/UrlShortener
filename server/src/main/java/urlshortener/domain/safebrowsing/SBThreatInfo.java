package urlshortener.domain.safebrowsing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"threatTypes", "platformTypes", "threatEntryTypes", "threatEntries"})
@Data
@AllArgsConstructor
public class SBThreatInfo {

  @JsonProperty("threatTypes")
  public List<String> threatTypes;

  @JsonProperty("platformTypes")
  public List<String> platformTypes;

  @JsonProperty("threatEntryTypes")
  public List<String> threatEntryTypes;

  @JsonProperty("threatEntries")
  public List<SBThreatEntry> threatEntries;
}
