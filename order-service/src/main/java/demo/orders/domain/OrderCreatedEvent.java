package demo.orders.domain;

import java.time.Instant;

public record OrderCreatedEvent(
    String orderId,
    String customerId,
    Instant createdAt,
    Instant updatedAt
) {
  public static OrderCreatedEvent from(Order order) {
    return new OrderCreatedEvent(
        order.getId().toString(),
        order.getCustomerId(),
        order.getCreatedAt(),
        order.getUpdatedAt()
    );
  }
}
