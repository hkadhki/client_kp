package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client("Вася");
        client.connect();
        Thread.sleep(10000000);
    }
}