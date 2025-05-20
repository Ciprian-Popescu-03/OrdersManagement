package com.ordersmanagement.Model;

public class Client {
    private int id;
    private String name;
    private String address;
    private int age;

    public Client() {
        // Required by reflection
    }

    public Client(String name, String address, int age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public Client(int id, String name, String address, int age) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getAge() {
        return age;
    }

    public void setName(String johnathanDoe) {
        this.name = johnathanDoe;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ")";
    }

}
