package demo.orders.infra;

import demo.orders.domain.IOrderRepository;
import demo.orders.domain.Order;
import demo.orders.domain.OrderNotFoundException;
import demo.orders.repository.JpaOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class OrderRepositoryImpl implements IOrderRepository {
  private final JpaOrderRepository jpaOrderRepository;

  public OrderRepositoryImpl(JpaOrderRepository jpaOrderRepository) {
    this.jpaOrderRepository = jpaOrderRepository;
  }

  @Override
  public Order save(Order order) {
    return jpaOrderRepository.save(order);
  }

  @Override
  public Order findById(UUID id) {
    return jpaOrderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id.toString()));
  }
}
