package demo.orders.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  public static final String ORDERS_EXCHANGE = "orders.events";
  public static final String ORDER_CREATED_KEY = "order.created";
  public static final String ORDER_SUBMITTED_KEY = "order.submitted";

  @Bean
  TopicExchange ordersExchange() {
    return new TopicExchange(ORDERS_EXCHANGE, true, false);
  }

  @Bean
  JacksonJsonMessageConverter jacksonJsonMessageConverter() {
    return new JacksonJsonMessageConverter();
  }
}

