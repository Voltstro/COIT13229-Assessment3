package au.edu.cqu.jhle.shared.models;

import java.io.Serializable;

public class OrderLine implements Serializable {
    private int id;
    private int productId;
    private int orderId;
    private int quantity;
    private Double cost;

    public OrderLine(int productId, int orderId, int quantity, Double cost) {
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.cost = cost;
    }

    public OrderLine(int id, int productId, int orderId, int quantity, Double cost) {
        this.id = id;
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "id=" + id +
                ", productId=" + productId + '\'' +
                ", orderId=" + orderId + '\'' +
                ", quantity=" + quantity + '\'' +
                ", cost=" + cost + '\'' +
                '}';
    }


}
