package ua.hillellit.model;

import ua.hillellit.MultiThreadServer;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class MonoThreadClientHandler implements Runnable {
    private static Socket clientDialog;

    public MonoThreadClientHandler(Socket client) {
        MonoThreadClientHandler.clientDialog = client;
    }

    @Override
    public void run() {

        try {
            DataInputStream in = new DataInputStream(clientDialog.getInputStream());
            System.out.println("DataInputStream created");

            DataOutputStream out = new DataOutputStream(clientDialog.getOutputStream());
            System.out.println("DataOutputStream  created");

            while (!clientDialog.isClosed()) {
                System.out.println("Server reading from channel");
                String entry = in.readUTF();
                System.out.println("READ from clientDialog message - " + entry);
                if (entry.equalsIgnoreCase("file")) {
                    saveFile();
                }
                if (entry.equalsIgnoreCase("exit")) {
                    System.out.println("Client initialize connections suicide ...");
                    out.writeUTF("Server reply - " + entry + " - OK");
                    MultiThreadServer.list.removeIf(s -> s.getSocket().equals(clientDialog));
                    Thread.sleep(3000);
                    break;
                }
                System.out.println("Server try writing to channel");
                out.writeUTF("Server reply - " + entry + " - OK");
                System.out.println("Server Wrote message to clientDialog.");
                out.flush();
            }
            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");

            in.close();
            out.close();
            clientDialog.close();

            System.out.println("Closing connections & channels - DONE.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void saveFile() throws IOException {
        InputStream in = clientDialog.getInputStream();

        // Читаем размер файл
        byte[] fileSizeBuf = new byte[8];
        in.read(fileSizeBuf, 0, 8);
        // Преобразовываем в long
        ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
        buf.put(fileSizeBuf);
        buf.flip();
        long fileSize = buf.getLong();

        FileOutputStream fos = new FileOutputStream("src\\main\\resources\\data.txt");
        byte[] bytes = new byte[4000 * 1024];


        int count;
        while (fileSize > 0 && (count = in.read(bytes)) > 0) {
            fos.write(bytes, 0, count);
        }

        fos.close();
        in.close();
    }
}
