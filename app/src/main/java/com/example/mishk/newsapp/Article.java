package com.example.mishk.newsapp;
//Object representing a single news item
public class Article {
    //Each article has @Title, @Author, @Date, @Section, @url
    private String mTitle;
    private String mAuthor;
    private String mSection;
    private String mDate;
    private String mWebUrl;

    //Constructor for new Article object
    public Article(String Title, String Author, String Section, String Date, String WebUrl){
        mTitle=Title;
        mAuthor=Author;
        mSection=Section;
        mDate=Date;
        mWebUrl=WebUrl;
    }
    //Get Title
    public String getTitle(){
        return mTitle;
    }
    //Get Author name
    public String getAuthor(){
        return mAuthor;
    }
    //Get name of the section
    public String getSection(){
        return mSection;
    }
    //Get date and time
    public String getDate(){
        return mDate;
    }
    //Get article web url
    public String getUrl(){
        return mWebUrl;
    }
}
