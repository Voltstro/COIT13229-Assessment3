package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.Product;

import java.util.List;

public class GetProductsRequest extends Request {
    private List<Product> productList;

    public GetProductsRequest() {
    }

    public GetProductsRequest(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        try {
            productList = databaseUtility.getProducts();
        } catch (Exception ex) {
            setErrorMessage("Could not get products from database!\n" + ex.getMessage());
            return;
        }

        setValid(true);
    }
}
