import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ClientGUI extends Application {

    private TextField languageCodeField = new TextField();
    private TextField wordField = new TextField();
    private Button translateButton = new Button("Translate");
    private TextArea resultArea = new TextArea();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Dictionary Client");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);

        // Adding UI Components to the Grid
        gridPane.add(new Label("Language Code:"), 0, 0);
        gridPane.add(languageCodeField, 1, 0);
        gridPane.add(new Label("Word to Translate:"), 0, 1);
        gridPane.add(wordField, 1, 1);
        gridPane.add(translateButton, 1, 2);
        gridPane.add(resultArea, 0, 3, 2, 1);

        translateButton.setOnAction(e -> translateWord());

        Scene scene = new Scene(gridPane, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void translateWord() {
        String hostname = "localhost";
        int port = 4000;
        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(languageCodeField.getText());
            out.println(wordField.getText());
            String response = in.readLine();
            resultArea.setText(response);

        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
