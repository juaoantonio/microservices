package demo.orders.interfaceadapter.web;

import demo.orders.application.query.OrderDetailsQueryResult;
import demo.orders.application.query.OrderSummaryQueryResult;
import demo.orders.application.query.OrdersQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class OrderQueryController {
  private final OrdersQueryService ordersQueryService;

  @GetMapping
  public ResponseEntity<Page<OrderSummaryQueryResult>> getOrders(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    var pageable = Pageable.ofSize(size).withPage(page);
    var orders = this.ordersQueryService.getOrders(pageable);
    return ResponseEntity.ok(orders);
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<OrderDetailsQueryResult> getOrder(@PathVariable String orderId) {
    var order = this.ordersQueryService.getOrderById(orderId);
    return ResponseEntity.ok(order);
  }
}
