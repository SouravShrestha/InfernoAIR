package com.example.souravshrestha.newsbullets;

/**
 * Created by Sourav Shrestha on 3/26/2018.
 */

public class EachNewsContentMain {

    String body;
    String date;
    String head;
    String language;

    public EachNewsContentMain(){

    }

    public EachNewsContentMain(String body, String date, String head, String language) {
        this.body = body;
        this.date = date;
        this.head = head;
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

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }



}
