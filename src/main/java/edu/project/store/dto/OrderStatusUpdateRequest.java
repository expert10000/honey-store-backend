package edu.project.store.dto;

import edu.project.store.model.OrderStatus;

public class OrderStatusUpdateRequest {
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
