package org.h0110w.som.test_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {
    @Bean
    public Queue helloQueue() {
        return new Queue("helloQueue");
    }


    @Bean
    DirectExchange exchange() {
        return new DirectExchange("exchange");
    }

    @Bean
    Binding binding(Queue requestHelloQueue, DirectExchange exchange){
        return BindingBuilder.bind(requestHelloQueue).to(exchange).with("hello");
    }
}
