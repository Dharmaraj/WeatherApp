package com.weatherapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weatherapp.model.Weather;

import java.util.List;

/**
 * Created by dharmaraj on 5/8/15.
 */
public class WeatherAdapter extends ArrayAdapter<Weather> {


    Context mContext;
    int layoutResourceId;
    List<Weather> weatherData = null;

    public WeatherAdapter(Context context, int layoutResourceId, List<Weather> objects) {
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.weatherData = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        //when convertView is not null, only update weather contents instead of inflating layout
        if (convertView == null) {
            // inflate  layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

//            holder.cityName = (TextView) convertView.findViewById(R.id.action_bar);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // get weather information based on position
        Weather weatherInfo = weatherData.get(position);


        return convertView;

    }

    class ViewHolder {

        TextView cityName;
    }
}
