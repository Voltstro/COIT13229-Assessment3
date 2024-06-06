package au.edu.cqu.jhle.shared.requests;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.Product;

import java.util.List;

public class GetProductsRequest extends Request {
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

    private List<Product> productList;

    @Override
    public void doRequest(DatabaseUtility databaseUtility) {
        productList = databaseUtility.getProducts();
    }
}
