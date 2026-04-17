package demo.orders.domain;

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
        assertEquals(OrderPaymentStatus.PAYMENT_PENDING, order.getPaymentStatus());
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
    @DisplayName("Deve enviar o pedido para processamento")
    void test_4() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());

        order.submit();
        assertEquals(OrderStatus.SUBMITTED, order.getOrderStatus());
        assertEquals(OrderPaymentStatus.PAYMENT_PENDING, order.getPaymentStatus());
    }

    @Test
    @DisplayName("Deve confirmar o pagamento do pedido após iniciar o processo de pagamento do pedido")
    void test_6() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId,  createDefaultOrderItems());
        order.submit();
        order.confirmPayment();
        assertEquals(OrderStatus.SUBMITTED, order.getOrderStatus());
        assertEquals(OrderPaymentStatus.PAYMENT_APPROVED, order.getPaymentStatus());
    }

    @Test
    @DisplayName("Não deve confirmar o pagamento de um pedido se o processo de pagamento ainda não foi iniciado")
    void test_7() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());
        var exception = assertThrows(IllegalStateException.class, order::confirmPayment);
        assertEquals("Não é possível confirmar o pagamento de um pedido que ainda não iniciou o processo de pagamento", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve permitir adicionar items a um pedido com status diferente de CREATED")
    void test_8() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());
        var newOrderItems = List.of(OrderItem.create(UUID.randomUUID().toString(), 2, BigDecimal.valueOf(10.0)));

        order.submit();
        order.confirmPayment();
        var exception = assertThrows(IllegalStateException.class, () -> {
            order.addItems(newOrderItems);
        });
        assertEquals("Não é possível adicionar items quando o pedido já está em para processo de pagamento", exception.getMessage());
    }

    @Test
    @DisplayName("Deve ser possível completar o pedido após o pagamento ser aprovado")
    void test_9() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());
        order.submit();
        order.confirmPayment();
        order.completeOrder();
        assertEquals(OrderStatus.COMPLETED, order.getOrderStatus());
        assertEquals(OrderPaymentStatus.PAYMENT_APPROVED, order.getPaymentStatus());
    }

    @Test
    @DisplayName("Não deve ser possível completar o pedido se o pagamento ainda não foi aprovado")
    void test_10() {
        String customerId = UUID.randomUUID().toString();
        var order = Order.create(customerId, createDefaultOrderItems());
        var exception = assertThrows(IllegalStateException.class, () -> {
            order.completeOrder();
        });
        assertEquals("Não é possível completar um pedido que ainda não teve o pagamento aprovado", exception.getMessage());
    }
}