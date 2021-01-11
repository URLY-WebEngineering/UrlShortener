package urlshortener.web;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class SystemInformationTests {

  @Autowired private TestRestTemplate restTemplate;
  @Mock private RabbitTemplate template;
  @Mock private DirectExchange direct;

  @InjectMocks private SystemInformationController systemInformation;

  @Before
  public void setup() {

    this.template = Mockito.mock(RabbitTemplate.class);
    this.direct = new DirectExchange("tut.direct");
    MockitoAnnotations.initMocks(this);
  }

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

  @Test
  public void testQueues() {
    String numberData = "150";

    assertThatCode(
            () -> this.template.convertAndSend(direct.getName(), "responses_url", numberData))
        .doesNotThrowAnyException();

    Mockito.verify(this.template)
        .convertAndSend(eq(direct.getName()), eq("responses_url"), eq(numberData));

    assertThatCode(
            () -> this.template.convertAndSend(direct.getName(), "responses_click", numberData))
        .doesNotThrowAnyException();

    Mockito.verify(this.template)
        .convertAndSend(eq(direct.getName()), eq("responses_click"), eq(numberData));

    assertThatCode(
            () -> this.template.convertAndSend(direct.getName(), "responses_user", numberData))
        .doesNotThrowAnyException();

    Mockito.verify(this.template)
        .convertAndSend(eq(direct.getName()), eq("responses_user"), eq(numberData));

    assertThatCode(
            () -> this.template.convertAndSend(direct.getName(), "responses_queue", numberData))
        .doesNotThrowAnyException();

    Mockito.verify(this.template)
        .convertAndSend(eq(direct.getName()), eq("responses_queue"), eq(numberData));
  }

  @Test
  public void coveringTest() {
    String numberData = "150";

    this.systemInformation.checkSystemInformation();

    this.systemInformation.requestUpdate();

    this.systemInformation.receiveUsers(numberData);

    this.systemInformation.receiveUrl(numberData);

    this.systemInformation.receiveClick(numberData);

    this.systemInformation.receiveDone(numberData);

    AtomicInteger ParsedData = this.systemInformation.parseMessage(numberData);
    assertEquals(ParsedData.get(), 150);
  }
}
