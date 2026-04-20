package demo.orders.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "order_items")
@Table(name = "order_items")
@NoArgsConstructor
@Getter
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

   private String productId;
   private int quantity;
   private BigDecimal price;

  private OrderItem(String productId, int quantity, BigDecimal price) {
    this.productId = productId;
    this.quantity = quantity;
    this.price = price;
  }

  public static OrderItem create(String productId, int quantity, BigDecimal price) {
    return new OrderItem(productId, quantity, price);
  }
}
