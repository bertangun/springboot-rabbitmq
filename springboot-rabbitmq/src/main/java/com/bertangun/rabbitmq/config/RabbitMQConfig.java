package com.bertangun.rabbitmq.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQConfig {


    //Dynamically pass rabbitMQ queue name to Queue(queue) object
    @Value("${rabbitmq.queue.name}")
    private String queue;

    //Dynamically pass rabbitMQ json queue name to queue01_json object
    @Value("${rabbitmq.queue.json.name}")
    private String jsonQueue;

    //Dynamically pass rabbitMQ exchange name to TopicExchange(exchange) object
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    //Dynamically pass rabbitMQ routing key to .with(routingKey) object
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    //Dynamically pass rabbitMQ json routing key to .with(routingJsonKey) object
    @Value("${rabbitmq.routing.json.key}")
    private String routingJsonKey;

    // spring bean for rabbitMQ queue
    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    // spring bean for rabbitMQ json queue(store json messages)
    @Bean
    public Queue jsonQueue(){
        return new Queue(jsonQueue);
    }

    // spring bean for rabbitMQ exchange
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    // binding between queue and exchange using routing key
    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
    }

    // binding between json queue and exchange using json routing key
    @Bean
    public Binding jsonBinding(){
        return BindingBuilder
                .bind(jsonQueue())
                .to(exchange())
                .with(routingJsonKey);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}

// Spring boot auto configuration will automatically configure 3 beans below for us.
// (ConnectionFactory, RabbitTemplate, RabbitAdmin)
// We don't have to explicitly create spring bean for these 3 classes
//But for the json queue, we implemented MessageConverter and rabbitTemplate or AmqpTemplate


