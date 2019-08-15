package com.telran.server.dto;

public class City {

    private String city;
    private Double bill;

    public City(String city, Double bill) {
        this.city = city;
        this.bill = bill;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getBill() {
        return bill;
    }

    public void setBill(Double bill) {
        this.bill = bill;
    }

    @Override
    public String toString() {
        return String.format("%s,%s", this.city, this.bill);
    }
}
