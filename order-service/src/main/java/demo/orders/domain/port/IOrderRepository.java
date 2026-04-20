package demo.orders.domain.port;

import demo.orders.domain.model.Order;
import java.util.UUID;

public interface IOrderRepository {
  Order save(Order order);

  Order findById(UUID id);
}
