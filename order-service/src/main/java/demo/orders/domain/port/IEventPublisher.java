package demo.orders.domain.port;

import demo.orders.domain.event.IEvent;

public interface IEventPublisher {
    void publishEvent(IEvent from);
}
