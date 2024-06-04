package au.edu.cqu.jhle.shared.models;

import java.io.Serializable;

public class DeliverySchedule implements Serializable {
    public DeliverySchedule(int id, int postcode, String day, Double cost) {
        this.id = id;
        this.postcode = postcode;
        this.day = day;
        this.cost = cost;
    }
    
    private int id;
    
    private int postcode;
    
    private String day;
    
    private double cost;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostcode() {
        return postcode;
    }

    public void setPostcode(int postcode) {
        this.postcode = postcode;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
    
    @Override
    public String toString() {
        return "DeliverySchedule {" +
               "id=" + id +
                ", postcode='" + postcode + '\'' +
                ", day='" + day + '\'' +
                ", cost=" + cost +
                '}';
    }
}
