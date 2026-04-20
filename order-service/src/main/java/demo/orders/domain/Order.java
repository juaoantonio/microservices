package demo.orders.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
@Entity(name = "orders")
@Table(name = "orders")
public class Order {
  @Id private UUID id;

  @Column private String customerId;

  @OneToMany(cascade = CascadeType.ALL) private List<OrderItem> items;

  @Column private OrderStatus orderStatus;

  @Column private PaymentResult paymentResult;

  @Column private InventoryResult inventoryResult;

  @Column private Instant createdAt;

  @Column private Instant updatedAt;

  private Order(
      String customerId,
      List<OrderItem> items,
      OrderStatus orderStatus,
      PaymentResult paymentResult,
      InventoryResult inventoryResult) {
    this.id = UUID.randomUUID();
    this.items = items;
    this.customerId = customerId;
    this.orderStatus = orderStatus;
    this.paymentResult = paymentResult;
    this.inventoryResult = inventoryResult;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  protected Order() {}

  public static Order create(String customerId, List<OrderItem> items) {
    if (items == null || items.isEmpty()) {
      throw new IllegalArgumentException("O pedido deve conter pelo menos um item");
    }
    return new Order(
        customerId, items, OrderStatus.CREATED, PaymentResult.UNKNOWN, InventoryResult.UNKNOWN);
  }

  public void addItems(List<OrderItem> items) {
    if (!(this.orderStatus == OrderStatus.CREATED)) {
      throw new IllegalStateException(
          "Não é possível adicionar items quando o pedido já foi submetido para processamento");
    }
    this.items.addAll(items);
  }

  public void submit() {
    if (this.getTotalValue().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalStateException(
          "Não é possível submeter um pedido com valor total igual ou menor que zero");
    }
    this.orderStatus = OrderStatus.SUBMITTED;
    this.paymentResult = PaymentResult.UNKNOWN;
    this.inventoryResult = InventoryResult.UNKNOWN;
    this.updatedAt = Instant.now();
  }

  public void approvePayment() {
    if (!(this.orderStatus == OrderStatus.SUBMITTED
        && this.paymentResult == PaymentResult.UNKNOWN)) {
      throw new IllegalStateException(
          "Não é possível confirmar o pagamento de um pedido que ainda não foi submetido para processamento");
    }
    this.paymentResult = PaymentResult.APPROVED;
    this.updatedAt = Instant.now();
  }

  public BigDecimal getTotalValue() {
    return items.stream()
        .map(
            (orderItem) -> {
              return orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            })
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public void completeOrder() {
    if (this.orderStatus != OrderStatus.SUBMITTED) {
      throw new IllegalStateException(
          "Não é possível completar um pedido que ainda não foi submetido para processamento");
    }
    if (this.paymentResult == PaymentResult.UNKNOWN) {
      throw new IllegalStateException(
          "Não é possível completar um pedido que ainda não teve o pagamento aprovado");
    }
    if (this.inventoryResult == InventoryResult.UNKNOWN) {
      throw new IllegalStateException(
          "Não é possível completar um pedido que ainda não teve o resultado do inventário aprovado");
    }
    this.orderStatus = OrderStatus.COMPLETED;
    this.updatedAt = Instant.now();
  }

  public void approveInventory() {
    if (!(this.orderStatus == OrderStatus.SUBMITTED)) {
      throw new IllegalStateException(
          "Não é possível aprovar o resultado do inventário de um pedido que ainda não foi submetido para processamento");
    }
    if (this.paymentResult == PaymentResult.UNKNOWN) {
      throw new IllegalStateException(
          "Não é possível aprovar o resultado do inventário de um pedido que ainda não teve o pagamento aprovado");
    }
    this.inventoryResult = InventoryResult.APPROVED;
    this.updatedAt = Instant.now();
  }
}
