package com.example.souravshrestha.newsbullets;

/**
 * Created by Sourav Shrestha on 3/20/2018.
 */

public class RegionalDetails {

    String date,lang,url/*,title*/;

    public RegionalDetails(){

    }

    public RegionalDetails(String date, String lang, String url/*, String title*/) {
        this.date = date;
        this.lang = lang;
        this.url = url;
//        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
}
