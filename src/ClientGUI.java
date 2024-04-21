import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ClientGUI extends Application {

    private final TextField languageCodeField = new TextField();
    private final TextField wordField = new TextField();
    private final Button translateButton = new Button("Translate");
    private final TextArea resultArea = new TextArea();
    private final Button createDictionaryButton = new Button("Create New Dictionary");
    private final TextField newLanguageCodeField = new TextField();
    private final TextField newDictionaryPortField = new TextField();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dictionary Client");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);


        gridPane.add(new Label("Language Code:"), 0, 0);
        gridPane.add(languageCodeField, 1, 0);
        gridPane.add(new Label("Word to Translate:"), 0, 1);
        gridPane.add(wordField, 1, 1);
        gridPane.add(translateButton, 1, 2);
        gridPane.add(resultArea, 0, 3, 2, 1);
        gridPane.add(new Label("New Language Code:"), 0, 4);
        gridPane.add(newLanguageCodeField, 1, 4);
        gridPane.add(new Label("Port for New Dictionary:"), 0, 5);
        gridPane.add(newDictionaryPortField, 1, 5);
        gridPane.add(createDictionaryButton, 1, 6);
        
        translateButton.setOnAction(e -> translateWord());
        createDictionaryButton.setOnAction(e -> createNewDictionary());
        
        Scene scene = new Scene(gridPane, 600, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createNewDictionary() {
        String languageCode = newLanguageCodeField.getText();
        String portText = newDictionaryPortField.getText();

        if (ErrorHandler.isValidLanguageCode(languageCode)) {
            resultArea.setText("Error: Invalid language code format.");
            return;
        }

        if (!ErrorHandler.isValidPort(portText)) {
            resultArea.setText("Error: Invalid port number.");
            return;
        }
        int port = Integer.parseInt(portText);
        try (Socket socket = new Socket("localhost", 4000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("CREATE_DICTIONARY");
            out.println(languageCode);
            out.println(port);

            String response = in.readLine();
            resultArea.setText(response);

        } catch (Exception ex) {
            resultArea.setText("Error: " + ex.getMessage());
        }
    }

    private void translateWord() {
        String languageCode = languageCodeField.getText();
        String word = wordField.getText();

        if (word.trim().isEmpty()) {
            resultArea.setText("Error: Word field cannot be empty.");
            return;
        }

        if (ErrorHandler.isValidLanguageCode(languageCode)) {
            resultArea.setText("Error: Invalid language code format.");
            return;
        }

        if (ErrorHandler.isNumeric(word)) {
            resultArea.setText("Error: Word cannot be a number.");
            return;
        }

        try (Socket socket = new Socket("localhost", 4000);
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
