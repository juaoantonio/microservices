package demo.orders.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    List<OrderItem> createDefaultOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(OrderItem.create(UUID.randomUUID().toString(), 2, BigDecimal.valueOf(10.0)));
        orderItems.add(OrderItem.create(UUID.randomUUID().toString(), 1, BigDecimal.valueOf(20.0)));
        return orderItems;
    }

    @Test
    @DisplayName("Deve criar um pedido padrão")
    void test_0() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());

        assertEquals(customerId, order.getCustomerId());
        assertEquals(OrderStatus.CREATED, order.getOrderStatus());
        assertEquals(PaymentResult.UNKNOWN, order.getPaymentResult());
        assertEquals(InventoryResult.UNKNOWN, order.getInventoryResult());
        assertNotNull(order.getId());
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
        assertEquals(2, order.getItems().size());
    }

    @Test
    @DisplayName("Não deve criar um pedido sem items")
    void test_1() {
        String customerId = UUID.randomUUID().toString();
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            Order.create(customerId, new ArrayList<>());
        });
        assertEquals("O pedido deve conter pelo menos um item", exception.getMessage());
    }

    @Test
    @DisplayName("Deve adicionar novos items do pedido")
    void test_2() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());

        var newOrderItems = List.of(OrderItem.create(UUID.randomUUID().toString(), 2, BigDecimal.valueOf(10.0)));
        order.addItems(newOrderItems);
        assertEquals(3, order.getItems().size());
    }

    @Test
    @DisplayName("Deve calcular o total do pedido")
    void test_3() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());

        BigDecimal orderTotal = order.getTotalValue();
        assertEquals(BigDecimal.valueOf(40.0), orderTotal);
    }

    @Test
    @DisplayName("Deve submeter o pedido para processamento")
    void test_4() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());

        order.submit();
        assertEquals(OrderStatus.SUBMITTED, order.getOrderStatus());
        assertEquals(PaymentResult.UNKNOWN, order.getPaymentResult());
    }

    @Test
    @DisplayName("Não deve submeter o pedido se o total for maior que zero")
    void test_5() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, List.of(OrderItem.create(UUID.randomUUID().toString(), 2, BigDecimal.valueOf(0.0))));

        var exception = assertThrows(IllegalStateException.class, () -> {
            order.submit();
        });
        assertEquals("Não é possível submeter um pedido com valor total igual ou menor que zero", exception.getMessage());
    }

    @Test
    @DisplayName("Deve confirmar o pagamento do pedido após submeter o pedido para processamento")
    void test_6() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId,  createDefaultOrderItems());
        order.submit();
        order.approvePayment();
        assertEquals(OrderStatus.SUBMITTED, order.getOrderStatus());
        assertEquals(PaymentResult.APPROVED, order.getPaymentResult());
    }

    @Test
    @DisplayName("Não deve aprovar o pagamento de um pedido se pedido ainda não foi submetido para processamento")
    void test_7() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());
        var exception = assertThrows(IllegalStateException.class, order::approvePayment);
        assertEquals("Não é possível confirmar o pagamento de um pedido que ainda não foi submetido para processamento", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve permitir adicionar items a um pedido com status diferente de CREATED")
    void test_8() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());
        var newOrderItems = List.of(OrderItem.create(UUID.randomUUID().toString(), 2, BigDecimal.valueOf(10.0)));

        order.submit();
        var exception = assertThrows(IllegalStateException.class, () -> {
            order.addItems(newOrderItems);
        });
        assertEquals("Não é possível adicionar items quando o pedido já foi submetido para processamento", exception.getMessage());
    }

    @Test()
    @DisplayName("Deve permitir aprovar o resultado do inventário após o pagamento ser aprovado")
    void test_11() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());
        order.submit();
        order.approvePayment();
        order.approveInventory();
        assertEquals(OrderStatus.SUBMITTED, order.getOrderStatus());
        assertEquals(PaymentResult.APPROVED, order.getPaymentResult());
        assertEquals(InventoryResult.APPROVED, order.getInventoryResult());
    }

    @Test
    @DisplayName("Não deve permitir aprovar o resultado do inventário se o pagamento ainda não foi aprovado")
    void test_12() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());
        order.submit();
        var exception = assertThrows(IllegalStateException.class, order::approveInventory);
        assertEquals("Não é possível aprovar o resultado do inventário de um pedido que ainda não teve o pagamento aprovado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve ser possível completar o pedido após o resultado do processamento do pagamento e do inventário serem aprovados")
    void test_9() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());
        order.submit();
        order.approvePayment();
        order.approveInventory();
        order.completeOrder();
        assertEquals(OrderStatus.COMPLETED, order.getOrderStatus());
        assertEquals(PaymentResult.APPROVED, order.getPaymentResult());
    }

    @Test
    @DisplayName("Não deve ser possível completar o pedido se o resultado do processamento do inventário ainda não foi aprovado")
    void test_13() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());
        order.submit();
        order.approvePayment();
        var exception = assertThrows(IllegalStateException.class, () -> {
            order.completeOrder();
        });
        assertEquals("Não é possível completar um pedido que ainda não teve o resultado do inventário aprovado", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve ser possível completar o pedido se o pedido ainda não foi submetido para processamento")
    void test_10() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());
        var exception = assertThrows(IllegalStateException.class, () -> {
            order.completeOrder();
        });
        assertEquals("Não é possível completar um pedido que ainda não foi submetido para processamento", exception.getMessage());
    }
}