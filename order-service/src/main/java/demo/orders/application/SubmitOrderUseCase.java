package demo.orders.application;

import demo.orders.domain.Order;
import demo.orders.domain.OrderSubmittedEvent;
import demo.orders.repository.OrderRepository;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubmitOrderUseCase {
  private final OrderRepository orderRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public Order submitOrder(String orderId) {
    var order = this.orderRepository.findById(UUID.fromString(orderId))
            .orElseThrow(() -> new RuntimeException("Order not found"));
    order.submit();
    var savedOrder = this.orderRepository.save(order);
    this.eventPublisher.publishEvent(OrderSubmittedEvent.from(savedOrder));
    return savedOrder;
  }
}
