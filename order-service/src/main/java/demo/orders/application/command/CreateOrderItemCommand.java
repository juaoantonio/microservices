package demo.orders.application.command;

import java.math.BigDecimal;

public record CreateOrderItemCommand(
    String productId,
    int quantity,
    BigDecimal price
) {
}
