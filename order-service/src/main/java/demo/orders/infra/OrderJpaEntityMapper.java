package demo.orders.infra;

import demo.orders.domain.Order;
import demo.orders.domain.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderJpaEntityMapper {
    OrderJpaEntity toOrderJpaEntity(Order order) {
        return new OrderJpaEntity(
                order.getId(),
                order.getCustomerId(),
                toOrderItemJpaEntities(order.getItems()),
                order.getOrderStatus(),
                order.getPaymentResult(),
                order.getInventoryResult(),
                order.getCreatedAt(),
                order.getUpdatedAt());
    }

    Order toOrder(OrderJpaEntity orderJpaEntity) {
        return Order.restore(
                orderJpaEntity.getId(),
                orderJpaEntity.getCustomerId(),
                toOrderItems(orderJpaEntity.getItems()),
                orderJpaEntity.getOrderStatus(),
                orderJpaEntity.getPaymentResult(),
                orderJpaEntity.getInventoryResult(),
                orderJpaEntity.getCreatedAt(),
                orderJpaEntity.getUpdatedAt());
    }

    private List<OrderItemJpaEntity> toOrderItemJpaEntities(List<OrderItem> items) {
        return items.stream()
                .map(this::toOrderItemJpaEntity)
                .toList();
    }

    private OrderItemJpaEntity toOrderItemJpaEntity(OrderItem item) {
        return new OrderItemJpaEntity(
                item.getProductId(),
                item.getQuantity(),
                item.getPrice());
    }

    private List<OrderItem> toOrderItems(List<OrderItemJpaEntity> items) {
        return items.stream()
                .map(this::toOrderItem)
                .toList();
    }

    private OrderItem toOrderItem(OrderItemJpaEntity item) {
        return OrderItem.restore(
                item.getProductId(),
                item.getQuantity(),
                item.getPrice());
    }
}
