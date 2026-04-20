package demo.orders.rabbitmq;

import demo.orders.domain.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {
  private final RabbitTemplate rabbitTemplate;

  public OrderEventPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishOrderCreated(Order order) {
    rabbitTemplate.convertAndSend(RabbitMQConfig.ORDERS_EXCHANGE, RabbitMQConfig.ORDER_CREATED_KEY, order);
  }
}
