import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 4000;

        try (Socket socket = new Socket(hostname, port)) {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in)) ;


            System.out.println("Connected to the main server at " + hostname + ":" + port);
            System.out.print("Enter the language code (e.g., EN, GR): ");
            String languageCode = clientInput.readLine();
            System.out.println("Enter the word to translate");
            String word = clientInput.readLine();

            output.println(languageCode);
            output.println(word);

            String response = input.readLine();
            System.out.println(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

