package demo.orders.domain.exception;

public class OrderNotFoundException extends RuntimeException {

  public OrderNotFoundException(String id) {
      super("Order with id " + id + " not found");
  }
}
