package demo.orders.application;

import demo.orders.dto.CreateOrderItemRequestDto;
import demo.orders.domain.Order;
import demo.orders.domain.OrderItem;
import demo.orders.repository.OrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {
  private final OrderRepository orderRepository;

  public Order createOrder(String customerId, List<CreateOrderItemRequestDto> orderItensDto) {
    var items = orderItensDto.stream()
            .map(item -> OrderItem.create(item.productId(), item.quantity(), item.price()))
            .toList();
    var order = Order.create(customerId, items);
    return this.orderRepository.save(order);
  }
}
