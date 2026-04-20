package demo.orders.infra;

import demo.orders.domain.IOrderRepository;
import demo.orders.domain.Order;
import demo.orders.domain.OrderNotFoundException;
import demo.orders.repository.JpaOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

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
