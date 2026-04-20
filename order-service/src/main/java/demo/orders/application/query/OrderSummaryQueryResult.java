package demo.orders.application.query;

import demo.orders.infrastructure.persistence.OrderJpaEntity;
import java.time.Instant;

public record OrderSummaryQueryResult(
    String orderId,
    String customerId,
    String orderStatus,
    String inventoryResult,
    String paymentResult,
    Instant createdAt,
    Instant updatedAt
) {
  static OrderSummaryQueryResult from(OrderJpaEntity order) {
    return new OrderSummaryQueryResult(
        order.getId().toString(),
        order.getCustomerId(),
        order.getOrderStatus().name(),
        order.getInventoryResult().name(),
        order.getPaymentResult().name(),
        order.getCreatedAt(),
        order.getUpdatedAt());
  }
}
