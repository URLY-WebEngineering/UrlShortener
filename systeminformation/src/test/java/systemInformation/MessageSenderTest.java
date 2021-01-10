package systemInformation;
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
    public void testQueue() {
        assertThatCode(() -> this.rabbitTemplateMock.convertAndSend(direct.getName(), "request_queue", "send information")).doesNotThrowAnyException();

        Mockito.verify(this.rabbitTemplateMock)
                .convertAndSend(eq(direct.getName()), eq("request_queue"), eq("send information"));
    }
}