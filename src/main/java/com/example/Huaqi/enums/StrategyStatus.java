package com.example.Huaqi.enums;

/**
 * Strategy表/类中的交易类型
 */
public enum StrategyStatus {
    Success("Success"),
    Failed("Failed");

    private final String status;
    StrategyStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
