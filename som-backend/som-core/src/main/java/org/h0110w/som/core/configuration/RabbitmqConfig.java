package org.h0110w.som.core.configuration;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
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