package demo.orders.interfaceadapter.web.dto;

import demo.orders.domain.model.Order;
import java.util.List;

public record CreateOrderResponseDto(
    String orderId,
    String customerId,
    String orderStatus,
    String inventoryResult,
    String paymentResult,
    List<OrderItemResponseDto> items) {
  public static CreateOrderResponseDto from(Order order) {
    var items =
        order.getItems().stream()
            .map(
                item ->
                    new OrderItemResponseDto(
                        item.getProductId(), item.getQuantity(), item.getPrice()))
            .toList();
    return new CreateOrderResponseDto(
        order.getId().toString(),
        order.getCustomerId(),
        order.getOrderStatus().name(),
        order.getInventoryResult().name(),
        order.getPaymentResult().name(),
        items);
  }
}
