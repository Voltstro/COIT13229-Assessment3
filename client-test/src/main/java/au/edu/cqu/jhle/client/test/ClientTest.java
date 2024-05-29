package au.edu.cqu.jhle.client.test;

import au.edu.cqu.jhle.shared.models.Product;
import au.edu.cqu.jhle.shared.requests.AddProductRequest;
import au.edu.cqu.jhle.shared.requests.IRequest;
import au.edu.cqu.jhle.shared.requests.LoginRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class ClientTest {

    /**
     * Server host of Server
     */
    private static final String serverHost = "localhost";

    /**
     * Server port of Server
     */
    private static final int serverPort = 8000;

    public static void main(String[] args) {
        Socket clientSocket = null;
        try {
            //Connect to server
            clientSocket = new Socket(serverHost, serverPort);
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());

            outputStream.writeObject(new LoginRequest("root", "Password.123"));
            LoginRequest loginResponse = (LoginRequest) inputStream.readObject();

            if(!loginResponse.isValid()) {
                System.out.println("Login failed!");
                return;
            }

            IRequest addProductRequest = new AddProductRequest(new Product(1, "test", "test", 123.1, "test"));
            outputStream.writeObject(addProductRequest);

            //TODO: We should probs valid a request was successful or not
            IRequest request = (IRequest) inputStream.readObject();


        } catch (Exception ex){
            //TODO: Don't do exceptions like this
            System.out.println(ex);
        } finally {
            if(clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    //Should be fine...
                }
            }
        }
    }
}
