package com.example.sushitime.models;

public class HomeVerModel { int image;
    String name;
    String prix;
    String timing;
    String rating;

    public HomeVerModel(int image, String name, String prix, String timing, String rating) {
        this.image = image;
        this.name = name;
        this.prix = prix;
        this.timing = timing;
        this.rating = rating;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPrix() {
        return prix;
    }

    public String getTiming() {
        return timing;
    }

    public String getRating() {
        return rating;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    public String getprix() {
        return getPrix();
    }
}
