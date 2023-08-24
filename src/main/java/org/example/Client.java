package org.example;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Client {

    static Logger LOGGER;

    static {
        try (FileInputStream ins = new FileInputStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getGlobal();
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    private static String name;
    private static Socket clientSocket;
    private static BufferedReader reader;

    private static BufferedReader in;
    private static BufferedWriter out;

    public Client(String name) {
        this.name = name;
    }

    private static Runnable send = () -> {
        String word;
        while (true) {
            try {
                word = reader.readLine();
                LOGGER.log(Level.INFO, word);
                if (word.equals("/exit")) {
                    out.write("/exit" + "\n");
                    break;
                } else {
                    out.write(name + ": " + word + "\n");
                }
                out.flush();
            } catch (IOException e) {

            }
        }
    };

    private static Runnable write = () -> {
        String str;
        try {
            while (true) {
                str = in.readLine();
                LOGGER.log(Level.INFO, str);
                System.out.println(str);
            }
        } catch (IOException e) {

        }
    };


    public void connect() throws IOException {
        String settings = "";
        try (BufferedReader br = new BufferedReader(new FileReader("settings.txt"))) {
            String s;
            while ((s = br.readLine()) != null) {
                settings = settings + "\n" + s;
            }
            System.out.println(settings);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        int port = Integer.parseInt(settings.substring(settings.indexOf("port:") + 6, settings.indexOf(";")));
        String host = settings.substring(settings.indexOf("host:") + 6, settings.indexOf(";", settings.indexOf(";") + 1));

        clientSocket = new Socket(host, port);
        System.out.println("подключено");
        reader = new BufferedReader(new InputStreamReader(System.in));
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        Thread threadSend = new Thread(send);
        threadSend.start();
        Thread threadWrite = new Thread(write);
        threadWrite.start();

        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}


