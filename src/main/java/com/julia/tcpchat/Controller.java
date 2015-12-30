package com.julia.tcpchat;

import com.julia.tcpchat.protocol.Message;
import com.julia.tcpchat.protocol.Protocol;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.nesferatos.fxsettings.FxSettings;

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

    @FXML
    public Label statusLabel;

    @FXML
    public MenuItem settingsItem;

    Socket s;
    OutputStream outputStream;
    InputStream inputStream;
    BufferedReader in;
    AppSettings appSettings = new AppSettings();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        settingsItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FxSettings fxSettings = new FxSettings(appSettings);

                Stage stage = new Stage();
                stage.setTitle("My New Stage Title");
                stage.setScene(new Scene(fxSettings, 450, 450));
                stage.show();
            }

        });

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Message msg = new Message();
                msg.setText(inputText.getText());
                msg.setNickName(appSettings.getComputername());
                byte[] data = Protocol.serialize(msg);
                try {
                    System.out.println(inputText.getText());
                    outputStream.write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputText.clear();

            }
        });

        openItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                    s = new Socket(appSettings.address, 10000);

//                    in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                    inputStream = s.getInputStream();

                    outputStream = s.getOutputStream();

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (!s.isClosed()) {
                                    Message message = Protocol.deserialize(inputStream);

                                    Platform.runLater(() -> chatArea.appendText("\n"+ message.getNickName() +": "+ message.getText()));
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    t.start();

                    statusLabel.setText("соединение установлено");

                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    alert.setTitle("Ошибка соединения");
                    alert.setHeaderText("Ошибка соединения");
                    alert.setContentText("Не удалось подключиться к серверу");

                    alert.showAndWait();
                }


            }
        });
    }
}
