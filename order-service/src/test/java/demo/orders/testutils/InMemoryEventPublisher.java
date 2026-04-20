package demo.orders.testutils;

import demo.orders.domain.event.IEvent;
import demo.orders.domain.port.IEventPublisher;
import java.util.ArrayList;
import java.util.List;

public class InMemoryEventPublisher implements IEventPublisher {
  private final List<IEvent> events = new ArrayList<>();

  @Override
  public void publishEvent(IEvent event) {
    events.add(event);
  }

  public List<IEvent> events() {
    return events;
  }
}
