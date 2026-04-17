package demo.orders.domain;

import lombok.Getter;

@Getter
public enum InventoryResult {
    UNKNOWN("UNKNOWN"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");

    private String value;

    InventoryResult(String value) {
    }
}
