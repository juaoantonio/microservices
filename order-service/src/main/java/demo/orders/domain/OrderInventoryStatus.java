package demo.orders.domain;

import lombok.Getter;

@Getter
public enum OrderInventoryStatus {
    INVENTORY_PENDING("INVENTORY_PENDING"),
    INVENTORY_APPROVED("INVENTORY_APPROVED"),
    INVENTORY_FAILED("INVENTORY_FAILED");

    private String value;

    OrderInventoryStatus(String value) {
    }
}
