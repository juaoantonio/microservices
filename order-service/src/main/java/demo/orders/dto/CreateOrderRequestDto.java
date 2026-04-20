package demo.orders.dto;

import java.util.List;

public record CreateOrderRequestDto(String customerId, List<CreateOrderItemRequestDto> items) {}
