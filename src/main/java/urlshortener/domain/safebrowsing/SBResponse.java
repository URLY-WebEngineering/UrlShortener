package urlshortener.domain.safebrowsing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"matches"})
public class SBResponse {

  @JsonProperty("matches")
  public List<SBMatch> matches = null;

  public SBResponse() {}

  public SBResponse(List<SBMatch> matches) {
    this.matches = matches;
  }

  public List<SBMatch> getMatches() {
    return matches;
  }

  public void setMatches(List<SBMatch> matches) {
    this.matches = matches;
  }

  @Override
  public String toString() {
    return "SBResponse{" + "matches=" + matches + '}';
  }
}
