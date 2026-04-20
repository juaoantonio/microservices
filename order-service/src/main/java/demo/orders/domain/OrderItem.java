package demo.orders.domain;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderItem {
    private final String productId;
    private final int quantity;
    private final BigDecimal price;

    private OrderItem(String productId, int quantity, BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderItem create(String productId, int quantity, BigDecimal price) {
        return new OrderItem(productId, quantity, price);
    }

    public static OrderItem restore( String productId, int quantity, BigDecimal price) {
        return new OrderItem(productId, quantity, price);
    }
}
