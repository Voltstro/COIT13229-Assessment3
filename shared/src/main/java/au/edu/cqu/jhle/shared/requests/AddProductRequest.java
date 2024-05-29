package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseManager;
import au.edu.cqu.jhle.shared.models.Product;

public class AddProductRequest implements IRequest {
    public AddProductRequest(Product product) {
        this.product = product;
    }

    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public void doRequest(DatabaseManager databaseManager) {
        //TODO: Yup
        System.out.println("Added product " + product.toString());
    }
}
