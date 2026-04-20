package demo.orders.infrastructure.persistence;

import demo.orders.domain.model.InventoryResult;
import demo.orders.domain.model.Order;
import demo.orders.domain.model.OrderItem;
import demo.orders.domain.model.OrderStatus;
import demo.orders.domain.model.PaymentResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderJpaEntityMapperTest {
    private final OrderJpaEntityMapper mapper = new OrderJpaEntityMapper();

    @Test
    @DisplayName("Deve converter pedido entre domínio e entidade JPA")
    void shouldMapOrderBetweenDomainAndJpaEntity() {
        var order = Order.create(
                UUID.randomUUID().toString(),
                List.of(OrderItem.create(UUID.randomUUID().toString(), 2, BigDecimal.TEN)));

        var entity = mapper.toOrderJpaEntity(order);
        var mappedOrder = mapper.toOrder(entity);

        assertEquals(order.getId(), mappedOrder.getId());
        assertEquals(order.getCustomerId(), mappedOrder.getCustomerId());
        assertEquals(OrderStatus.CREATED, mappedOrder.getOrderStatus());
        assertEquals(PaymentResult.UNKNOWN, mappedOrder.getPaymentResult());
        assertEquals(InventoryResult.UNKNOWN, mappedOrder.getInventoryResult());
        assertEquals(order.getCreatedAt(), mappedOrder.getCreatedAt());
        assertEquals(order.getUpdatedAt(), mappedOrder.getUpdatedAt());
        assertEquals(1, mappedOrder.getItems().size());
        assertEquals(order.getItems().getFirst().getProductId(), mappedOrder.getItems().getFirst().getProductId());
        assertEquals(order.getItems().getFirst().getQuantity(), mappedOrder.getItems().getFirst().getQuantity());
        assertEquals(order.getItems().getFirst().getPrice(), mappedOrder.getItems().getFirst().getPrice());
    }


}
