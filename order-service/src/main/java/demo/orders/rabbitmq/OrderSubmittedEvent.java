package demo.orders.rabbitmq;

import demo.orders.domain.Order;
import java.time.Instant;

public record OrderSubmittedEvent(
    String orderId,
    String customerId,
    Instant createdAt,
    Instant updatedAt
) {
  public static OrderSubmittedEvent from(Order order) {
    return new OrderSubmittedEvent(
        order.getId().toString(),
        order.getCustomerId(),
        order.getCreatedAt(),
        order.getUpdatedAt()
    );
  }
}
