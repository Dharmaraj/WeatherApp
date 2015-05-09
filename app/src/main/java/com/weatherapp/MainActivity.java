package com.weatherapp;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.weatherapp.utilities.CurrentLocationInformation;
import com.weatherapp.utilities.HttpPath;
import com.weatherapp.utilities.ServerRequest;


public class MainActivity extends ActionBarActivity {


    double lat;
    double longitude;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!ServerRequest.isNetworkConnected(getApplicationContext())) {
            displayMessage("No Internet Access, Please Connect Internet");
        }
        getLocationAndWeatherInfo(savedInstanceState);
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
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
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

