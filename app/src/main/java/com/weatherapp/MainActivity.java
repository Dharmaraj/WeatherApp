package com.weatherapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.weatherapp.utilities.CurrentLocationInformation;
import com.weatherapp.utilities.HttpPath;
import com.weatherapp.utilities.ServerRequest;


public class MainActivity extends ActionBarActivity {


    double lat;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CurrentLocationInformation currentLocationInformation = new CurrentLocationInformation(getApplicationContext());
        if (currentLocationInformation.canGetCurrentLocation()) {
            lat = currentLocationInformation.getCurrentLatitude();
            longitude = currentLocationInformation.getCurrentLongitude();
            Toast.makeText(getApplicationContext(), "Lat=" + lat + ",Long=" + longitude, Toast.LENGTH_LONG).show();
        }

        new MyAsyncTask().execute();

    }


    private class MyAsyncTask extends AsyncTask<Void, Void, String> {


        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait, Weather Information Fetching");
            progressDialog.setCancelable(true);


        }

        @Override
        protected String doInBackground(Void... paramsArg) {
            return ServerRequest.
                    requestGetHttp(HttpPath.openWeatherPath + "?lat=" + lat + "&lon=" + longitude);
        }

        @Override
        protected void onPostExecute(String result) {

            displayRecordOnUI(result);
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
