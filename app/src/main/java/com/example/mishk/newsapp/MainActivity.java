package com.example.mishk.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Article>>{
    public static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";
    private static final int LOADER_ID = 1;
    private NewsAdapter newsAdapter;
    private TextView emptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsAdapter = new NewsAdapter(this, new ArrayList<Article>());
        ListView newsView = findViewById(R.id.list);
        newsView.setAdapter(newsAdapter);
        emptyView = (TextView) findViewById(R.id.empty_view);
        newsView.setEmptyView(emptyView);
        //Reference for the code checking the state of internet connection: https://github.com/udacity/ud843-QuakeReport/commit/f0f9cd5ee7a8d67bd2e6f7e2539664a95499831b
        //Get a Connectivity Manager to check internet connection
        ConnectivityManager manager = (ConnectivityManager)  getSystemService(Context.CONNECTIVITY_SERVICE);
        //Request information about current active network
        NetworkInfo nI = manager.getActiveNetworkInfo();
        //Start fetching data if network connection available
        if (nI !=null && nI.isConnected()) {
            //Get a LoaderManager to interact with Loader
            LoaderManager loaderManager = getLoaderManager();
            //Initialize the loader.
            //Pass in LOADER_ID, null for bundle and current Activity for loaderCallbacks
            loaderManager.initLoader(LOADER_ID, null, this);
        }else{
            //Show error message
            View progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet_connection);
        }

        newsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Check what article was clicked
                Article currentArticle = newsAdapter.getItem(position);
                //Convert webUrl to URI object
                Uri uri = Uri.parse(currentArticle.getUrl());
                //Create new intent using created uri
                Intent openArticle = new Intent(Intent.ACTION_VIEW, uri);
                //Start the intent to open article in a browser
                startActivity(openArticle);
            }
        });
        }
    @Override
    public Loader<ArrayList<Article>> onCreateLoader (int i, Bundle bundle){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Retrieve a String value from the preferences
        String sectionSet = sharedPreferences.getString(getString(R.string.settings_section_key), getString(R.string.settings_section_default));
        //Retrieve  a String value from preferences
        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        //Break apart the URI string that was passed into URI parameter
        Uri baseUri =Uri.parse(GUARDIAN_REQUEST_URL);
        //Prepare baseUri that we built for adding query params
        Uri.Builder builder = baseUri.buildUpon();
        //Add query params and their values
        builder.appendQueryParameter("q", "Belarus");
        builder.appendQueryParameter("section", sectionSet);
        builder.appendQueryParameter("show_tags", "contributor");
        builder.appendQueryParameter("order-by", orderBy);
        builder.appendQueryParameter("api-key","b485c565-8eee-4af6-a97c-562eb03e280b" );
        Log.v("MainActivity", "uri: " + builder.toString());
      return new NewsLoader(this, builder.toString());
    }
    @Override
    public void onLoadFinished (Loader<ArrayList<Article>> loader, ArrayList<Article> news) {
        //Make progress bar invisible
        View progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        emptyView.setText(R.string.no_data);
        //Clear the previous version of adapter
        newsAdapter.clear();
        //Check for available list of Articles and update the UI
        if(news != null && !news.isEmpty()){
            newsAdapter.addAll(news);
        }
        }
        @Override
    public void onLoaderReset(Loader<ArrayList<Article>>loader){
        newsAdapter.clear();
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent (this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
