package com.example.souravshrestha.newsbullets;

/**
 * Created by Sourav Shrestha on 3/22/2018.
 */

public class EachRegionContent {

    String date,lang,url;

    public EachRegionContent(){

    }

    public EachRegionContent(String date, String lang, String url) {
        this.date = date;
        this.lang = lang;
        this.url = url;
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
}
