package com.example.mishk.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<Article>>{
    //Tag for log messages
    private static final String LOG_TAG = NewsLoader.class.getName();
    //Query URL
    private String mUrl;
    //Constructor for new NewsLoader
    public NewsLoader (Context context, String url){
        super(context);
        mUrl=url;
    }
    @Override
    protected void onStartLoading (){
        forceLoad();
    }
    //Load in background thread
    @Override
    public ArrayList<Article> loadInBackground (){
        if(mUrl== null){
            return null;
        }
        //Make a network request, parse response and extract list of news articles
        return Utils.fetchNewsData(mUrl);

    }
}
