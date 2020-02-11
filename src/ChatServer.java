import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    public static void main(String[] args) {
        int portNumber = 4444;
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(portNumber);
            acceptClients(serverSocket, portNumber);
        } catch (IOException e) {
            System.err.println("Could not listen to port: " + portNumber);
            System.exit(1);
        }
    }

    public static void acceptClients(ServerSocket serverSocket, int portNumber) {
        ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                ClientThread client = new ClientThread(socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            } catch (IOException e) {
                System.err.println("Accept failed on: " + portNumber);
            }
        }
    }
}
