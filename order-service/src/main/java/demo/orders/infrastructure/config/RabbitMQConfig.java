package demo.orders.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.Bidi;

@Configuration
public class RabbitMQConfig {
  public static final String PAYMENTS_EXCHANGE = "payment.events";
  public static final String INVENTORY_EXCHANGE = "payment.events";
  public static final String ORDERS_EXCHANGE = "orders.events";

  public static final String ORDER_PAYMENTS_QUEUE = "order.payments.queue";
  public static final String ORDER_INVENTORY_QUEUE = "order.inventory.queue";

  public static final String PAYMENT_APPROVED_KEY = "payment.approved";
  public static final String PAYMENT_REJECTED_KEY = "payment.rejected";

  public static final String INVENTORY_APPROVED_KEY = "inventory.approved";
  public static final String INVENTORY_REJECTED_KEY = "inventory.rejected";

  public static final String ORDER_CREATED_KEY = "order.created";
  public static final String ORDER_SUBMITTED_KEY = "order.submitted";

  @Bean
  TopicExchange ordersExchange() {
    return new TopicExchange(ORDERS_EXCHANGE, true, false);
  }

  @Bean
  TopicExchange paymentsExchange() {
    return new TopicExchange(PAYMENTS_EXCHANGE, true, false);
  }

  @Bean
  TopicExchange inventoryExchange() {
    return new TopicExchange(INVENTORY_EXCHANGE, true, false);
  }

  @Bean
  Queue orderPaymentsQueue() {
    return new Queue(ORDER_PAYMENTS_QUEUE, true);
  }

  @Bean
  Queue orderInventoryQueue() {
    return new Queue(ORDER_INVENTORY_QUEUE, true);
  }

  @Bean
  Binding paymentApprovedBinding(Queue orderPaymentsQueue, TopicExchange paymentsExchange) {
    return BindingBuilder.bind(orderPaymentsQueue).to(paymentsExchange).with(PAYMENT_APPROVED_KEY);
  }

  @Bean
  Binding paymentRejectedBinding(Queue orderPaymentsQueue, TopicExchange paymentsExchange) {
    return BindingBuilder.bind(orderPaymentsQueue).to(paymentsExchange).with(PAYMENT_REJECTED_KEY);
  }

  @Bean
  Binding inventoryApprovedBinding(Queue orderInventoryQueue, TopicExchange inventoryExchange) {
    return BindingBuilder.bind(orderInventoryQueue).to(inventoryExchange).with(INVENTORY_APPROVED_KEY);
  }

  @Bean
  Binding inventoryRejectedBinding(Queue orderInventoryQueue, TopicExchange inventoryExchange) {
    return BindingBuilder.bind(orderInventoryQueue).to(inventoryExchange).with(INVENTORY_REJECTED_KEY);
  }

  @Bean
  JacksonJsonMessageConverter jacksonJsonMessageConverter() {
    return new JacksonJsonMessageConverter();
  }
}

