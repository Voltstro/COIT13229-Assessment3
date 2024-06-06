package au.edu.cqu.jhle.server;

import au.edu.cqu.jhle.shared.database.DatabaseUtility;
import au.edu.cqu.jhle.shared.models.User;
import au.edu.cqu.jhle.shared.requests.Request;
import au.edu.cqu.jhle.shared.requests.LoginRequest;
import au.edu.cqu.jhle.shared.requests.PublicKeyRequest;

import javax.crypto.Cipher;
import java.security.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class Server {

    /**
     * Port for the server to listen on
     */
    private static final int port = 8000;

    private static PrivateKey privateKey;
    private static PublicKey publicKey;

    /**
     * Server main entry point
     */
    public static void main(String[] args) {

        //Generate keys
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair = generator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException ex) {
            //This shouldn't happen
            throw new RuntimeException(ex);
        }

        //Now start server
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);

            System.out.println("Server is listening on port " + port);
            while (true) {
                //Accept incoming connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server got connection from " + clientSocket.getLocalAddress());

                //Run on separate thread
                Connection clientConnection = new Connection(clientSocket);
                clientConnection.start();
            }
        } catch(IOException e) {
            System.out.println("Error in Server!\n" + e.getMessage());
        } finally {
            //Close socket when done
            if(serverSocket != null) {
                try{
                    serverSocket.close();
                } catch (IOException e) {
                    System.out.println("Error closing server socket!\n" + e.getMessage());
                }
            }
        }
    }

    public static String decrypt(byte[] data) {
       try {
           Cipher cipher = Cipher.getInstance("RSA");
           cipher.init(Cipher.DECRYPT_MODE, privateKey, cipher.getParameters());
           return new String(cipher.doFinal(data));
       } catch (Exception ex) {
           throw new RuntimeException(ex);
       }
    }

    /**
     * Connection class for each client connecting to the server
     * Runs on its own thread
     */
    static class Connection extends Thread {
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;
        private Socket clientSocket;
        private User connectionUser;

        public Connection(Socket clientSocket) {
            try {
                this.clientSocket = clientSocket;

                outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
            } catch(IOException e) {
                System.out.println("Error setting up connection!\n" + e.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                //Create database util for this connection
                DatabaseUtility databaseManager = new DatabaseUtility();

                //Send public key
                PublicKeyRequest publicKeyRequest = (PublicKeyRequest) inputStream.readObject();
                byte[] bytesPubKey = publicKey.getEncoded();
                publicKeyRequest.setPublicKey(bytesPubKey);
                outputStream.writeObject(publicKeyRequest);

                //First request will always be a login request type
                /*
                while (true) {
                    LoginRequest loginRequest = (LoginRequest) inputStream.readObject();
                    String passwordText = decrypt(loginRequest.getPassword());

                    //TODO: We probs want to know about user's role and details
                    User user = loginRequest.isValid(passwordText, databaseManager);

                    //Write back object
                    outputStream.writeObject(loginRequest);

                    //Login request was good, move on
                    if(user != null)
                        break;
                }
                */

                //Now we can accept normal requests
                while (true) {
                    Request request = (Request) inputStream.readObject();

                    if (request instanceof LoginRequest loginRequest) {
                        try {
                            loginRequest.doRequest(databaseManager);
                            User user = loginRequest.getUser();
                            if (user == null) {
                                throw new Exception();
                            }

                            //Validate password
                            byte[] passwordEncrpted = loginRequest.getPassword();
                            String password = decrypt(passwordEncrpted);
                            if (!user.password.equals(password))
                                throw new Exception("Password invalid!");

                            //Set connection user to this one
                            connectionUser = user;
                        } catch (Exception ex) {
                            //Failed
                        }

                        outputStream.writeObject(request);
                        continue;
                    }

                    request.doRequest(databaseManager);
                    outputStream.writeObject(request);
                }

            } catch(EOFException e) {
                System.out.println("EOF Error!\n" + e.getMessage());
            } catch(IOException e) {
                //Thrown when client disconnects
            } catch (ClassNotFoundException ex) {
                System.out.println("Class not found error!\n" + ex.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch(IOException e) {
                    //Swallow, should be fine
                }
            }
        }
    }
}
