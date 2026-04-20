package demo.orders.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import demo.orders.application.command.CreateOrderCommand;
import demo.orders.application.command.CreateOrderItemCommand;
import demo.orders.domain.event.OrderCreatedEvent;
import demo.orders.domain.model.OrderStatus;
import demo.orders.testutils.InMemoryEventPublisher;
import demo.orders.testutils.InMemoryOrderRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateOrderUseCaseTest {
  private final InMemoryOrderRepository orderRepository = new InMemoryOrderRepository();
  private final InMemoryEventPublisher eventPublisher = new InMemoryEventPublisher();
  private final CreateOrderUseCase useCase = new CreateOrderUseCase(orderRepository, eventPublisher);

  @Test
  @DisplayName("Deve criar pedido e retornar apenas o id")
  void shouldCreateOrderAndReturnOnlyOrderId() {
    var command = new CreateOrderCommand(
        UUID.randomUUID().toString(),
        List.of(new CreateOrderItemCommand(UUID.randomUUID().toString(), 2, BigDecimal.TEN)));

    var orderId = useCase.createOrder(command);

    assertNotNull(orderId);
    assertEquals(orderRepository.savedOrder().getId().toString(), orderId);
    assertEquals(command.customerId(), orderRepository.savedOrder().getCustomerId());
    assertEquals(OrderStatus.CREATED, orderRepository.savedOrder().getOrderStatus());
    assertEquals(1, orderRepository.savedOrder().getItems().size());
    assertEquals(
        command.items().getFirst().productId(),
        orderRepository.savedOrder().getItems().getFirst().getProductId());
    assertEquals(
        command.items().getFirst().quantity(),
        orderRepository.savedOrder().getItems().getFirst().getQuantity());
    assertEquals(command.items().getFirst().price(), orderRepository.savedOrder().getItems().getFirst().getPrice());
  }

  @Test
  @DisplayName("Deve publicar evento de pedido criado")
  void shouldPublishOrderCreatedEvent() {
    var command = new CreateOrderCommand(
        UUID.randomUUID().toString(),
        List.of(new CreateOrderItemCommand(UUID.randomUUID().toString(), 1, BigDecimal.valueOf(20))));

    var orderId = useCase.createOrder(command);

    assertEquals(1, eventPublisher.events().size());
    var event = assertInstanceOf(OrderCreatedEvent.class, eventPublisher.events().getFirst());
    assertEquals(orderId, event.orderId());
    assertEquals(command.customerId(), event.customerId());
  }
}
