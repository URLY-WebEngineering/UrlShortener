package urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Application extends SpringBootServletInitializer {

  public static void main(String[] args) {
    checkEnvVars();
    SpringApplication.run(Application.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Application.class);
  }

  private static void checkEnvVars() {
    String API_KEY = System.getenv("GSB_API_KEY");
    if (API_KEY == null) {
      throw new RuntimeException("GSB_API_KEY not established as env variable");
    }
  }
}
