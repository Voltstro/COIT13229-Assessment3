package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.Product;

public class GetProductByIdRequest extends Request {
    public GetProductByIdRequest(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProductList(Product product) {
        this.product = product;
    }

    private Product product;
    
    private int id;

    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        try {
            product = databaseUtility.getProductById(id);
        } catch (Exception ex) {
            setErrorMessage("Could not get product from database!\n" + ex.getMessage());
            return;
        }

        setValid(true);
    }
}
