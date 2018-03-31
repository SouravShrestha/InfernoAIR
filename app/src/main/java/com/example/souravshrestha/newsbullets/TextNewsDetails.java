package com.example.souravshrestha.newsbullets;

/**
 * Created by Sourav Shrestha on 3/26/2018.
 */

public class TextNewsDetails {

    String image;
    String title;

    public TextNewsDetails(){

    }

    public TextNewsDetails(String image, String title) {
        this.image = image;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
