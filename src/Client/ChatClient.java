package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Scanner;

public class ChatClient {

    private static String host = "localhost";
    private static int portNumber = 4444;
    private static boolean hostAccepted = false;

    private String userName;
    private String serverHost;
    private int serverPort;

    public static void main(String[] args){
        String readName = null;
        Scanner scan = new Scanner(System.in);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while (hostAccepted == false) {
            host = promptHostIP(scan);
            hostAccepted = pingHost(host);
        }

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

    private ChatClient(String name, String host, int portNumber) {
        this.userName = name;
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

    private static String promptHostIP(Scanner scan) {
        String serverIP = "localhost";
        String input = null;
        System.out.println("Enter IP of Server (Default is localhost):");
        if ((input = scan.nextLine()) != null) {
            serverIP = input;
        }
        return serverIP;
    }

    private static Boolean pingHost(String hostIP) {
        try {
            InetAddress host = InetAddress.getByName(hostIP);
            System.out.println("Sending ping to host: " + hostIP);
            Date start = new Date();
            if (host.isReachable(5000)) {
                Date stop = new Date();
                long timeToRespond = (stop.getTime() - start.getTime());
                System.out.println("Host responded in " + timeToRespond + " ms");
                return true;
            } else {
                System.out.println("Could not connect to host");
            }
        } catch (UnknownHostException e) {
            System.out.println("Fatal Connection Error: Unknown Host");
        } catch (IOException e) {
            System.out.println("Fatal Connection Error: IOException");
        }
        return false;
    }
}