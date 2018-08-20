package tech.rtsproduction.news24;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {
    ViewPager pager;
    ProgressBar progressBar;
    TextView stateTextView, sectionTextView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    String STRING_URL = "https://content.guardianapis.com/search";
    String sectionName = "news";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.settingsMenu: {
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                        return true;
                    }
                    case R.id.businessMenu: {
                        sectionName = getString(R.string.section1);
                        fetchData();
                        return true;
                    }
                    case R.id.gamesMenu: {
                        sectionName = getString(R.string.section2);
                        fetchData();
                        return true;
                    }
                    case R.id.politicsMenu: {
                        sectionName = getString(R.string.section3);
                        fetchData();
                        return true;
                    }
                    case R.id.techMenu: {
                        sectionName = getString(R.string.section4);
                        fetchData();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        if (item.getItemId() == R.id.refreshMenu) {
            getLoaderManager().restartLoader(0,null,this);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //LOADER METHODS
    @Override
    public Loader<ArrayList<NewsData>> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        stateTextView.setVisibility(View.GONE);
        return new NewsLoader(MainActivity.this, buildURL());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsData>> loader, ArrayList<NewsData> data) {
        progressBar.setVisibility(View.GONE);
        if (data == null||data.size() == 0 ) {
            stateTextView.setVisibility(View.VISIBLE);
        }
        pager.setAdapter(new CustomPagerAdapter(MainActivity.this, data));
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsData>> loader) {
        pager.setAdapter(null);
    }

    //USER-DEFINED FUNCTIONS
    private void fetchData() {
        if(!checkInternet()){
            sectionTextView.setText(sectionName);
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    private boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        if (!isConnected) {
            stateTextView.setText(R.string.checkinternet);
            stateTextView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.viewPagerMain);
        drawerLayout = findViewById(R.id.drawerLayoutMain);
        navigationView = findViewById(R.id.navViewMain);
        progressBar = findViewById(R.id.progressBarMain);
        stateTextView = findViewById(R.id.noDataTextMain);
        sectionTextView = findViewById(R.id.sectionNameTextMenu);
        toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private String buildURL() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pageSize = sharedPreferences.getString(getString(R.string.page_size_key), getString(R.string.page_size_default));
        String orderBy = sharedPreferences.getString(getString(R.string.order_by_key), getString(R.string.order_by_default));

        Uri baseURI = Uri.parse(STRING_URL);
        Uri.Builder builder = baseURI.buildUpon();

        builder.appendQueryParameter("api-key", BuildConfig.GuardianAPI);
        builder.appendQueryParameter("order-by", orderBy);
        builder.appendQueryParameter("page-size", pageSize);
        builder.appendQueryParameter("show-tags", "contributor");
        builder.appendQueryParameter("section", sectionName);
        return builder.toString();
    }
}
