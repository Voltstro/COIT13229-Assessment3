package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.Product;

public class AddProductRequest extends Request {
    private Product product;

    public AddProductRequest(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        try {
            databaseUtility.upsertProduct(product);
        } catch (Exception ex) {
            setErrorMessage("Failed to upsert product!\n" + ex.getMessage());
            return;
        }

        setValid(true);
    }
}
