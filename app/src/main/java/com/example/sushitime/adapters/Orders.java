package com.example.sushitime.adapters;

public class Orders {
    String name;
    String prix;
    String rating;

    public Orders() {
    }

    public String getRating() {
        return rating;
    }

    public String getPrix() {
        return prix;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }
}
