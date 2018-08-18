package tech.rtsproduction.news24;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {
    ViewPager pager;
    ProgressBar progressBar;
    TextView stateTextView;
    Toolbar toolbar;

    String STRING_URL = "https://content.guardianapis.com/search?show-tags=contributor&api-key="+BuildConfig.GuardianAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        checkInternet();
        fetchData();
    }

    //OVERRIDE FUNCTIONS

    //MENU OPTIONS METHOD
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.refreshMenu){
            getLoaderManager().initLoader(0,null,this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //LOADER METHODS
    @Override
    public Loader<ArrayList<NewsData>> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        return new NewsLoader(MainActivity.this, STRING_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsData>> loader, ArrayList<NewsData> data) {
        progressBar.setVisibility(View.GONE);
        if (data == null) {
            stateTextView.setVisibility(View.VISIBLE);
        }
        pager.setAdapter(new CustomPagerAdapter(MainActivity.this, data));
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsData>> loader) {
        pager.setAdapter(null);
    }

    //USER-DEFINED FUNCTIONS

    private void fetchData(){
        getLoaderManager().initLoader(0, null, this);
    }
    private void checkInternet(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        if (!isConnected) {
            stateTextView.setText(R.string.checkinternet);
        }
    }
    private void initUI(){
        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.viewPagerMain);
        progressBar = findViewById(R.id.progressBarMain);
        stateTextView = findViewById(R.id.noDataTextMain);
        toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }
}
