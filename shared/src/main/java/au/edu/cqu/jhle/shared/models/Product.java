package au.edu.cqu.jhle.shared.models;

import java.io.Serializable;

public class Product implements Serializable {
    public Product(int id, String name, String unit, Double price, String ingredients) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.ingredients = ingredients;
    }

    private int id;

    private String name;

    private String unit;

    private Double price;

    private String ingredients;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                ", ingredients='" + ingredients + '\'' +
                '}';
    }
}