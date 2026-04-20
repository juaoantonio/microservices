package demo.orders.interfaceadapter.web;

import demo.orders.application.command.CreateOrderCommand;
import demo.orders.application.command.CreateOrderItemCommand;
import demo.orders.application.command.SubmitOrderCommand;
import demo.orders.application.usecase.CreateOrderUseCase;
import demo.orders.application.usecase.SubmitOrderUseCase;
import demo.orders.interfaceadapter.web.dto.CreateOrderRequestDto;
import demo.orders.interfaceadapter.web.dto.CreateOrderResponseDto;
import demo.orders.interfaceadapter.web.dto.SubmitOrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class OrderController {
  private final CreateOrderUseCase createOrderUseCase;
  private final SubmitOrderUseCase submitOrderUseCase;

  @PostMapping
  public ResponseEntity<CreateOrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto request) {
    var order = this.createOrderUseCase.createOrder(toCommand(request));
    return ResponseEntity.ok(CreateOrderResponseDto.from(order));
  }

  @PatchMapping("/{orderId}/submit")
  public ResponseEntity<SubmitOrderResponseDto> submitOrder(@PathVariable String orderId) {
    var order = this.submitOrderUseCase.submitOrder(new SubmitOrderCommand(orderId));
    return ResponseEntity.ok(SubmitOrderResponseDto.from(order));
  }

  private CreateOrderCommand toCommand(CreateOrderRequestDto request) {
    var items = request.items().stream()
        .map(item -> new CreateOrderItemCommand(item.productId(), item.quantity(), item.price()))
        .toList();
    return new CreateOrderCommand(request.customerId(), items);
  }
}
