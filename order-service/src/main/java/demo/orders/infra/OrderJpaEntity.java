package demo.orders.infra;

import demo.orders.domain.InventoryResult;
import demo.orders.domain.OrderStatus;
import demo.orders.domain.PaymentResult;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Entity(name = "orders")
@Table(name = "orders")
@NoArgsConstructor
public class OrderJpaEntity {
    @Id
    private UUID id;

    @Column(name = "customer_id")
    private String customerId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItemJpaEntity> items;

    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "payment_result")
    private PaymentResult paymentResult;

    @Column(name = "inventory_result")
    private InventoryResult inventoryResult;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    OrderJpaEntity(
            UUID id,
            String customerId,
            List<OrderItemJpaEntity> items,
            OrderStatus orderStatus,
            PaymentResult paymentResult,
            InventoryResult inventoryResult,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.orderStatus = orderStatus;
        this.paymentResult = paymentResult;
        this.inventoryResult = inventoryResult;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
