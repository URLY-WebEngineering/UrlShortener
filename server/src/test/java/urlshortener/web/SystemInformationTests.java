package urlshortener.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SystemInformationTests {

  @Autowired private TestRestTemplate restTemplate;

  @Test
  public void testInfo() {
    ResponseEntity<String> entity = restTemplate.getForEntity("/info", String.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertNotNull(entity.getHeaders().getContentType());
    assertNotNull(entity.getBody());
  }

  @Test
  public void testMetrics() {
    ResponseEntity<String> entity = restTemplate.getForEntity("/info/metrics", String.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertNotNull(entity.getHeaders().getContentType());
    assertNotNull(entity.getBody());
  }

  @Test
  public void testDatabase() {
    ResponseEntity<String> entity = restTemplate.getForEntity("/info/database", String.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertNotNull(entity.getHeaders().getContentType());
    assertNotNull(entity.getBody());
  }
}
