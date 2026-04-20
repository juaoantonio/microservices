package demo.orders.dto;

import java.math.BigDecimal;

public record CreateOrderItemRequestDto(
        String productId,
        int quantity,
        BigDecimal price
)  {
}
