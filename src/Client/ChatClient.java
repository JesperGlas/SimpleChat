package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    private static final String host = "localhost";
    private static final int portNumber = 4444;

    private String userName;
    private String serverHost;
    private int serverPort;
    private Scanner userInputScanner;

    public static void main(String[] args){
        String readName = null;
        Scanner scan = new Scanner(System.in);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter Username:");
        while(readName == null || readName.trim().equals("")) {
            readName = scan.nextLine();
            if(readName.trim().equals("")){
                System.out.println("Invalid. Please enter again:");
            }
        }

        ChatClient client = new ChatClient(readName, host, portNumber);
        client.startClient(bufferedReader);
    }

    private ChatClient(String userName, String host, int portNumber){
        this.userName = userName;
        this.serverHost = host;
        this.serverPort = portNumber;
    }

    private void startClient(BufferedReader bufferedReader){
        try{
            Socket socket = new Socket(serverHost, serverPort);

            // Waiting for network communication
            Thread.sleep(1000);

            ServerThread serverThread = new ServerThread(socket, userName);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
            while(serverAccessThread.isAlive()) {
                System.out.println("Busy wait?");
                String line;
                if((line = bufferedReader.readLine()) != null){
                    serverThread.addNextMessage(line);
                }
            }
        } catch(IOException ex){
            System.err.println("Fatal Connection error!");
            ex.printStackTrace();
        } catch(InterruptedException ex){
            System.out.println("Interrupted");
        }
    }
}