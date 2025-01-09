package com.f.backend.entity;

import jakarta.persistence.*;

@Entity
public class Hotel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String address;
    private String rating;
    private double maximum_price;
    private double minimum_price;
    private String image;

    /**
     * By default it is in lazy. We use EAGER because of \:
     * - When it call we need instance data. Lazy take more time then EAGER
     */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private Location location;

    public Hotel() {
    }

    public Hotel(Integer id, String name, String address, String rating, double maximum_price, double minimum_price,
            String image, Location location) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.maximum_price = maximum_price;
        this.minimum_price = minimum_price;
        this.image = image;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public double getMaximum_price() {
        return maximum_price;
    }

    public void setMaximum_price(double maximum_price) {
        this.maximum_price = maximum_price;
    }

    public double getMinimum_price() {
        return minimum_price;
    }

    public void setMinimum_price(double minimum_price) {
        this.minimum_price = minimum_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
