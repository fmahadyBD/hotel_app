package com.f.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String image;
    private double price;
    private int area;
    private int adultNo;
    private int childNo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    public Room() {
    }

    public Room(int id, String name, String image, double price, int area, int adultNo, int childNo, Hotel hotel) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.area = area;
        this.adultNo = adultNo;
        this.childNo = childNo;
        this.hotel = hotel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getAdultNo() {
        return adultNo;
    }

    public void setAdultNo(int adultNo) {
        this.adultNo = adultNo;
    }

    public int getChildNo() {
        return childNo;
    }

    public void setChildNo(int childNo) {
        this.childNo = childNo;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", area=" + area +
                ", adultNo=" + adultNo +
                ", childNo=" + childNo +
                ", hotel=" + hotel +
                '}';
    }
}
