package demo.orders.application.query;

import demo.orders.infrastructure.persistence.OrderItemJpaEntity;
import java.math.BigDecimal;

public record OrderItemQueryResult(
    String productId,
    int quantity,
    BigDecimal price
) {
  static OrderItemQueryResult from(OrderItemJpaEntity item) {
    return new OrderItemQueryResult(item.getProductId(), item.getQuantity(), item.getPrice());
  }
}
