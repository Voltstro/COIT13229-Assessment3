package au.edu.cqu.jhle.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class ProductDetailController implements Initializable {
    
    @FXML
    private TextField idInput;
    
    @FXML
    private TextField nameInput;
    
    @FXML
    private TextField quantityInput;
    
    @FXML
    private TextField unitInput;
    
    @FXML
    private TextField unitPriceInput;
    
    @FXML
    private TextField ingredientsInput;
    
    private Product productDetails;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void onReturnToList() throws IOException {
        App.setRoot("products");
    }
    
    @FXML
    private void onSaveProduct() throws IOException {
        saveProduct();
    }
    
    @FXML
    private void onRemoveProduct() throws IOException {
        removeProduct();
    }
    
    public void setProduct(Product product) {
        this.productDetails = product;
        populateFields();
    }
    
    private void populateFields() {
        idInput.setText(productDetails.getId().toString());
        nameInput.setText(productDetails.getName());
        quantityInput.setText(productDetails.getQuantity().toString());
        unitInput.setText(productDetails.getUnit());
        unitPriceInput.setText(productDetails.getUnitPrice().toString());
        ingredientsInput.setText(productDetails.getIngredients());
    }
    
    private void saveProduct() {
        
    }
    
    private void removeProduct() {
        
    }
}
