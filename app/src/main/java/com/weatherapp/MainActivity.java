package com.weatherapp;


import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weatherapp.adapter.WeatherAdapter;
import com.weatherapp.model.SubWeather;
import com.weatherapp.model.Weather;
import com.weatherapp.utilities.CurrentLocationInformation;
import com.weatherapp.utilities.HttpPath;
import com.weatherapp.utilities.ServerRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class MainActivity extends ActionBarActivity {


    private double lat;
    private double longitude;
    private ProgressDialog progressDialog;

    private ListView weatherInfoListView;
    private Toolbar toolBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //take references of controls
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        weatherInfoListView = (ListView) findViewById(R.id.weatherInfoListView);



        if (!ServerRequest.isNetworkConnected(getApplicationContext())) {
            displayMessage("No Internet Access, Please Connect Internet");
        }
        getLocationAndWeatherInfo(savedInstanceState);


        //Refresh Button Event Handle
        TextView refreshTV = (TextView) toolBar.findViewById(R.id.refreshTV);
        refreshTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationAndWeatherInfo(savedInstanceState);
            }
        });

    }

    //display Toast message
    public void displayMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public void getLocationAndWeatherInfo(Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please Wait,Current Location Weather Information Fetching");
        progressDialog.setCancelable(true);
        progressDialog.show();

        CurrentLocationInformation currentLocationInformation = new CurrentLocationInformation(getApplicationContext());
        if (currentLocationInformation.canGetCurrentLocation()) {
            lat = currentLocationInformation.getCurrentLatitude();
            longitude = currentLocationInformation.getCurrentLongitude();
        }

        if (lat != 0 && longitude != 0) {

            getLoaderManager().initLoader(0, savedInstanceState,
                    new LoaderManager.LoaderCallbacks<String>() {

                        @Override
                        public Loader<String> onCreateLoader(int id, Bundle args) {
                            return new WeatherInfoAsyncTaskLoader(MainActivity.this, lat, longitude);
                        }

                        @Override
                        public void onLoadFinished(Loader<String> loader, String result) {

                            displayRecordOnUI(result);

                            if (progressDialog != null)
                                progressDialog.dismiss();

                        }


                        @Override
                        public void onLoaderReset(Loader<String> loader) {
                        }
                    }
            ).forceLoad();


        } else {
            displayMessage("Unable to get current location latitude and longitude");
            if (progressDialog != null)
                progressDialog.dismiss();

        }


    }

    public void displayRecordOnUI(String result) {
        try {

            List<Weather> weatherList = new ArrayList<>();

            Weather weather = new Weather();

            JSONObject jsonObject = new JSONObject(result);

            //Get sys Object JSON
            if (!jsonObject.isNull("sys")) {
                JSONObject jsonObject1 = jsonObject.getJSONObject("sys");
                weather.setCountry(jsonObject1.getString("country"));
                weather.setSunrise(convertUnixTimeIntoCurrentTime(Long.parseLong(jsonObject1.getString("sunrise"))));
                weather.setSunset(convertUnixTimeIntoCurrentTime(Long.parseLong(jsonObject1.getString("sunset"))));
            }


            //Get Weather Array JSON
            if (!jsonObject.isNull("weather")) {
                JSONArray jsonArray = jsonObject.getJSONArray("weather");

                List<SubWeather> subWeatherList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    SubWeather subWeather = new SubWeather();

                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                    subWeather.setMainStr(jsonObject2.getString("main"));
                    subWeather.setDescription(jsonObject2.getString("description"));
                    subWeather.setIcon(jsonObject2.getString("icon"));

                    subWeatherList.add(subWeather);
                }

                weather.setSubWeatherList(subWeatherList);

            }

            //Get base
            if (!jsonObject.isNull("base")) {
                weather.setBase(jsonObject.getString("base"));
            }

            //Get Main Object
            if (!jsonObject.isNull("main")) {
                JSONObject jsonObject2 = jsonObject.getJSONObject("main");
                weather.setTemp((Double.parseDouble(jsonObject2.getString("temp")) - 273.15) + " C");
                weather.setTempMin((Double.parseDouble(jsonObject2.getString("temp_min")) - 273.15) + " C");
                weather.setTempMax((Double.parseDouble(jsonObject2.getString("temp_max")) - 273.15) + " C");
                weather.setPressure(jsonObject2.getString("pressure") + " hPa");
                weather.setSeaLevel(jsonObject2.getString("sea_level") + " hPa");
                weather.setGrndLevel(jsonObject2.getString("grnd_level") + " hPa");
                weather.setHumidity(jsonObject2.getString("humidity") + " %");
            }

            //Get Wind Object
            if (!jsonObject.isNull("wind")) {
                JSONObject jsonObject3 = jsonObject.getJSONObject("wind");
                weather.setWindSpeed(jsonObject3.getString("speed") + " mps");
                weather.setWindDeg(jsonObject3.getString("deg") + " deg");
            }

            //Get Clouds Object
            if (!jsonObject.isNull("clouds")) {
                JSONObject jsonObject4 = jsonObject.getJSONObject("clouds");
                weather.setCloudsAll(jsonObject4.getString("all") + " %");
            }

            //Get Rain Object
            if (!jsonObject.isNull("rain")) {
                JSONObject jsonObject5 = jsonObject.getJSONObject("rain");
                weather.setRain3H(jsonObject5.getString("3h") + " mm");
            }

            //Get Snow Object
            if (!jsonObject.isNull("snow")) {
                JSONObject jsonObject6 = jsonObject.getJSONObject("snow");
                weather.setSnow3H(jsonObject6.getString("3h") + " mm");
            }

            //Get City Name, Data receive and City id
            if (!jsonObject.isNull("dt")) {
                weather.setDt(jsonObject.getString("dt"));
            }
            if (!jsonObject.isNull("id")) {
                weather.setId(jsonObject.getString("id"));
            }
            if (!jsonObject.isNull("name")) {
                weather.setCityName(jsonObject.getString("name"));
            }

            //Add Weather into List
            weatherList.add(weather);

            TextView toolbarTitle = (TextView) toolBar.findViewById(R.id.toolbarTitle);
            toolbarTitle.setText(weather.getCityName());

            //Initialize Custom ArrayAdapter and set on ListView
            WeatherAdapter weatherAdapter = new WeatherAdapter(this, R.layout.weather_info_list, weatherList);
            weatherInfoListView.setAdapter(weatherAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    //Convert UNIX time into current time system
    public String convertUnixTimeIntoCurrentTime(long unixSeconds) {
        Date date = new Date(unixSeconds * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
        sdf.setTimeZone(TimeZone.getDefault()); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);
        return formattedDate;
    }

}


//Getting Weather Information using AsyncTaskLoader
class WeatherInfoAsyncTaskLoader extends AsyncTaskLoader<String> {

    double lat;
    double lon;

    public WeatherInfoAsyncTaskLoader(Context context, double lat, double lon) {
        super(context);
        this.lat = lat;
        this.lon = lon;
    }


    @Override
    public String loadInBackground() {
        return ServerRequest.
                requestGetHttp(HttpPath.openWeatherPath + "?lat=" + lat + "&lon=" + lon);
    }
}

