package demo.orders.interfaceadapter.web.dto;

import demo.orders.application.command.SubmitOrderCommand;

public record SubmitOrderRequestDto(String orderId) {
  public static SubmitOrderCommand toCommand(String orderId) {
    return new SubmitOrderCommand(orderId);
  }
}
