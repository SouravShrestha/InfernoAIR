package com.example.souravshrestha.newsbullets;

/**
 * Created by Sourav Shrestha on 3/15/2018.
 */

public class ArchiveReference {

    String date,title,url,image,lang;

    public ArchiveReference(){

    }

    public ArchiveReference(String date, String url, String title, String lang, String image) {
        this.date = date;
        this.url = url;
        this.title = title;
        this.lang = lang;
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
