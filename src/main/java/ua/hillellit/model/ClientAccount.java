package ua.hillellit.model;

import java.net.Socket;
import java.time.LocalTime;

public class ClientAccount {
    String name;
    LocalTime time;
    Socket socket;

    public ClientAccount(String name, LocalTime time, Socket socket) {
        this.name = name;
        this.time = time;
        this.socket = socket;
    }

    @Override
    public String toString() {
        return "ClientAccount{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", socket=" + socket +
                '}';
    }

    public String getName() {
        return name;
    }

    public Socket getSocket() {
        return socket;
    }
}

