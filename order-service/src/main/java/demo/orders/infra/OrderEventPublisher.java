package demo.orders.infra;

import demo.orders.domain.OrderCreatedEvent;
import demo.orders.domain.OrderSubmittedEvent;
import demo.orders.rabbitmq.RabbitMQConfig;
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
    rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_SUBMITTED_KEY, order);
  }
}
