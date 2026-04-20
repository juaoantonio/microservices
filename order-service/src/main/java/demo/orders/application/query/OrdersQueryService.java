package demo.orders.application.query;

import demo.orders.domain.exception.OrderNotFoundException;
import demo.orders.infrastructure.persistence.OrderJpaRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrdersQueryService {
  private final OrderJpaRepository orderRepository;

  @Transactional(readOnly = true)
  public Page<OrderSummaryQueryResult> getOrders(Pageable pageable) {
    return this.orderRepository.findAll(pageable)
        .map(OrderSummaryQueryResult::from);
  }

  @Transactional(readOnly = true)
  public OrderDetailsQueryResult getOrderById(String orderId) {
    var id = UUID.fromString(orderId);
    return this.orderRepository.findOneById(id)
        .map(OrderDetailsQueryResult::from)
        .orElseThrow(() -> new OrderNotFoundException(orderId));
  }
}
