package ua.hillellit;

import ua.hillellit.model.ClientAccount;
import ua.hillellit.model.MonoThreadClientHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    public static List<ClientAccount> list = new ArrayList<>();
    public static int i = 1;
    static ExecutorService executeIt = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(3345)) {
            System.out.println("Server socket created, command console reader for listen to server commands");

            while (!server.isClosed()) {
                Socket client = server.accept();

                ClientAccount currentClient = new ClientAccount("Client-" + i, LocalTime.now(), client);
                list.add(currentClient);
                i++;
                for (ClientAccount ca : list) {
                    DataOutputStream oos = new DataOutputStream(ca.getSocket().getOutputStream());
                    oos.writeUTF("[Server] " + currentClient.getName() + " успешно подключился.");
                    oos.flush();
                }

                System.out.println("Connection accepted.");
                System.out.println(list);

                executeIt.execute(new MonoThreadClientHandler(client));
                System.out.print("Connection accepted.");
            }

            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

