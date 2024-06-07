package au.edu.cqu.jhle.shared.models;

import java.io.Serializable;

public class Order implements Serializable {
    private int id;
    private int customerId;
    private int statusId;
    private String deliveryTime;
    private Double totalCost;

    public Order(int customerId, int statusId, String deliveryTime, Double totalCost) {
        this.customerId = customerId;
        this.statusId = statusId;
        this.deliveryTime = deliveryTime;
        this.totalCost = totalCost;
    }

    public Order(int id, int customerId, int statusId, String deliveryTime, Double totalCost) {
        this.id = id;
        this.customerId = customerId;
        this.statusId = statusId;
        this.deliveryTime = deliveryTime;
        this.totalCost = totalCost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer id=" + customerId + '\'' +
                ", status id=" + statusId + '\'' +
                ", delivery time=" + deliveryTime + '\'' +
                ", total cost=" + totalCost + '\'' +
                '}';
    }
}
