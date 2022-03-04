package org.h0110w.som.test_service.config;

import lombok.extern.slf4j.Slf4j;
import org.h0110w.som.commons.dtos.Message;
import org.h0110w.som.test_service.HelloService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RabbitListener(queues = "helloQueue")
public class Listener {
    private final HelloService helloService;

    public Listener(HelloService helloService) {
        this.helloService = helloService;
    }

    @RabbitHandler
    public void processMessage(Message message){
        log.info("test service listener got message:" +message);
    }
}
