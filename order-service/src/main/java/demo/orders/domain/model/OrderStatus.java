package demo.orders.domain.model;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("CREATED"),
    SUBMITTED("SUBMITTED"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED");

    private String value;

    OrderStatus(String value) {
    }
}
