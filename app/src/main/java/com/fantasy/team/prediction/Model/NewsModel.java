package com.fantasy.team.prediction.Model;

public class NewsModel {

    public String newsHead;
    public String newsImg;
    public String newsContent;
    public String newsDt;
    public String newsCategory;

    public NewsModel() {

    }


    public NewsModel(String newsHead, String newsImg, String newsContent, String newsDt, String newsCategory) {
        this.newsHead = newsHead;
        this.newsImg = newsImg;
        this.newsContent = newsContent;
        this.newsDt = newsDt;
        this.newsCategory = newsCategory;
    }

    public String getNewsHead() {
        return newsHead;
    }

    public void setNewsHead(String newsHead) {
        this.newsHead = newsHead;
    }

    public String getNewsImg() {
        return newsImg;
    }

    public void setNewsImg(String newsImg) {
        this.newsImg = newsImg;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsDt() {
        return newsDt;
    }

    public void setNewsDt(String newsDt) {
        this.newsDt = newsDt;
    }

    public String getNewsCategory() {
        return newsCategory;
    }

    public void setNewsCategory(String newsCategory) {
        this.newsCategory = newsCategory;
    }
}
