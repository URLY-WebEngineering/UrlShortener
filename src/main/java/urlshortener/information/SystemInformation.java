package urlshortener.information;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import urlshortener.service.ClickService;
import urlshortener.service.ShortURLService;

@Endpoint(id = "database")
@Configuration
@EnableScheduling
@Component
public class SystemInformation {

  private final AtomicInteger numClicks;
  private final AtomicInteger numUsers;
  private final AtomicInteger numURLs;

  private final ClickService clickService;
  private final ShortURLService shortUrlService;

  public SystemInformation(ClickService clickService, ShortURLService shortUrlService) {
    this.numClicks = new AtomicInteger(0);
    this.numURLs = new AtomicInteger(0);
    this.numUsers = new AtomicInteger(0);
    this.clickService = clickService;
    this.shortUrlService = shortUrlService;
  }

  @Scheduled(fixedRate = 1000)
  public void checkSystemInformation() {
    numUsers.set(0);
    numClicks.set(Math.toIntExact(clickService.getTotalClick()));
    numURLs.set(Math.toIntExact(shortUrlService.getTotalURL()));
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

  public class Information {
    public String name;
    public String description;
    public AtomicInteger number;

    public Information(String name, String description, AtomicInteger number) {
      this.name = name;
      this.description = description;
      this.number = number;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public AtomicInteger getNumber() {
      return number;
    }

    public void setNumber(AtomicInteger number) {
      this.number = number;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
