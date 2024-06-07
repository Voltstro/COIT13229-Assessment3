package au.edu.cqu.jhle.controllers;

import au.edu.cqu.jhle.client.ClientApp;
import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.core.Utils;
import au.edu.cqu.jhle.shared.models.Product;
import au.edu.cqu.jhle.shared.requests.AddProductRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductDetailController implements Initializable {

    ClientRequestManager requestManager;
    @FXML
    private Label productTitleLabel;
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
        requestManager = ClientApp.getClientRequestManager();
        productDetails = null;
    }

    @FXML
    private void onReturnToList() throws IOException {
        ClientApp.setRoot("products");
    }

    @FXML
    private void onSaveProduct() throws IOException {
        saveProduct();
    }

    public void setProduct(Product product) {
        this.productDetails = product;
        populateFields();
    }

    private void populateFields() {
        productTitleLabel.setText("Product Details (ID: %s)".formatted(productDetails.getId()));

        nameInput.setText(productDetails.getName());
        quantityInput.setText(String.valueOf(productDetails.getQuantity()));
        unitInput.setText(productDetails.getUnit());
        unitPriceInput.setText(productDetails.getPrice().toString());
        ingredientsInput.setText(productDetails.getIngredients());
    }

    private void saveProduct() throws IOException {
        try {
            //Ensure fields are not empty
            if (Utils.isEmpty(nameInput) || Utils.isEmpty(quantityInput) || Utils.isEmpty(unitInput) || Utils.isEmpty(unitPriceInput) || Utils.isEmpty(ingredientsInput)) {
                Utils.createAndShowAlert("Invalid fields", "Fields cannot be empty!", Alert.AlertType.ERROR);
                return;
            }

            //Get field details
            String name = nameInput.getText();
            int quantity = Integer.parseInt(quantityInput.getText());
            String unit = unitInput.getText();
            double price = Double.parseDouble(unitPriceInput.getText());
            String ingredients = ingredientsInput.getText();

            boolean newProduct = true;
            if (productDetails == null) {
                productDetails = new Product(name, quantity, unit, price, ingredients);
            } else {
                productDetails.setName(name);
                productDetails.setQuantity(quantity);
                productDetails.setUnit(unit);
                productDetails.setPrice(price);
                productDetails.setIngredients(ingredients);
                newProduct = false;
            }

            //Send response
            AddProductRequest response = requestManager.upsertProductRequest(new AddProductRequest(productDetails));
            if (response.isValid()) {
                //Display message saying products was updated/added successfully
                if (newProduct) {
                    Utils.createAndShowAlert("Successfully created product", "Product was successfully created!", Alert.AlertType.INFORMATION);
                } else {
                    Utils.createAndShowAlert("Successfully updated product", "Product was successfully updated!", Alert.AlertType.INFORMATION);
                }

                ClientApp.setRoot("products");
                return;
            }

            //Failed
            Utils.createAndShowAlert("Failed Updating Product", response.getErrorMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException ex) {
            //Number formating error
            Utils.createAndShowAlert("Invalid fields", "Quantity and price must be valid numbers!", Alert.AlertType.ERROR);
        }
    }
}
