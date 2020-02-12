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
            while (socket.isConnected()) {
                if (in.hasNextLine()) {
                    String input = in.nextLine();

                    System.out.println(input); // Server read chat

                    for (ClientThread thatClient : server.getClients()) {
                        PrintWriter thatClientOut = thatClient.getWriter();
                        if (thatClientOut != null) {
                            thatClientOut.write(input + "\r\n");
                            thatClientOut.flush();
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