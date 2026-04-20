package demo.orders.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import demo.orders.application.command.SubmitOrderCommand;
import demo.orders.domain.event.OrderSubmittedEvent;
import demo.orders.domain.model.Order;
import demo.orders.domain.model.OrderItem;
import demo.orders.domain.model.OrderStatus;
import demo.orders.testutils.InMemoryEventPublisher;
import demo.orders.testutils.InMemoryOrderRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubmitOrderUseCaseTest {
  private final InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
  private final InMemoryEventPublisher eventPublisher = new InMemoryEventPublisher();
  private final SubmitOrderUseCase useCase = new SubmitOrderUseCase(orderRepository, eventPublisher);

  @Test
  @DisplayName("Deve submeter pedido e retornar apenas o id")
  void shouldSubmitOrderAndReturnOnlyOrderId() {
    var order = createOrder();
    orderRepository.save(order);
    var command = new SubmitOrderCommand(order.getId().toString());

    var orderId = useCase.submitOrder(command);

    assertEquals(order.getId().toString(), orderId);
    assertEquals(OrderStatus.SUBMITTED, orderRepository.savedOrder().getOrderStatus());
  }

  @Test
  @DisplayName("Deve publicar evento de pedido submetido")
  void shouldPublishOrderSubmittedEvent() {
    var order = createOrder();
    orderRepository.save(order);
    var command = new SubmitOrderCommand(order.getId().toString());

    var orderId = useCase.submitOrder(command);

    assertEquals(1, eventPublisher.events().size());
    var event = assertInstanceOf(OrderSubmittedEvent.class, eventPublisher.events().getFirst());
    assertEquals(orderId, event.orderId());
    assertEquals(order.getCustomerId(), event.customerId());
  }

  private Order createOrder() {
    return Order.create(
        UUID.randomUUID().toString(),
        List.of(OrderItem.create(UUID.randomUUID().toString(), 2, BigDecimal.TEN)));
  }
}
