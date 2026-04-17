package demo.orders.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Order {
    @Getter private String id;
    @Getter private String customerId;
    @Getter private List<OrderItem> items;
    @Getter private OrderStatus orderStatus;
    @Getter private OrderPaymentStatus paymentStatus;
    @Getter private OrderInventoryStatus inventoryStatus;
    @Getter private Instant createdAt;
    @Getter private Instant updatedAt;

    private Order(
            String customerId,
            List<OrderItem> items,
            OrderStatus orderStatus,
            OrderPaymentStatus paymentStatus,
            OrderInventoryStatus inventoryStatus
    ) {
        this.id = UUID.randomUUID().toString();
        this.items = items;
        this.customerId = customerId;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.inventoryStatus = inventoryStatus;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public static Order create(String customerId, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter pelo menos um item");
        }
        return new Order(customerId, items, OrderStatus.CREATED, OrderPaymentStatus.PAYMENT_PENDING, OrderInventoryStatus.INVENTORY_PENDING);
    }

    public void addItems(List<OrderItem> items) {
        if (!(this.orderStatus == OrderStatus.CREATED)) {
            throw new IllegalStateException("Não é possível adicionar items quando o pedido já está em para processo de pagamento");
        }
        this.items.addAll(items);
        this.updatedAt = Instant.now();
    }


    public void submit() {
        if (this.getTotalValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Não é possível submeter um pedido com valor total igual ou menor que zero");
        }
        this.orderStatus = OrderStatus.SUBMITTED;
        this.paymentStatus = OrderPaymentStatus.PAYMENT_PENDING;
        this.inventoryStatus = OrderInventoryStatus.INVENTORY_PENDING;
        this.updatedAt = Instant.now();
    }

    public void confirmPayment() {
        if (!(this.orderStatus == OrderStatus.SUBMITTED && this.paymentStatus == OrderPaymentStatus.PAYMENT_PENDING)) {
            throw new IllegalStateException("Não é possível confirmar o pagamento de um pedido que ainda não iniciou o processo de pagamento");
        }
        this.paymentStatus = OrderPaymentStatus.PAYMENT_APPROVED;
        this.updatedAt = Instant.now();
    }

    public BigDecimal getTotalValue() {
        return items.stream()
                .map((orderItem) -> {
                    return orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void completeOrder() {
        if (!(this.orderStatus == OrderStatus.SUBMITTED && this.paymentStatus == OrderPaymentStatus.PAYMENT_APPROVED)) {
            throw new IllegalStateException("Não é possível completar um pedido que ainda não teve o pagamento aprovado");
        }
        this.orderStatus = OrderStatus.COMPLETED;
        this.updatedAt = Instant.now();
    }
}
