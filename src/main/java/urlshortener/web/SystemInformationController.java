package urlshortener.web;

import java.util.ArrayList;
import java.util.List;
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
@EnableScheduling
@EnableAsync
@Component
public class SystemInformationController {

  private Integer numClicks;
  private Integer numUsers;
  private Integer numURLs;

  private final ClickService clickService;
  private final ShortURLService shortUrlService;

  public SystemInformationController(ClickService clickService, ShortURLService shortUrlService) {
    this.numClicks = 0;
    this.numURLs = 0;
    this.numUsers = 0;
    this.clickService = clickService;
    this.shortUrlService = shortUrlService;
  }

  @Scheduled(fixedRate = 1000)
  @Async
  public void checkSystemInformation() {
    numUsers = 0;
    numClicks = Math.toIntExact(clickService.getTotalClick());
    numURLs = (Math.toIntExact(shortUrlService.getTotalURL()));
  }

  @ReadOperation
  public List<Information> getInformation() {
    List<Information> list = new ArrayList<>();
    list.add(
        new Information("url.number", "Number of url shortened stored in the database", numURLs));
    list.add(
        new Information(
            "clicks.number", "Number of clicks to urls stored in  the database", numClicks));
    list.add(new Information("users.number", "Number of users  on the database", numUsers));
    return list;
  }
}
