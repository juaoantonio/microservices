package demo.orders.application;

import demo.orders.domain.OrderCreatedEvent;
import demo.orders.dto.CreateOrderItemRequestDto;
import demo.orders.domain.Order;
import demo.orders.domain.OrderItem;
import demo.orders.repository.OrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {
  private final OrderRepository orderRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public Order createOrder(String customerId, List<CreateOrderItemRequestDto> orderItensDto) {
    var items = orderItensDto.stream()
            .map(item -> OrderItem.create(item.productId(), item.quantity(), item.price()))
            .toList();
    var order = Order.create(customerId, items);
    var savedOrder = this.orderRepository.save(order);
    this.eventPublisher.publishEvent(OrderCreatedEvent.from(savedOrder));
    return savedOrder;
  }
}
