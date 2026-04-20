package demo.orders.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Order {
    private UUID id;
    private String customerId;
    private List<OrderItem> items;
    private OrderStatus orderStatus;
    private PaymentResult paymentResult;
    private InventoryResult inventoryResult;
    private Instant createdAt;
    private Instant updatedAt;

    private Order(
            String customerId,
            List<OrderItem> items,
            OrderStatus orderStatus,
            PaymentResult paymentResult,
            InventoryResult inventoryResult) {
        this.id = UUID.randomUUID();
        this.items = new ArrayList<>(items);
        this.customerId = customerId;
        this.orderStatus = orderStatus;
        this.paymentResult = paymentResult;
        this.inventoryResult = inventoryResult;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    private Order(
            UUID id,
            String customerId,
            List<OrderItem> items,
            OrderStatus orderStatus,
            PaymentResult paymentResult,
            InventoryResult inventoryResult,
            Instant createdAt,
            Instant updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.orderStatus = orderStatus;
        this.paymentResult = paymentResult;
        this.inventoryResult = inventoryResult;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    protected Order() {
    }

    public static Order create(String customerId, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter pelo menos um item");
        }
        return new Order(
                customerId, items, OrderStatus.CREATED, PaymentResult.UNKNOWN, InventoryResult.UNKNOWN);
    }

    public static Order restore(
            UUID id,
            String customerId,
            List<OrderItem> items,
            OrderStatus orderStatus,
            PaymentResult paymentResult,
            InventoryResult inventoryResult,
            Instant createdAt,
            Instant updatedAt) {
        return new Order(id, customerId, items, orderStatus, paymentResult, inventoryResult, createdAt, updatedAt);
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
