import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class DictionaryServer extends Thread {
    private Map<String, String> dictionary;
    private ServerSocket serverSocket;
    private String languageCode;

    public DictionaryServer(String languageCode,
                            Map<String, String> dictionary,
                            int port) {
        this.languageCode = languageCode;
        this.dictionary = dictionary;
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("Server for " + languageCode + " started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        start();
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputWord = input.readLine();
                String translation = dictionary.getOrDefault(inputWord, "Error: Word not found");
                output.println(translation);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            serverSocket.close();
        } catch (Exception e) {}
    }

    public static void main(String[] args) {
        Map<String, String> englishDictionary = new HashMap<>();
        englishDictionary.put("cześć", "hello");
        englishDictionary.put("świat", "world");

        Map<String, String> germanDictionary = new HashMap<>();
        germanDictionary.put("hello", "hallo");
        germanDictionary.put("world", "welt");

        new DictionaryServer("EN", englishDictionary, 5000);
        new DictionaryServer("GR", germanDictionary, 5001);
    }
}
