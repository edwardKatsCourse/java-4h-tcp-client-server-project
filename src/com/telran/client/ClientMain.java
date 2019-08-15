package com.telran.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) throws Exception {

        //localhost
        InetAddress ipAddress = InetAddress.getLocalHost();
        while (true) {

            try (
                    Socket clientRequest = new Socket(ipAddress, 8080);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientRequest.getOutputStream()));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientRequest.getInputStream()));
            ) {
                System.out.println("Message to server");

                String requestBody = new Scanner(System.in).nextLine();
                writer.write(requestBody + "\n");
                writer.flush();

                String responseFromServer = reader.readLine();
                if (responseFromServer != null && !responseFromServer.trim().isEmpty()) {
                    System.out.println("Response: ");
                    System.out.println(responseFromServer);
                } else {
                    System.out.println("Saved");
                }

                System.out.println("Perform another operation? (Y/N)");
                String userAnswer = new Scanner(System.in).nextLine();
                if (!userAnswer.equalsIgnoreCase("y")) {
                    break;
                }

            } catch (IOException e) {
                System.out.println("Nothing to do");
                e.printStackTrace();
            }

        }


    }
}
