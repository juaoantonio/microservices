package demo.orders.application.query;

import demo.orders.infrastructure.persistence.OrderJpaEntity;
import java.time.Instant;
import java.util.List;

public record OrderDetailsQueryResult(
    String orderId,
    String customerId,
    String orderStatus,
    String inventoryResult,
    String paymentResult,
    Instant createdAt,
    Instant updatedAt,
    List<OrderItemQueryResult> items
) {
  static OrderDetailsQueryResult from(OrderJpaEntity order) {
    var items = order.getItems().stream()
        .map(OrderItemQueryResult::from)
        .toList();
    return new OrderDetailsQueryResult(
        order.getId().toString(),
        order.getCustomerId(),
        order.getOrderStatus().name(),
        order.getInventoryResult().name(),
        order.getPaymentResult().name(),
        order.getCreatedAt(),
        order.getUpdatedAt(),
        items);
  }
}
