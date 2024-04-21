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
               threadPool.execute(new ClientRequestHandler(clientSocket, languagePorts));
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

    public static void main(String[] args) throws IOException {
        MainServer mainServer = new MainServer(4000);
        mainServer.listen();
    }
}
