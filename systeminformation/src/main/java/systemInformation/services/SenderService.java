package systemInformation.services;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;


@Configuration
@EnableAsync
@Component
public class SenderService {


    // Bind the process to the queues
    // A binding is a relationship between an exchange and a queue
    @Bean
    public Binding bindingRequest(DirectExchange direct,Queue ResponsesRequest) {
        return BindingBuilder.bind(ResponsesRequest).to(direct).with("request_queue");
    }

    @Bean
    public Queue ResponsesRequest() {
        return new Queue("request_queue");
    }

    private RabbitTemplate template;

    private DirectExchange direct;

    private AccessService accessData;

    public SenderService(RabbitTemplate template,DirectExchange direct,AccessService accessData){
        this.direct = direct;
        this.template = template;
        this.accessData = accessData;
    }

    @Async
    @RabbitListener(queues = "request_queue")
    public void listenRequest(String in) {
        sendUrl();
        sendClick();
        sendUser();
    }

    @Async
    public void sendUrl() {
        template.convertAndSend(direct.getName(), "responses_url", accessData.getTotalURL().toString());
    }

    @Async
    public void sendClick() {
        template.convertAndSend(direct.getName(), "responses_click", accessData.getTotalClick().toString());
    }

    @Async
    public void sendUser() {
        template.convertAndSend(direct.getName(), "responses_user", "800");
    }
}


