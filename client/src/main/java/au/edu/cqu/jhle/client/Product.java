package au.edu.cqu.jhle.client;

/* NOTE: Refactor into shared project*/
public class Product {
    private Integer id;
    private String name;
    private Integer quantity;
    private String unit;
    private Double unitPrice;
    private String ingredients;
    
    public Product() {}
    
    public Product(
        Integer id, 
        String name, 
        Integer quantity, 
        String unit, 
        Double unitPrice, 
        String ingredients) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.ingredients = ingredients;
    }
    
    @Override
    public String toString() {
        return "id: " + id + ", name: " + name + "quantity: " + quantity + ", unit: " + unit + ", unitPrice: " + unitPrice + ", ingredients: " + ingredients; 
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    
    
}
