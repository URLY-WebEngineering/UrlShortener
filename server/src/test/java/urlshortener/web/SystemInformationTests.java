package urlshortener.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SystemInformationTests {

  @Autowired private TestRestTemplate restTemplate;
  private RabbitTemplate template;
  private DirectExchange direct;

  @InjectMocks private SystemInformationController systemInformation;
  private MockMvc mockMvc;

  @Before
  public void setup() {

    this.template = Mockito.mock(RabbitTemplate.class);
    this.direct = new DirectExchange("tut.direct");
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(systemInformation).build();
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
  public void testQueue() {

    String numberData = "150";

    template.convertAndSend(direct.getName(), "responses_url", numberData);
    template.convertAndSend(direct.getName(), "responses_click", numberData);
    template.convertAndSend(direct.getName(), "responses_user", numberData);

    systemInformation.receiveClick(numberData);
    systemInformation.receiveUrl(numberData);
    systemInformation.receiveUsers(numberData);
  }
}
