package urlshortener.web;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
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
  private static final long KILOBYTE = 1024L;
  private static final long SECONDS = 1000L;

  public SystemInformationController(ClickService clickService, ShortURLService shortUrlService) {
    this.clickService = clickService;
    this.shortUrlService = shortUrlService;
  }

  @RequestMapping(value = "/system_info", method = RequestMethod.GET)
  public ResponseEntity<SystemInformation> sytemInfoSpot() {
    // TODO Get the number of users
    Long numUsers = 0L;

    RuntimeMXBean runtimer = ManagementFactory.getRuntimeMXBean();
    // Time in seconds
    Long UpTime = miliToSeconds(runtimer.getUptime());

    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    // Memory that's available for the applications to work correctly
    Long usedMemory = bytesToKilobytes(osBean.getCommittedVirtualMemorySize());
    // System Memory Size
    Long machineMemory = bytesToKilobytes(osBean.getTotalPhysicalMemorySize());
    // Free memory available
    Long freeMemory = bytesToKilobytes(osBean.getFreePhysicalMemorySize());

    HttpHeaders h = new HttpHeaders();
    // Get all clicks
    Long numClicks = clickService.getTotalClick();
    // Get all URLs
    Long numURLs = shortUrlService.getTotalURL();

    SystemInformation info =
        new SystemInformation(
            numClicks, numURLs, numUsers, UpTime, machineMemory, freeMemory, usedMemory);
    return new ResponseEntity<>(info, h, HttpStatus.OK);
  }

  private static long bytesToKilobytes(long bytes) {
    return bytes / KILOBYTE;
  }

  private static long miliToSeconds(long mili) {
    return mili / SECONDS;
  }
}
