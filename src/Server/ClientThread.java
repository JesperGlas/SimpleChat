package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {
    private Socket socket;
    private PrintWriter clientOut;
    private Server server;

    public ClientThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    private PrintWriter getWriter() {
        return clientOut;
    }

    @Override
    public void run() {
        try {
            // Setup
            this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            Scanner in = new Scanner(socket.getInputStream());

            // Start communicating
            while (!socket.isClosed()) {
                if (in.hasNextLine()) {
                    String input = in.nextLine();

                    System.out.println(input); // Server print chat

                    for (ClientThread client : server.getClients()) {
                        PrintWriter clientOut = client.getWriter();
                        if (clientOut != null) {
                            clientOut.write(input + "\r\n");
                            clientOut.flush();
                        }
                    }
                }
            }
            // Close socket on exit
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}