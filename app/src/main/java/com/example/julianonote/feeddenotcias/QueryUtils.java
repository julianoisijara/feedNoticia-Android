package com.example.julianonote.feeddenotcias;

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
import java.util.List;


public final class QueryUtils {


    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }


    public static List<Feed> fetchFeedData(String requestUrl) {


        URL url = createUrl(requestUrl);


        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Feed> Feeds = extractFeatureFromJson(jsonResponse);

        return Feeds;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";


        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Feed JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Feed> extractFeatureFromJson(String FeedJSON) {

        if (TextUtils.isEmpty(FeedJSON)) {
            return null;
        }


        List<Feed> Feeds = new ArrayList<>();


        try {

            JSONObject baseJsonResponse = new JSONObject(FeedJSON);

            JSONObject FeedObject = baseJsonResponse.getJSONObject("response");
            JSONArray FeedNoticia = FeedObject.getJSONArray("results");

            for (int i = 0; i < FeedNoticia.length(); i++) {

                JSONObject FeddNoticia = FeedNoticia.getJSONObject(i);

                String tema = FeddNoticia.getString("sectionName");
                String titulo = FeddNoticia.getString("webTitle");
                String time = FeddNoticia.getString("webPublicationDate");
                String url = FeddNoticia.getString("webUrl");

                Feed feed = new Feed(tema, titulo, time, url);

                Feeds.add(feed);
            }

        } catch (JSONException e) {


            Log.e("QueryUtils", "Problem parsing the Feed JSON results", e);
        }

        return Feeds;
    }

}