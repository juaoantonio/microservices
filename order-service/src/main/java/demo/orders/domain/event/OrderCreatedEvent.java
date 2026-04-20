package demo.orders.domain.event;

import demo.orders.domain.model.Order;
import java.time.Instant;

public record OrderCreatedEvent(
    String orderId,
    String customerId,
    Instant createdAt,
    Instant updatedAt
) implements IEvent {
  public static OrderCreatedEvent from(Order order) {
    return new OrderCreatedEvent(
        order.getId().toString(),
        order.getCustomerId(),
        order.getCreatedAt(),
        order.getUpdatedAt()
    );
  }
}
