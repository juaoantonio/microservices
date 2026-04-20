package demo.orders.testutils;

import demo.orders.domain.model.Order;
import demo.orders.domain.port.IOrderRepository;
import java.util.UUID;

public class InMemoryOrderRepository implements IOrderRepository {
  private Order savedOrder;

  @Override
  public Order save(Order order) {
    this.savedOrder = order;
    return order;
  }

  @Override
  public Order findById(UUID id) {
    if (savedOrder == null || !savedOrder.getId().equals(id)) {
      throw new IllegalArgumentException("Order not found");
    }
    return savedOrder;
  }

  public Order savedOrder() {
    return savedOrder;
  }
}
