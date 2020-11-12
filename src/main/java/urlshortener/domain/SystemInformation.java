package urlshortener.domain;

public class SystemInformation {
  Long numClicks;
  Long numURLs;
  Long numUsers;
  // Time the system has been up and running
  Long UpTime;
  // Memory in KBs
  Long SystemMemory;
  Long AvailableMemory;
  Long UsedMemory;

  public SystemInformation(
      Long numClicks,
      Long numURLs,
      Long numUsers,
      Long UpTime,
      Long SystemMemory,
      Long AvailableMemory,
      Long UsedMemory) {
    this.numClicks = numClicks;
    this.numURLs = numURLs;
    this.numUsers = numUsers;
    this.UpTime = UpTime;
    this.SystemMemory = SystemMemory;
    this.AvailableMemory = AvailableMemory;
    this.UsedMemory = UsedMemory;
  }

  public void setUpTime(Long UpTime) {
    this.UpTime = UpTime;
  }

  public Long getUpTime() {
    return UpTime;
  }

  public void setNumClicks(Long numClicks) {
    this.numClicks = numClicks;
  }

  public Long getNumClicks() {
    return numClicks;
  }

  public Long getNumURLs() {
    return numURLs;
  }

  public void setNumURLs(Long numURLs) {
    this.numURLs = numURLs;
  }

  public Long getNumUsers() {
    return numUsers;
  }

  public void setNumUsers(Long numUsers) {
    this.numUsers = numUsers;
  }

  public Long getSystemMemory() {
    return SystemMemory;
  }

  public void setSystemMemory(Long systemMemory) {
    SystemMemory = systemMemory;
  }

  public Long getAvailableMemory() {
    return AvailableMemory;
  }

  public void setAvailableMemory(Long availableMemory) {
    AvailableMemory = availableMemory;
  }

  public Long getUsedMemory() {
    return UsedMemory;
  }

  public void setUsedMemory(Long usedMemory) {
    UsedMemory = usedMemory;
  }
}
