package demo.orders.application.usecase;

import demo.orders.application.command.CreateOrderCommand;
import demo.orders.domain.event.OrderCreatedEvent;
import demo.orders.domain.model.Order;
import demo.orders.domain.model.OrderItem;
import demo.orders.domain.port.IEventPublisher;
import demo.orders.domain.port.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {
  private final IOrderRepository orderRepository;
  private final IEventPublisher eventPublisher;

  @Transactional
  public Order createOrder(CreateOrderCommand command) {
    var items = command.items().stream()
            .map(item -> OrderItem.create(item.productId(), item.quantity(), item.price()))
            .toList();
    var order = Order.create(command.customerId(), items);
    var savedOrder = this.orderRepository.save(order);
    this.eventPublisher.publishEvent(OrderCreatedEvent.from(savedOrder));
    return savedOrder;
  }
}
