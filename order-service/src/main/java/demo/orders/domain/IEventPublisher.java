package demo.orders.domain;

public interface IEventPublisher {
    void publishEvent(IEvent from);
}
