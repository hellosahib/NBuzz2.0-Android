package tech.rtsproduction.news24;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HttpHelper {
    private static final String LOG_TAG = HttpHelper.class.getSimpleName();
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;

    public HttpHelper() {
        //Empty Constructor
    }

    public String makeHTTPRequest(String newsURL) throws IOException {
        Log.e(LOG_TAG, "Enter Backround Task");
        String jsonResponse = null;
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        URL url = createUrl(newsURL);
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                Log.e(LOG_TAG, "Success Code 200");
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                //DO Nothing
                Log.e(LOG_TAG, "Response Code Not 200");
            }
        } catch (IOException e) {
            Log.i(LOG_TAG, e.getLocalizedMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream io) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        if (io != null) {
            InputStreamReader reader = new InputStreamReader(io, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line != null) {
                jsonBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return jsonBuilder.toString();
    }

    private URL createUrl(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    public ArrayList<NewsData> getData(String jsonData) {
        if (jsonData == null) {
            return null;
        } else {
            //JSON IS PARSED HERE
            ArrayList<NewsData> arrayList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject responseObject = jsonObject.getJSONObject("response");
                JSONArray resultsObject = responseObject.getJSONArray("results");
                for (int i = 0; i < resultsObject.length(); i++) {
                    JSONObject result = resultsObject.getJSONObject(i);
                    JSONArray tagsObject = result.getJSONArray("tags");
                    String authorName;
                    if(tagsObject.length()>0){
                        JSONObject tagZero = tagsObject.getJSONObject(0);
                         authorName = tagZero.optString("webTitle");
                    }else{
                        authorName = "Unnamed";
                    }
                    arrayList.add(new NewsData(result.optString("webTitle"), result.optString("sectionName"), result.optString("webUrl"), result.optString("webPublicationDate"),authorName));
                }
            } catch (JSONException e) {
                Log.e("JSON Parsing", e.getLocalizedMessage());
            }
            return arrayList;
        }//ELSE ENDS HERE
    }


}
