package urlshortener.web;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import urlshortener.domain.SystemInformation;
import urlshortener.service.ClickService;
import urlshortener.service.ShortURLService;

@Controller
public class SystemInformationController {

  private final ClickService clickService;
  private final ShortURLService shortUrlService;

  public SystemInformationController(ClickService clickService, ShortURLService shortUrlService) {
    this.clickService = clickService;
    this.shortUrlService = shortUrlService;
  }

  @RequestMapping(value = "/system_info", method = RequestMethod.GET)
  public ResponseEntity<SystemInformation> sytemInfoSpot() {
    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    // Time in nanoseconds
    Long CpuTime = osBean.getProcessCpuTime();

    // Returns the amount of free swap space in bytes.
    Long memoryAvailableSwap = osBean.getFreeSwapSpaceSize() / 1024 / 1024 / 1024;
    Long memoryAvailablePhycal = osBean.getFreePhysicalMemorySize()/ 1024 / 1024 / 1024;

    Long usedMemory = osBean.getCommittedVirtualMemorySize() / 1024 / 1024 / 1024;
    // Returns the amount of virtual memory that is guaranteed to be available to the running
    // process in bytes, or -1 if this operation is not supported.

    HttpHeaders h = new HttpHeaders();
    Long numClicks = clickService.getTotalClick();
    Long numURLs = shortUrlService.getTotalURL();
    SystemInformation info =
        new SystemInformation(
            numClicks, numURLs, CpuTime, usedMemory, memoryAvailableSwap, memoryAvailablePhycal);
    return new ResponseEntity<>(info, h, HttpStatus.OK);
  }
}
