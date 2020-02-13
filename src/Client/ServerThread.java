package Client;

import Shared.Payload;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerThread implements Runnable {
    private Socket socket;
    private String userName;
    private final LinkedList<String> messagesToSend;
    private boolean hasMessages = false;

    public ServerThread(Socket socket, String userName) {
        this.socket = socket;
        this.userName = userName;
        messagesToSend = new LinkedList<String>();
    }

    public void addNextMessage(String message) {
        synchronized (messagesToSend) {
            hasMessages = true;
            messagesToSend.push(message);
        }
    }

    @Override
    public void run() {
        System.out.println("Welcome: " + userName);

        System.out.println("Local Port :" + socket.getLocalPort());
        System.out.println("Server = " + socket.getRemoteSocketAddress() + ":" + socket.getPort());

        try {
            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInStream = socket.getInputStream();
            Scanner serverIn = new Scanner(serverInStream);

            while (socket.isConnected()) {
                if (serverInStream.available() > 0) {
                    if (serverIn.hasNextLine()) {
                        String receivedString = serverIn.nextLine();
                        Payload receivedPayload = new Payload(receivedString);
                        // Prints message to client
                        System.out.println(">> " + receivedPayload.getSender() + " " + receivedPayload.getTimeStampString() + "\n" + "> " + receivedPayload.getBody());
                    }
                } else {
                    // Used to prevent high CPU usage
                    Thread.sleep(200);
                }
                if (hasMessages) {
                    String nextSend = "";
                    synchronized (messagesToSend) {
                        nextSend = messagesToSend.pop();
                        hasMessages = !messagesToSend.isEmpty();
                    }
                    Payload messagePayload = new Payload(1, userName, nextSend);
                    // Sends message payload to server
                    serverOut.println(messagePayload.toString());
                    serverOut.flush();
                }
            }
            // Close socket on exit
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}