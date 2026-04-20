package demo.orders.application.usecase;

import demo.orders.application.command.SubmitOrderCommand;
import demo.orders.domain.event.OrderSubmittedEvent;
import demo.orders.domain.model.Order;
import demo.orders.domain.port.IEventPublisher;
import demo.orders.domain.port.IOrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubmitOrderUseCase {
  private final IOrderRepository orderRepository;
  private final IEventPublisher eventPublisher;

  @Transactional
  public Order submitOrder(SubmitOrderCommand command) {
    var order = this.orderRepository.findById(UUID.fromString(command.orderId()));
    order.submit();
    var savedOrder = this.orderRepository.save(order);
    this.eventPublisher.publishEvent(OrderSubmittedEvent.from(savedOrder));
    return savedOrder;
  }
}
