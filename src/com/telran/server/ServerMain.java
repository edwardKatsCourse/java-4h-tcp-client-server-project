package com.telran.server;

import com.telran.server.dto.City;
import com.telran.server.repository.CityRepository;
import com.telran.server.utils.CityConverterUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerMain {

    private static CityRepository cityRepository = new CityRepository();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            System.out.println("Waiting for client to connect");
            try (
                    //blocks here till request arrives
                    Socket clientRequest = serverSocket.accept();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientRequest.getInputStream()));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientRequest.getOutputStream()));
            ) {
                String requestBody = reader.readLine();
//                System.out.println("Request body: " + requestBody);

                //save;Haifa,100.90 -> ["save", "Haifa,100.90"]
                String[] headerBody = requestBody.split(";");

                //save
                String header = headerBody[0];

                //"Haifa,100.90"
                String body = headerBody[1];

                System.out.println("Header -> " + header);
                System.out.println("body -> " + body);

                String response = "";

                if (header.equalsIgnoreCase("save")) {
                    save(body);
                }

                if (header.equalsIgnoreCase("get")) {
                    //get
                    String searchParameter = body;


                    if (searchParameter.equalsIgnoreCase("bill-count")) {
                        response = getBillCountResponse();
                    }

                    if (searchParameter.equalsIgnoreCase("city-count")) {
                        response = getCityCountResponse();
                    }
                }


                writer.write(response + "\n");
                writer.flush();


            } catch (IOException e) {
                System.out.println("Client communication error: closing connection with client");
//                e.printStackTrace();
            }

        }

        //receiving request info
        //returning response


    }

    private static void save(String body) {
        //converter     -   "Haifa,100.9" -> new City("Haifa", 100.9)
        //repository    -   saves and gets List<City>
        //repository - called classes, that save/get info to/from Database
        City city = CityConverterUtils.convertToObject(body);
        cityRepository.save(city);
    }

    private static String getBillCountResponse() {
        List<City> cities = cityRepository.getAll();
        Map<String, Double> result = cities.stream()
                .collect(Collectors.toMap(
                        x -> x.getCity(),
                        y -> y.getBill(),
                        (bill1, bill2) -> bill1 + bill2
                ));
        return result.entrySet().stream()
                .map(x -> String.format("%s=%s", x.getKey(), x.getValue()))
                .collect(Collectors.joining(","));

        //city_1=bill_1,city2=bill_2..
    }

    private static String getCityCountResponse() {
        List<City> cities = cityRepository.getAll();

        //Haifa, 2
        //Tel-Aviv, 3
        Map<String, Long> result = cities
                .stream()
                .collect(Collectors.groupingBy(
                        City::getCity,
                        Collectors.counting()
                ));

        //Haifa=2,Tel-Aviv=3,Netania=10
        return result.entrySet()
                .stream()
                .map(x -> String.format("%s=%s", x.getKey(), x.getValue()))
                .collect(Collectors.joining(","));
    }
}
