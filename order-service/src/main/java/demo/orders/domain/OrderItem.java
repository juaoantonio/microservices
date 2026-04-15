package demo.orders.domain;

import lombok.Getter;

import java.math.BigDecimal;

public class OrderItem {
    @Getter private String productId;
    @Getter private int quantity;
    @Getter private BigDecimal price;

    private OrderItem(String productId, int quantity, BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderItem create(String productId, int quantity, BigDecimal price) {
        return new OrderItem(productId, quantity, price);
    }
}
