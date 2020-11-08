package urlshortener.domain.safebrowsing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "threatTypes",
    "platformTypes",
    "threatEntryTypes",
    "threatEntries"
})
public class SBThreatInfo {

  @JsonProperty("threatTypes")
  public List<String> threatTypes = null;
  @JsonProperty("platformTypes")
  public List<String> platformTypes = null;
  @JsonProperty("threatEntryTypes")
  public List<String> threatEntryTypes = null;
  @JsonProperty("threatEntries")
  public List<SBThreatEntry> threatEntries = null;

  public SBThreatInfo(){}

  public SBThreatInfo(List<String> threatTypes, List<String> platformTypes, List<String> threatEntryTypes, List<SBThreatEntry> threatEntries) {
    this.threatTypes = threatTypes;
    this.platformTypes = platformTypes;
    this.threatEntryTypes = threatEntryTypes;
    this.threatEntries = threatEntries;
  }

  public List<String> getThreatTypes() {
    return threatTypes;
  }

  public void setThreatTypes(List<String> threatTypes) {
    this.threatTypes = threatTypes;
  }

  public List<String> getPlatformTypes() {
    return platformTypes;
  }

  public void setPlatformTypes(List<String> platformTypes) {
    this.platformTypes = platformTypes;
  }

  public List<String> getThreatEntryTypes() {
    return threatEntryTypes;
  }

  public void setThreatEntryTypes(List<String> threatEntryTypes) {
    this.threatEntryTypes = threatEntryTypes;
  }

  public List<SBThreatEntry> getThreatEntries() {
    return threatEntries;
  }

  public void setThreatEntries(List<SBThreatEntry> threatEntries) {
    this.threatEntries = threatEntries;
  }

  @Override
  public String toString() {
    return "SBThreatInfo{" +
        "threatTypes=" + threatTypes +
        ", platformTypes=" + platformTypes +
        ", threatEntryTypes=" + threatEntryTypes +
        ", threatEntries=" + threatEntries +
        '}';
  }
}