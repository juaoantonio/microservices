package demo.payments.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.JacksonJsonMessageConverter;

@Configuration
public class RabbitMQConfig {
    public static final String PAYMENTS_EXCHANGE = "payment.events";
    public static final String ORDERS_EXCHANGE = "order.events";

    public static final String PAYMENT_ORDERS_QUEUE = "payment.orders.queue";

    public static final String PAYMENT_APPROVED_KEY = "payment.approved";
    public static final String PAYMENT_REJECTED_KEY = "payment.rejected";

    public static final String ORDER_SUBMITTED_KEY = "order.submitted";

    @Bean
    TopicExchange paymentsExchange() {
        return new TopicExchange(PAYMENTS_EXCHANGE, true, false);
    }

    @Bean
    TopicExchange ordersExchange() {
        return new TopicExchange(ORDERS_EXCHANGE, true, false);
    }

    @Bean
    Queue paymentOrdersQueue() {
        return new Queue(PAYMENT_ORDERS_QUEUE, true);
    }

    @Bean
    Binding orderSubmittedBinding(Queue paymentOrdersQueue, TopicExchange ordersExchange) {
        return BindingBuilder.bind(paymentOrdersQueue).to(ordersExchange).with(ORDER_SUBMITTED_KEY);
    }

    @Bean
    JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
