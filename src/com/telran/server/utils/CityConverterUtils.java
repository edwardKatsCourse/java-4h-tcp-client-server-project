package com.telran.server.utils;

import com.telran.server.dto.City;

public class CityConverterUtils {

    public static City convertToObject(String body) {
        String [] data = body.split(",");

        //haifa,300.10
        return new City(data[0], Double.parseDouble(data[1]));
    }

    public String convertToString(City city) {
        return String.format("%s,%s", city.getCity(), city.getBill());
    }
}
