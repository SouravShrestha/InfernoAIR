package com.example.souravshrestha.newsbullets;

/**
 * Created by Sourav Shrestha on 3/26/2018.
 */

public class EachNewsContentMain {

    String body;
    String date;
    String heading;
    String language;

    public EachNewsContentMain(){

    }

    public EachNewsContentMain(String body, String date, String heading, String language) {
        this.body = body;
        this.date = date;
        this.heading = heading;
        this.language = language;
    }
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String head) {
        this.heading = head;
    }



}
