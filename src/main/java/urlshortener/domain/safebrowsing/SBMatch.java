package urlshortener.domain.safebrowsing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "threatType",
    "platformType",
    "threat",
    "cacheDuration",
    "threatEntryType"
})
public class SBMatch {

  @JsonProperty("threatType")
  public String threatType;
  @JsonProperty("platformType")
  public String platformType;
  @JsonProperty("threat")
  public SBThreat threat;
  @JsonProperty("cacheDuration")
  public String cacheDuration;
  @JsonProperty("threatEntryType")
  public String threatEntryType;

  public SBMatch(){}

  public SBMatch(String threatType, String platformType, SBThreat threat, String cacheDuration, String threatEntryType) {
    this.threatType = threatType;
    this.platformType = platformType;
    this.threat = threat;
    this.cacheDuration = cacheDuration;
    this.threatEntryType = threatEntryType;
  }

  public String getThreatType() {
    return threatType;
  }

  public void setThreatType(String threatType) {
    this.threatType = threatType;
  }

  public String getPlatformType() {
    return platformType;
  }

  public void setPlatformType(String platformType) {
    this.platformType = platformType;
  }

  public SBThreat getThreat() {
    return threat;
  }

  public void setThreat(SBThreat threat) {
    this.threat = threat;
  }

  public String getCacheDuration() {
    return cacheDuration;
  }

  public void setCacheDuration(String cacheDuration) {
    this.cacheDuration = cacheDuration;
  }

  public String getThreatEntryType() {
    return threatEntryType;
  }

  public void setThreatEntryType(String threatEntryType) {
    this.threatEntryType = threatEntryType;
  }

  @Override
  public String toString() {
    return "SBMatch{" +
        "threatType='" + threatType + '\'' +
        ", platformType='" + platformType + '\'' +
        ", threat=" + threat +
        ", cacheDuration='" + cacheDuration + '\'' +
        ", threatEntryType='" + threatEntryType + '\'' +
        '}';
  }
}