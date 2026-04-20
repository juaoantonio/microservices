package demo.orders.infra;

import demo.orders.domain.IEvent;
import demo.orders.domain.IEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisherImpl implements IEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishEvent(IEvent event) {
        this.applicationEventPublisher.publishEvent(event);
    }
}
