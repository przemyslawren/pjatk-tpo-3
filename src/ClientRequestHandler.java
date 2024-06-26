import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class ClientRequestHandler implements Runnable {
    private Socket clientSocket;
    private HashMap<String, Integer> languagePorts;
    private MainServer mainServer;

    public ClientRequestHandler(Socket clientSocket,
                                HashMap<String, Integer> languagePorts,
                                MainServer mainServer) {
        this.clientSocket = clientSocket;
        this.languagePorts = languagePorts;
        this.mainServer = mainServer;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            String action = input.readLine();

            if (action.equals("CREATE_DICTIONARY")) {
                String languageCode = input.readLine();
                int port = Integer.parseInt(input.readLine());
                if (!languagePorts.containsValue(port)) {
                    if (!languagePorts.containsKey(languageCode)) {
                        mainServer.createNewDictionary(languageCode, port);
                        output.println("New dictionary for "
                                + languageCode + " created on port " + port);
                    } else {
                        output.println("Dictionary for " + languageCode + " already exists.");
                    }
                } else {
                    output.println("Port is already taken.");
                }
            } else {
                String languageCode = action;
                String word = input.readLine();

                if (languagePorts.containsKey(languageCode)) {
                    int port = languagePorts.get(languageCode);
                    String translation = forwardRequestToDictionaryServer(word, languageCode, port);
                    output.println(translation);
                } else {
                    output.println("Error: Unsupported language code.");
                }
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String forwardRequestToDictionaryServer(String word, String languageCode, int port) {
        try (Socket socket = new Socket("localhost", port);
            PrintWriter dictionaryOutput = new PrintWriter(socket
                    .getOutputStream(), true);
            BufferedReader dictionaryInput = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()))) {

            dictionaryOutput.println(word);
            return dictionaryInput.readLine();
        } catch (IOException e) {
            return "Error communicating with dictionary server: " + e.getMessage();
        }
    }
}
