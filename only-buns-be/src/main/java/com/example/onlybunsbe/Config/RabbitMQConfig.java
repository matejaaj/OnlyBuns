package com.example.onlybunsbe.Config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "post-exchange";
    public static final String QUEUE_NAME1 = "ad-queue-1";
    public static final String QUEUE_NAME2 = "ad-queue-2";

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_NAME);  // Fanout exchange šalje svima
    }

    @Bean
    public Queue queue1() {
        return new Queue(QUEUE_NAME1);  // Prvi queue
    }

    @Bean
    public Queue queue2() {
        return new Queue(QUEUE_NAME2);  // Drugi queue
    }

    @Bean
    public Binding binding1(Queue queue1, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue1).to(fanoutExchange);  // Veže prvi queue za exchange
    }

    @Bean
    public Binding binding2(Queue queue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue2).to(fanoutExchange);  // Veže drugi queue za exchange
    }
}
