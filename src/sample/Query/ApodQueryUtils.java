package sample.Query;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.Models.ApodModel;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class ApodQueryUtils {

    public static ArrayList<ApodModel> excutePost(String targetURL) {
        URL url = createUrl(targetURL);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);         //makeHttpRequest is taking url object
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<ApodModel> response = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return response;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
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

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                // Log.i(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                JOptionPane.showInternalMessageDialog(null, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
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


    private static ArrayList<ApodModel> extractFeatureFromJson(String jsonResponse) {

        ArrayList<ApodModel> apodList = new ArrayList<>();
        if (jsonResponse == null) {
            return apodList;
        }

        try {

            JSONArray responseArray = new JSONArray(jsonResponse);

            for (int i = 0; i < responseArray.length(); i++) {

                JSONObject currentApod = responseArray.getJSONObject(i);

                String copyRight = null;
                if (currentApod.has("copyright")) {
                    copyRight = currentApod.getString("copyright");
                }

                String date = null;
                if (currentApod.has("date")) {
                    date = currentApod.getString("date");
                }

                String explanation = null;
                if (currentApod.has("explanation")) {
                    explanation = currentApod.getString("explanation");
                }

                String url = null;
                if (currentApod.has("url")) {
                    url = currentApod.getString("url");
                }

                String mediaType = null;
                if (currentApod.has("media_type")) {
                    mediaType = currentApod.getString("media_type");
                }

                String thumbnail = null;
                if (mediaType.equals("video")) {
                    if (currentApod.has("thumbnail_url")) {
                        thumbnail = currentApod.getString("thumbnail_url");
                    }
                }

                String title = null;
                if (currentApod.has("title")) {
                    title = currentApod.getString("title");
                }

                ApodModel apod = new ApodModel();
                apod.setCopyRight(copyRight);
                apod.setDate(date);
                apod.setExplanation(explanation);
                apod.setMediaType(mediaType);
                apod.setThumbnailLink(thumbnail);
                apod.setTitle(title);
                apod.setUrl(url);

                apodList.add(apod);

            }
            return apodList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apodList;

    }

}