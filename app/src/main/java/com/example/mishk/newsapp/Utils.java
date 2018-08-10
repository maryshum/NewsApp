package com.example.mishk.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

//Helper methods for requesting data from Guardian
public final class Utils {
    //Tag for Log messages
    private final static String LOG_TAG = Utils.class.getSimpleName();
    //Private constructor
    private Utils(){

    }

    //Return URL object from String
    private static URL createUrl(String stringUrl){
        URL url=null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error creating URL", e);
        }
         return url;
    }
    //Make request to the given URL and return response as a String
    public static String makeHttpRequest(URL url)throws IOException {
        String jsonResponse = "" ;
        //If URL is null, return early
        if (jsonResponse == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            //In case of successful request read input stream and parse response
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream (inputStream);
            }else{
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG, "Problem retrieving JSON response", e);
        }finally{
            if(urlConnection==null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
    return jsonResponse;
    }
    //Convert inputStream into String to read JSON response
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder streamOutput = new StringBuilder();
        if(inputStream != null){
            InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader buffer = new BufferedReader(reader);
            String line = buffer.readLine();
            while(line != null){
                streamOutput.append(line);
                line = buffer.readLine();
            }
        }
    return streamOutput.toString();
    }
    //Reference used for the below code: https://github.com/Yosolita1978/RankedFeedApp/blob/master/app/src/main/java/co/yosola/ranked/QueryUtils.java
    //Return a list of Articles parsed from JSON
    private static ArrayList<Article> extractDataFromJSON(String jsonResponse){

        //If the JSON String is empty or null, return early
        if (TextUtils.isEmpty(jsonResponse)){
            return null;
        }
        ArrayList<Article> news = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject info = root.getJSONObject("response");
            JSONArray results = info.getJSONArray("results");
            //Loop through each article in the results array
            for (int i=0; i< results.length(); i++){
              //Get JSON Article object for position i
               JSONObject article = results.getJSONObject(i);
               //Extract webTitle for article title
                String title = article.getString("webTitle");
                //Extract sectionName for the name of section the article is published to
                String section = article.getString("sectionName");
                //Extract webUrl for web address of the article
                String webUrl = article.getString("webUrl");
                //Extract date when the article was published
                String date = article.getString("webPublicationDate") ;
                //Extract the name of the author of the article
                JSONArray tags = article.getJSONArray("tags");
                String author = null;
                if(tags.length()>=1){
                    JSONObject tag = tags.getJSONObject(0);
                    author= "By: "+ tag.optString("webTitle");
                }
                //Create Article object with parsed data
                Article article1 = new Article(title, author, section,date, webUrl);
                //Add Article to list of news
                news.add(article1);
            }

        }catch (JSONException e){
            Log.e("Utils", "Problem parsing the JSON news results", e);
        }
        //Return the list of news
   return news;
    }
    //**Reference used for the code below: QuakeReport app.
    //Query Guardian and receive Article object representing single article on the given topic
    public static ArrayList<Article> fetchNewsData (String requestUrl){
        //Create URL object
        URL url = createUrl(requestUrl);
        //Make HTTP request and receive JSON response
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        //Extract fields from JSON response and create Article object
        ArrayList<Article> news = extractDataFromJSON(jsonResponse);
        return news;

    }
}
