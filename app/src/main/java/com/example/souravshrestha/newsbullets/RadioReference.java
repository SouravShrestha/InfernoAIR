package com.example.souravshrestha.newsbullets;

/**
 * Created by Sourav Shrestha on 3/15/2018.
 */

public class RadioReference {

    String title;
    String url;
    String image;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCount(String count) {
        this.count = count;
    }

    String language;

    public String getCount() {
        return count;
    }

    public void setCount(String count,String language) {
        this.count = count;
        this.language = language;
    }

    String count;
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public RadioReference() {

    }

    public RadioReference(String title, String url,String image,String count) {
        this.title = title;
        this.url = url;
        this.image = image;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
