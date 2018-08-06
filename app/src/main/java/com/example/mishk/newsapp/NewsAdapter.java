package com.example.mishk.newsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class NewsAdapter extends ArrayAdapter<Article>{
    //Constructor for custom adapter
    public NewsAdapter (Activity context, ArrayList<Article> news){
    super(context, 0, news);
    }
    //Inflate layout for ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(null == listItemView){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        //Receive data from current Article object
        Article currentArticle = getItem(position);
        //Get section name and set text for the designated view
        TextView section = (TextView) listItemView.findViewById(R.id.section);
        String rubric = currentArticle.getSection();
        section.setText(rubric);
        //Get article title and set text for the designated view
        TextView title = (TextView) listItemView.findViewById(R.id.title);
        title.setText(currentArticle.getTitle());
        //Get author`s name and set text for the corresponding view
        TextView authorName = (TextView) listItemView.findViewById(R.id.author);
        //Check if author field is available for current article and make view invisible , if name is null
        if (null == currentArticle.getAuthor()){
            authorName.setVisibility(View.GONE);
        }else{
            authorName.setText(currentArticle.getAuthor());
        }
        //Reference for the code below: https://github.com/Yosolita1978/RankedFeedApp/blob/master/app/src/main/java/co/yosola/ranked/ArticleAdapter.java
        //Get date info and set text for the designated view
        String date = currentArticle.getDate();
        Date dateNoFormat = formatDate(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        String dateFin = dateFormat.format(dateNoFormat);
        TextView day = (TextView)listItemView.findViewById(R.id.date);
        //Check if date field is present for current article and make view invisible, if date is null
        if(null == dateFin){
            day.setVisibility(View.GONE);
        }else {
            day.setText(dateFin);
        }
        //Return layout for ListView
        return listItemView;
    }
  private Date formatDate (String dateFin){
      TimeZone timeZone = TimeZone.getTimeZone("UTC");
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      dateFormat.setTimeZone(timeZone);
      try{
          return dateFormat.parse(dateFin);
      }catch(ParseException e){
          e.printStackTrace();
      }
       return null;
  }
}
