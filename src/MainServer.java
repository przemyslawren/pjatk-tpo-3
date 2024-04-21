import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainServer {
   private ServerSocket serverSocket;
   private HashMap<String, Integer> languagePorts;
   private ExecutorService threadPool;

   public MainServer(int port) throws IOException {
       serverSocket = new ServerSocket(port);
       languagePorts = new HashMap<>();
       threadPool = Executors.newFixedThreadPool(10);
       languagePorts.put("EN", 5000);
       languagePorts.put("GR", 5001);
       System.out.println("Main Server is running on port " + port);
   }

   public void listen() {
       try {
           while (true) {
               Socket clientSocket = serverSocket.accept();
               threadPool.execute(new ClientRequestHandler(clientSocket, languagePorts, this));
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

   public void createNewDictionary(String languageCode, int port) {
       if (!languagePorts.containsKey(languageCode)) {
           Map<String, String> newDictionary = new HashMap<>(); // Pusty słownik dla nowego języka
           languagePorts.put(languageCode, port);
           new DictionaryServer(languageCode, newDictionary, port);
           System.out.println("New dictionary server for " + languageCode + " created on port " + port);
       } else {
           System.out.println("Dictionary for " + languageCode + " already exists.");
       }
   }

    public static void main(String[] args) throws IOException {
        MainServer mainServer = new MainServer(4000);
        mainServer.listen();
    }
}
