package com.fitnessApp.aiservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQ_Config {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    //This Declares the Queue named activity.queue in  Rabbit-MQ
    @Bean
    public Queue activityQueue() {
        return new Queue(this.queue, true);//durable means if rabbit MQ restarts then also this Message is
        // going to be there in the Queue
    }

    //Defining the Exchange with the name "fitness.exchange"
    @Bean
    public DirectExchange activityExchange() {
        return new DirectExchange(this.exchange);
    }

    //Now Binding the exchange:"fitness.exchange" with the queue :queue
    @Bean
    public Binding activityBinding(Queue activityQueue, DirectExchange activityExchange) {
        return BindingBuilder.bind(activityQueue)
                .to(activityExchange)
                .with(this.routingKey);
    }

    //This Bean will convert the JAVA objects into JSON before Sending it to the Rabbit-MQ
    @Bean
    public MessageConverter jsonMessageConvertor() {
        return new Jackson2JsonMessageConverter();
    }
}
