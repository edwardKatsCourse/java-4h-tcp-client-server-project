package com.telran.server.repository;

import com.telran.server.dto.City;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CityRepository {

    private static final String STORAGE_FILENAME = "storage.csv";

    public List<City> getAll() {
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(new File(STORAGE_FILENAME)));
//            String line = null;
//            StringBuilder builder = new StringBuilder();
//
//            while ((line = reader.readLine()) != null) {
//                builder.append(line);
//                builder.append("\n");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //haifa,300
        //haifa,400
        //tel-aviv,200
        //tel-aviv,1000
        //rosh-haayin,200

        try (Stream<String> lines = Files.readAllLines(Paths.get(STORAGE_FILENAME)).stream()) {

            return lines.map(x -> x.split(",")) //string -> string [] | "haifa,300" -> ["haifa", "300"]
                    .map(x -> new City(x[0], Double.parseDouble(x[1])))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            System.out.println("Storage GET error");
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public void save(City city) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(STORAGE_FILENAME), true))) {
            writer.write(city.toString());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("Storage SAVE error");
            e.printStackTrace();
        }
    }
}
