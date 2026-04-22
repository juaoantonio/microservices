package demo.orders.infrastructure.messaging;

import demo.orders.domain.event.OrderCreatedEvent;
import demo.orders.domain.event.OrderSubmittedEvent;
import demo.orders.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class OrderEventPublisher {
  private final RabbitTemplate rabbitTemplate;

  public OrderEventPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @TransactionalEventListener
  public void publishOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
    rabbitTemplate.convertAndSend(RabbitMQConfig.ORDERS_EXCHANGE, RabbitMQConfig.ORDER_CREATED_KEY, orderCreatedEvent);
  }

  @TransactionalEventListener
  public void publishOrderSubmitted(OrderSubmittedEvent order) {
    rabbitTemplate.convertAndSend(RabbitMQConfig.ORDERS_EXCHANGE, RabbitMQConfig.ORDER_SUBMITTED_KEY, order);
  }
}
