package com.julia.tcpchat;

import javafx.scene.paint.Color;
import ru.nesferatos.fxsettings.Setting;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Юлия on 27.12.2015.
 */

public class AppSettings {
    @Setting(name = "адрес сервера")
    String address = "localhost";

    public String getComputername() {
        return computername;
    }

    @Setting(name = "имя компьютера")
    private String computername = "";

    public AppSettings() {
        try {
            computername = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Основные настройки";
    }
}
