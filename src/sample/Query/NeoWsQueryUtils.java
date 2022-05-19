package sample.Query;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.Models.ApodModel;
import sample.Models.NeoWsModel;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;

public class NeoWsQueryUtils {

    public static ArrayList<NeoWsModel> excutePost(String targetURL,ArrayList<LocalDate> dates) {
        URL url = createUrl(targetURL);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);         //makeHttpRequest is taking url object
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<NeoWsModel> response = extractFeatureFromJson(jsonResponse,dates);

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
            urlConnection.setReadTimeout(15000 /* milliseconds */);
            urlConnection.setConnectTimeout(20000 /* milliseconds */);
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


    private static ArrayList<NeoWsModel> extractFeatureFromJson(String jsonResponse,ArrayList<LocalDate> dates) {

        ArrayList<NeoWsModel> neoWsList = new ArrayList<>();
        if (jsonResponse == null) {
            return neoWsList;
        }

        try {

            JSONObject mainObj = new JSONObject(jsonResponse);
            JSONObject nearEarthObj = mainObj.getJSONObject("near_earth_objects");



            for (int i = 0;i<dates.size();i++){

                String date = dates.get(i).toString();
                System.out.println("Current date: "+date);
                JSONArray asteroids = nearEarthObj.getJSONArray(date);
                ArrayList<NeoWsModel> list = new ArrayList<>();

                for (int j = 0;j<asteroids.length();j++) {

                    NeoWsModel model = new NeoWsModel();

                    JSONObject currentAsteroid = asteroids.getJSONObject(j);

                    model.setNewsDate(date);

                    if (currentAsteroid.has("name")) {
                        model.setName(currentAsteroid.getString("name"));
                    }

                    if (currentAsteroid.has("absolute_magnitude_h")) {
                        model.setAbsoluteMagnitude(currentAsteroid.getDouble("absolute_magnitude_h"));
                    }

                    JSONObject diameters = currentAsteroid.getJSONObject("estimated_diameter");
                    if (diameters.has("meters")) {
                        JSONObject meters = diameters.getJSONObject("meters");
                        if (meters.has("estimated_diameter_min")) {
                            model.setMinDiameter(meters.getDouble("estimated_diameter_min"));
                        }
                        if (meters.has("estimated_diameter_max")) {
                            model.setMaxDiameter(meters.getDouble("estimated_diameter_max"));
                        }
                    }

                    if (currentAsteroid.has("is_potentially_hazardous_asteroid")) {
                        model.setPotentiallyHazardous(currentAsteroid.getBoolean("is_potentially_hazardous_asteroid"));
                    }

                    if (currentAsteroid.has("close_approach_data")) {
                        JSONArray closeAp = currentAsteroid.getJSONArray("close_approach_data");
                        if (closeAp.getJSONObject(0).has("close_approach_date_full")) {
                            model.setApproachDate(closeAp.getJSONObject(0).getString("close_approach_date_full"));
                        }
                        if (closeAp.getJSONObject(0).has("relative_velocity")) {
                            JSONObject velocity = closeAp.getJSONObject(0).getJSONObject("relative_velocity");
                            if (velocity.has("kilometers_per_second")) {
                                model.setApproachVelocity(velocity.getString("kilometers_per_second"));
                            }
                        }
                        if (closeAp.getJSONObject(0).has("miss_distance")) {
                            JSONObject distance = closeAp.getJSONObject(0).getJSONObject("miss_distance");
                            if (distance.has("kilometers")) {
                                model.setMissDistance(distance.getString("kilometers"));
                            }
                        }
                        if (closeAp.getJSONObject(0).has("orbiting_body")) {
                            model.setOrbitingBody(closeAp.getJSONObject(0).getString("orbiting_body"));
                        }
                    }

                    list.add(model);

                }

                neoWsList.addAll(list);

            }
         //   System.out.println(neoWsList.get(0).getAbsoluteMagnitude());

            return neoWsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return neoWsList;

    }

}
