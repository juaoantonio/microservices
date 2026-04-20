package demo.orders.interfaceadapter.web.dto;

import demo.orders.application.command.CreateOrderCommand;
import demo.orders.application.command.CreateOrderItemCommand;
import java.util.List;

public record CreateOrderRequestDto(String customerId, List<CreateOrderItemRequestDto> items) {
  public static CreateOrderCommand toCommand(CreateOrderRequestDto request) {
    var items = request.items().stream()
        .map(item -> new CreateOrderItemCommand(item.productId(), item.quantity(), item.price()))
        .toList();
    return new CreateOrderCommand(request.customerId(), items);
  }
}
