package demo.orders.controller;

import demo.orders.application.CreateOrderUseCase;
import demo.orders.dto.CreateOrderRequestDto;
import demo.orders.dto.CreateOrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class OrderController {
  private final CreateOrderUseCase createOrderUseCase;

  @PostMapping
  public ResponseEntity<CreateOrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto request) {
    var order = this.createOrderUseCase.createOrder(request.customerId(), request.items());
    return ResponseEntity.ok(CreateOrderResponseDto.from(order));
  }
}
