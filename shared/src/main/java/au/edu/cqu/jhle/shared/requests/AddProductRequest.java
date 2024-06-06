package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.Product;

public class AddProductRequest extends Request {
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
    public void doRequest(DatabaseUtility databaseUtility) {
        //TODO: Yup
        System.out.println("Added product " + product.toString());
    }
}
