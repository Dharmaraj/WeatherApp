package com.weatherapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.weatherapp.R;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by dharmaraj on 5/9/15.
 */
public class WeatherExpandableAdapter extends BaseExpandableListAdapter {


    private Activity mContext;
    private Map<String, List<String>> weatherInfoCollections;
    private List<String> weatherHeadingList;

    public WeatherExpandableAdapter(Activity mContext,
                                    List<String> weatherHeadingList,
                                    Map<String, List<String>> weatherInfoCollections) {

        this.mContext = mContext;
        this.weatherHeadingList = weatherHeadingList;
        this.weatherInfoCollections = weatherInfoCollections;

    }

    @Override
    public int getGroupCount() {
        return weatherHeadingList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return weatherInfoCollections.get(weatherHeadingList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return weatherHeadingList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return weatherInfoCollections.get(weatherHeadingList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupName = (String) getGroup(groupPosition);
        GroupViewHolder groupViewHolder;

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.weather_info_group, null);

            groupViewHolder = new GroupViewHolder();
            groupViewHolder.headerTV = (TextView) convertView.findViewById(R.id.headerTV);
            convertView.setTag(groupViewHolder);

        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        groupViewHolder.headerTV.setText(groupName);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String weatherChildInfo = (String) getChild(groupPosition, childPosition);

        ChildViewHolder childViewHolder;

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.weather_info_child, null);

            childViewHolder = new ChildViewHolder();
            childViewHolder.leftTV = (TextView) convertView.findViewById(R.id.leftTV);
            childViewHolder.rightTV = (TextView) convertView.findViewById(R.id.rightTV);
            convertView.setTag(childViewHolder);

        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        StringTokenizer stringTokenizer = new StringTokenizer(weatherChildInfo, "=;");
        while (stringTokenizer.hasMoreTokens()) {
            childViewHolder.leftTV.setText(stringTokenizer.nextToken());
            childViewHolder.rightTV.setText(stringTokenizer.nextToken());
        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    //For Group View
    class GroupViewHolder {
        TextView headerTV;
    }

    //For Child View
    class ChildViewHolder {
        TextView leftTV;
        TextView rightTV;
    }


}
