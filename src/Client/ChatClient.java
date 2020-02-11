import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    public static void main(String[] args) {
        Socket socket = null;
        System.out.println("Please enter username");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        scanner.close();
        int portNumber = 4444;

        try {
            socket = new Socket("localhost", portNumber);
            Thread.sleep(1000);
            Thread server = new Thread(new ServerThread(socket, name));
            server.start();
        } catch (IOException e) {
            System.err.println("Fatal Connection error!");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Fatal connection error!");
            e.printStackTrace();
        }
    }
}
