package com.example.souravshrestha.newsbullets;

/**
 * Created by Sourav Shrestha on 3/11/2018.
 */

public class EachBulletContent {

    String url;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public EachBulletContent(){

    }

    public EachBulletContent(String url,String date) {
        this.url = url;
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
