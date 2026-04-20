package demo.orders.controller;

import demo.orders.application.CreateOrderUseCase;
import demo.orders.application.SubmitOrderUseCase;
import demo.orders.dto.CreateOrderRequestDto;
import demo.orders.dto.CreateOrderResponseDto;
import demo.orders.dto.SubmitOrderResponseDto;
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
    var order = this.createOrderUseCase.createOrder(request.customerId(), request.items());
    return ResponseEntity.ok(CreateOrderResponseDto.from(order));
  }

  @PatchMapping("/{orderId}/submit")
  public ResponseEntity<SubmitOrderResponseDto> submitOrder(@PathVariable String orderId) {
    var order= this.submitOrderUseCase.submitOrder(orderId);
    return ResponseEntity.ok(SubmitOrderResponseDto.from(order));
  }
}
