package com.example.souravshrestha.newsbullets;

/**
 * Created by Sourav Shrestha on 3/10/2018.
 */

public class RegionalDetailsMain {

    private String title;
    private String image;

    public RegionalDetailsMain() {

    }

    public RegionalDetailsMain(String title, String image) {
        this.title = title;
        this.image = image;
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
