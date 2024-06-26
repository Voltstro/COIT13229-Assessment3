package au.edu.cqu.jhle.core;

import au.edu.cqu.jhle.shared.models.User;
import au.edu.cqu.jhle.shared.requests.*;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class ClientRequestManager {

    /**
     * Server host of Server
     */
    private static final String serverHost = "localhost";

    /**
     * Server port of Server
     */
    private static final int serverPort = 8000;

    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private PublicKey publicKey;
    private User loggedInUser;

    public ClientRequestManager() {
        try {
            //Connect to server
            clientSocket = new Socket(serverHost, serverPort);

            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            //Ask for public key
            outputStream.writeObject(new PublicKeyRequest());

            //Get public key back
            PublicKeyRequest publicKeyRequest = (PublicKeyRequest) inputStream.readObject();
            try {
                X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKeyRequest.getPublicKey());
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                publicKey = keyFactory.generatePublic(pubKeySpec);
                System.out.println("Have received public key from server...");
            } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                //Shouldn't happen
            }
        } catch (IOException ex) {
            //Do something
        } catch (ClassNotFoundException ignored) {

        }
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public LoginRequest sendLoginRequest(LoginRequest request) throws IOException {
        try {
            outputStream.writeObject(request);
            return (LoginRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        }
    }

    public RegisterUserRequest sendRegisterUserRequest(RegisterUserRequest request) throws IOException {
        try {
            outputStream.writeObject(request);
            return (RegisterUserRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        }
    }

    public GetProductsRequest getProductsRequest(GetProductsRequest request) throws IOException {
        try {
            outputStream.writeObject(request);
            return (GetProductsRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        }
    }

    public AddProductRequest upsertProductRequest(AddProductRequest request) {
        try {
            outputStream.writeObject(request);
            return (AddProductRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public GetSchedulesRequest sendGetSchedulesRequest(GetSchedulesRequest request) throws IOException {
        try {
            outputStream.writeObject(request);
            return (GetSchedulesRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        }
    }

    public AddScheduleRequest upsertScheduleRequest(AddScheduleRequest request) {
        try {
            outputStream.writeObject(request);
            return (AddScheduleRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public GetOrdersRequest getOrdersRequest(GetOrdersRequest request) throws IOException {
        try {
            outputStream.writeObject(request);
            return (GetOrdersRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        }
    }

    public GetUsersRequest getUsersRequest(GetUsersRequest request) throws IOException {
        try {
            outputStream.writeObject(request);
            return (GetUsersRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public AddUserRequest addUserRequest(AddUserRequest request) throws IOException {
        try {
            outputStream.writeObject(request);
            return (AddUserRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public GetUserByIdRequest getUserByIdRequest(GetUserByIdRequest request) throws IOException {
        try {
            outputStream.writeObject(request);
            return (GetUserByIdRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public GetOrderLinesForOrderRequest getOrderLinesForOrderRequest(GetOrderLinesForOrderRequest request) throws IOException {
        try {
            outputStream.writeObject(request);
            return (GetOrderLinesForOrderRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public GetProductByIdRequest getProductByIdRequest(GetProductByIdRequest request) throws IOException {
        try {
            outputStream.writeObject(request);
            return (GetProductByIdRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public AddOrderRequest upsertOrderRequest(AddOrderRequest request) {
        try {
            outputStream.writeObject(request);
            return (AddOrderRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public AddOrderLineRequest upsertOrderLineRequest(AddOrderLineRequest request) {
        try {
            outputStream.writeObject(request);
            return (AddOrderLineRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public GetOrderByIdRequest getOrderByIdRequest(GetOrderByIdRequest request) throws IOException {
        try {
            outputStream.writeObject(request);
            return (GetOrderByIdRequest) inputStream.readObject();
        } catch (ClassNotFoundException ex) {
            //This should not happen
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] encrypt(String message) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            return cipher.doFinal(message.getBytes("UTF-8"));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
