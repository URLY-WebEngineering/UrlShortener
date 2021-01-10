package urlshortener.service;
// https://gaddings.io/testing-spring-boot-apps-with-rabbitmq-using-testcontainers/

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class MessageSenderTest {

  private RabbitTemplate rabbitTemplateMock;
  private DirectExchange direct;

  @Before
  public void setUp() {
    this.rabbitTemplateMock = Mockito.mock(RabbitTemplate.class);
    this.direct = new DirectExchange("tut.direct");
  }

  @Test
  public void testQueueClick() {
    assertThatCode(
            () ->
                this.rabbitTemplateMock.convertAndSend(
                    direct.getName(), "responses_click", "add click"))
        .doesNotThrowAnyException();

    Mockito.verify(this.rabbitTemplateMock)
        .convertAndSend(eq(direct.getName()), eq("responses_click"), eq("add click"));
  }

  @Test
  public void testQueueUrl() {
    assertThatCode(
            () ->
                this.rabbitTemplateMock.convertAndSend(
                    direct.getName(), "responses_url", "add url"))
        .doesNotThrowAnyException();

    Mockito.verify(this.rabbitTemplateMock)
        .convertAndSend(eq(direct.getName()), eq("responses_url"), eq("add url"));
  }

  @Test
  public void testQueueUser() {
    assertThatCode(
            () ->
                this.rabbitTemplateMock.convertAndSend(
                    direct.getName(), "responses_users", "add user"))
        .doesNotThrowAnyException();

    Mockito.verify(this.rabbitTemplateMock)
        .convertAndSend(eq(direct.getName()), eq("responses_users"), eq("add user"));
  }
}
