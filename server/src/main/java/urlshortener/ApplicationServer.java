package urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ApplicationServer extends SpringBootServletInitializer {

  public static void main(String[] args) {
    checkEnvVars(); // NOSONAR
    SpringApplication.run(ApplicationServer.class, args); // NOSONAR
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(ApplicationServer.class); // NOSONAR
  }

  private static void checkEnvVars() {
    String API_KEY = System.getenv("GSB_API_KEY"); // NOSONAR
    if (API_KEY == null) { // NOSONAR
      throw new RuntimeException("GSB_API_KEY not established as env variable"); // NOSONAR
    }
  }
}
