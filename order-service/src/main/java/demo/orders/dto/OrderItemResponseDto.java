package demo.orders.dto;

import java.math.BigDecimal;

public record OrderItemResponseDto(
        String productId,
        int quantity,
        BigDecimal price
)  {
}
