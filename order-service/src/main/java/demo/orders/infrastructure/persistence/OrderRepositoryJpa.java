package demo.orders.infrastructure.persistence;

import demo.orders.domain.exception.OrderNotFoundException;
import demo.orders.domain.model.Order;
import demo.orders.domain.port.IOrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryJpa implements IOrderRepository {
  private final JpaOrderRepository jpaOrderRepository;
  private final OrderJpaEntityMapper orderJpaEntityMapper;

  @Override
  public Order save(Order order) {
    var jpaOrder = this.orderJpaEntityMapper.toOrderJpaEntity(order);
    var savedJpaOrder = this.jpaOrderRepository.save(jpaOrder);
    return this.orderJpaEntityMapper.toOrder(savedJpaOrder);
  }

  @Override
  public Order findById(UUID id) {
    var jpaOrder = this.jpaOrderRepository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(id.toString()));
    return this.orderJpaEntityMapper.toOrder(jpaOrder);
  }
}
