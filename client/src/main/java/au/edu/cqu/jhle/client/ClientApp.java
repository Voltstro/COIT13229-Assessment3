package au.edu.cqu.jhle.client;

import au.edu.cqu.jhle.core.ClientRequestManager;
import au.edu.cqu.jhle.core.Utils;
import au.edu.cqu.jhle.shared.models.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class ClientApp extends Application {

    private static Scene scene;
    private static ClientRequestManager clientRequestManager;

    @Override
    public void start(Stage stage) throws IOException {

        //Connect to server
        //TODO: if connection fails, we should have like a nice error message or something
        try {
            clientRequestManager = new ClientRequestManager();
        } catch (Exception e) {
            System.out.println("Client request manager could not be created!");
            e.printStackTrace();

            Utils.createAndShowAlert("Failed to connect to server!", "Failed to connect to server! See console for more details.", Alert.AlertType.ERROR);
            return;
        }

        scene = new Scene(loadFXML("login"), 640, 480);
        stage.setTitle("Maleny Dairy To Home System (MDHS)");
        stage.setScene(scene);
        stage.show();
    }

    public static <T> T setRoot(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(ClientApp.class.getResource(fxml + ".fxml"));
        scene.setRoot(loader.load());
        return loader.getController();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public static ClientRequestManager getClientRequestManager() {
        return clientRequestManager;
    }

}