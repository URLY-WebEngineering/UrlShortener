package urlshortener.domain;

public class SystemInformation {
  Long numClicks;
  Long numURLs;
  Long numUsers;
  Long CpuTime;
  Long usedMemory;
  Long memoryAvailableSwap;

  Long memoryAvailablePhycal;
  // Memory used by the system
  // Memory avaible by the system

  public SystemInformation(
      Long numClicks,
      Long numURLs,
      Long CpuTime,
      Long usedMemory,
      Long memoryAvailableSwap,
      Long memoryAvailablePhycal) {
    this.numClicks = numClicks;
    this.numURLs = numURLs;
    this.CpuTime = CpuTime;
    this.usedMemory = usedMemory;
    this.memoryAvailableSwap = memoryAvailableSwap;
    this.memoryAvailablePhycal = memoryAvailablePhycal;
  }

  public void setCpuTime(Long cpuTime) {
    CpuTime = cpuTime;
  }

  public Long getCpuTime() {
    return CpuTime;
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

  public Long getUsedMemory() {
    return usedMemory;
  }

  public void setUsedMemory(Long usedMemory) {
    this.usedMemory = usedMemory;
  }

  public Long getMemoryAvailableSwap() {
    return memoryAvailableSwap;
  }

  public void setMemoryAvailableSwap(Long memoryAvailableSwap) {
    this.memoryAvailableSwap = memoryAvailableSwap;
  }

  public Long getMemoryAvailablePhycal() {
    return memoryAvailablePhycal;
  }

  public void setMemoryAvailablePhycal(Long memoryAvailablePhycal) {
    this.memoryAvailablePhycal = memoryAvailablePhycal;
  }
}
