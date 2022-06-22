package org.h0110w.som.core.service;


import lombok.extern.slf4j.Slf4j;
import org.h0110w.som.commons.dtos.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitTestProducer {
    private final RabbitTemplate rabbitTemplate;
    private final Queue requestHelloQueue;

    public RabbitTestProducer(RabbitTemplate rabbitTemplate, Queue requestHelloQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.requestHelloQueue = requestHelloQueue;
        log.info("sender initialized");
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 500)
    public void send(){
        rabbitTemplate.convertAndSend(requestHelloQueue.getName(), new Message("hello"));
        log.info(this.getClass().getName()+" sent hello message");
    }
}