package au.edu.cqu.jhle.server;

import au.edu.cqu.jhle.shared.database.DatabaseManager;
import au.edu.cqu.jhle.shared.requests.IRequest;
import au.edu.cqu.jhle.shared.requests.LoginRequest;

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

    /**
     * Server main entry point
     */
    public static void main(String[] args) {
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

    /**
     * Connection class for each client connecting to the server
     * Runs on its own thread
     */
    static class Connection extends Thread {
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;
        private Socket clientSocket;

        public Connection(Socket clientSocket) {
            try {
                this.clientSocket = clientSocket;
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
                outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            } catch(IOException e) {
                System.out.println("Error setting up connection!\n" + e.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                //TODO: Database manager needs to connect to a database lol
                DatabaseManager databaseManager = new DatabaseManager();

                //First request will always be a login request type
                while (true) {
                    LoginRequest loginRequest = (LoginRequest) inputStream.readObject();
                    //TODO: We probs want to know about user's role and details
                    boolean isValid = loginRequest.isValid(databaseManager);

                    //Write back object
                    outputStream.writeObject(loginRequest);

                    //Login request was good, move on
                    if(isValid)
                        break;
                }

                //Now we can accept normal requests
                while (true) {
                    IRequest request = (IRequest) inputStream.readObject();
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
