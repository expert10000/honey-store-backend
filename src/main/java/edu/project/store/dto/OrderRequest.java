package edu.project.store.dto;



import java.util.List;



public class OrderRequest {
    public List<OrderItemRequest> cart;
    public String deliveryMethod;
    public String deliveryAddress;
    public String deliveryComment;
    public boolean invoice;

    public static class OrderItemRequest {
        public Long productId;      
        public Integer quantity;
    }
}        