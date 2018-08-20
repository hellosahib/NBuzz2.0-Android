package tech.rtsproduction.news24;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<NewsData>> {

    private String STRING_URL;
    private final String LOG_TAG = NewsLoader.class.getSimpleName();
    private ArrayList<NewsData> dataArrayList = null;

    public NewsLoader(Context context, String url) {
        super(context);
        this.STRING_URL = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (dataArrayList != null) {
            deliverResult(dataArrayList);
        } else {
            forceLoad();
        }
    }

    @Override
    public ArrayList<NewsData> loadInBackground() {
        HttpHelper helper = new HttpHelper();
        try {
            String jsonResponse = helper.makeHTTPRequest(STRING_URL);
            dataArrayList = helper.getData(jsonResponse);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
        }
        return dataArrayList;
    }//END OF LOAD IN BACKGROUND

}
