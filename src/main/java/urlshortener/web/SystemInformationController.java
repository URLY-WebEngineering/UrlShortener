package urlshortener.web;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import urlshortener.domain.Information;
import urlshortener.service.ClickService;
import urlshortener.service.ShortURLService;

@Endpoint(id = "database")
@Configuration
@EnableAsync
@EnableScheduling
@Component
public class SystemInformationController {

  private final AtomicInteger numClicks;
  private final AtomicInteger numUsers;
  private final AtomicInteger numURLs;

  private final ClickService clickService;
  private final ShortURLService shortUrlService;

  public SystemInformationController(ClickService clickService, ShortURLService shortUrlService) {
    this.clickService = clickService;
    this.shortUrlService = shortUrlService;

    this.numClicks = new AtomicInteger(Math.toIntExact(clickService.getTotalClick()));
    this.numURLs = new AtomicInteger(Math.toIntExact(shortUrlService.getTotalURL()));
    this.numUsers = new AtomicInteger(0);
  }

  @Async("threadTaskScheduler")
  @Scheduled(fixedRate = 1000, initialDelay = 500)
  public void checkSystemInformation() {
    numUsers.set(0);
    numClicks.set(Math.toIntExact(clickService.getTotalClick()));
    numURLs.set(Math.toIntExact(shortUrlService.getTotalURL()));
  }

  @ReadOperation
  public List<Information> getInformation() {
    List<Information> list = new ArrayList<>();
    list.add(
        new Information("URL.number", "Number of URL shortened stored in the database", numURLs));
    list.add(
        new Information(
            "Clicks.number", "Number of clicks to urls stored in  the database", numClicks));
    list.add(new Information("Users.number", "Number of users  on the database", numUsers));
    return list;
  }
}
