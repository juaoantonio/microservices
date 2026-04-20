package demo.orders.interfaceadapter.web;

import demo.orders.application.usecase.CreateOrderUseCase;
import demo.orders.application.usecase.SubmitOrderUseCase;
import demo.orders.interfaceadapter.web.dto.CreateOrderRequestDto;
import demo.orders.interfaceadapter.web.dto.SubmitOrderRequestDto;
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
  public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequestDto request) {
    var orderId = this.createOrderUseCase.createOrder(CreateOrderRequestDto.toCommand(request));
    return ResponseEntity.ok(orderId);
  }

  @PatchMapping("/{orderId}/submit")
  public ResponseEntity<String> submitOrder(@PathVariable String orderId) {
    var submittedOrderId = this.submitOrderUseCase.submitOrder(SubmitOrderRequestDto.toCommand(orderId));
    return ResponseEntity.ok(submittedOrderId);
  }
}
