package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public TextArea inputText;

    @FXML
    public Button sendButton;

    @FXML
    public TextArea chatArea;

    @FXML
    public MenuItem openItem;

    Socket s;
    OutputStream outputStream;
    BufferedReader in;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            s = new Socket("192.168.0.101", 10000);

            in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            outputStream = s.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }



        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!s.isClosed()) {
                        String line = in.readLine();

                        Platform.runLater(() -> chatArea.appendText("\n" + line));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    System.out.println(inputText.getText());
                    outputStream.write((inputText.getText() + "\n").getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputText.clear();

            }
        });
//        openItem.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                try {
//                    s = new Socket("localhost", 10000);
//                    inputStream = s.getInputStream();
//
//                    outputStream = s.getOutputStream();
//
//
//                    Thread t = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            byte[] buf = new byte[1];
//                            try {
//                                while (!s.isClosed()) {
//                                        inputStream.read(buf);
//                                        chatArea.appendText(new String(buf));
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//
//                    t.start();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });


    }
}
