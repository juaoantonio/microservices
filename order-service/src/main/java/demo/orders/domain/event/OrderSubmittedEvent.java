package demo.orders.domain.event;

import demo.orders.domain.model.Order;
import java.time.Instant;

public record OrderSubmittedEvent(
    String orderId,
    String customerId,
    Instant createdAt,
    Instant updatedAt
) implements IEvent {
  public static OrderSubmittedEvent from(Order order) {
    return new OrderSubmittedEvent(
        order.getId().toString(),
        order.getCustomerId(),
        order.getCreatedAt(),
        order.getUpdatedAt()
    );
  }
}
