package urlshortener.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class AsyncConfig {
  @Bean(name = "threadTaskExecutor")
  public Executor threadTaskExecutor() {
    SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
    simpleAsyncTaskExecutor.setConcurrencyLimit(3);
    return simpleAsyncTaskExecutor;
  }

  @Bean(name = "threadTaskScheduler")
  public Executor threadTaskScheduler() {
    SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
    simpleAsyncTaskExecutor.setConcurrencyLimit(2);
    return simpleAsyncTaskExecutor;
  }
}
