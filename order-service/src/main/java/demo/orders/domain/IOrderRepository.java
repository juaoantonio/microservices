package demo.orders.domain;

import java.util.UUID;

public interface IOrderRepository {
  Order save(Order order);

  Order findById(UUID id);
}
