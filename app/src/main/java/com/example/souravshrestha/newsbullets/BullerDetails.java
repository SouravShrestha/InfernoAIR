package com.example.souravshrestha.newsbullets;

/**
 * Created by Sourav Shrestha on 3/10/2018.
 */

public class BullerDetails {

    private String title;
    private String image;
    private String count;

    public BullerDetails() {

    }

    public BullerDetails(String title, String image,String count) {
        this.title = title;
        this.image = image;
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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
