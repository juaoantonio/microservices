package demo.orders.application;

import demo.orders.domain.*;
import demo.orders.dto.CreateOrderItemRequestDto;
import demo.orders.repository.JpaOrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {
  private final IOrderRepository orderRepository;
  private final IEventPublisher eventPublisher;

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
