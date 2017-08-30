package com.mohitsingh9512gmail.applist.mAdapter;

/**
 * Created by Mohit Singh on 8/4/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mohitsingh9512gmail.applist.R;

import java.util.List;
import java.util.StringTokenizer;

public class AnalysisAdapter extends BaseAdapter {

    List<PackageInfo> packageList;
    Activity context;
    PackageManager packageManager;
    SharedPreferences sharedPreferences1;
    public static final String MyPREFERENCES="Analysis";
    SharedPreferences.Editor editor;
    public static int maxAnalysisValue;

    public AnalysisAdapter(Activity context, List<PackageInfo> packageList,
                           PackageManager packageManager) {
        super();
        this.context = context;
        this.packageList = packageList;
        this.packageManager = packageManager;
        sharedPreferences1=context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor=sharedPreferences1.edit();

    }

    private class ViewHolder {
        TextView apkName;
        ProgressBar progressBar;
        TextView percentage;
    }

    public int getCount() {
        return packageList.size();
    }

    public Object getItem(int position) {
        return packageList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.analysis_item, null);
            holder = new ViewHolder();

            holder.apkName = (TextView) convertView.findViewById(R.id.appname);
            holder.progressBar=(ProgressBar) convertView.findViewById(R.id.progressBar);
            holder.percentage=(TextView) convertView.findViewById(R.id.textViewPercentage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PackageInfo packageInfo = (PackageInfo) getItem(position);
        Drawable appIcon = packageManager
                .getApplicationIcon(packageInfo.applicationInfo);
        String appName = packageManager.getApplicationLabel(
                packageInfo.applicationInfo).toString();
        appIcon.setBounds(0, 0, 60, 60);
        holder.apkName.setCompoundDrawables(appIcon, null, null, null);
        holder.apkName.setCompoundDrawablePadding(15);
        holder.apkName.setText(appName);
        holder.progressBar.setProgress(sharedPreferences1.getInt(packageInfo.packageName,0));
        int currentProgress=sharedPreferences1.getInt(packageInfo.packageName,0);
        holder.progressBar.setMax(maxAnalysisValue);
        if(maxAnalysisValue!=0)
        {
            int ratio=(currentProgress*100/maxAnalysisValue);
            holder.percentage.setText(String.valueOf(ratio));
        }
        else
        {
            holder.percentage.setText(String.valueOf(0));
        }
        return convertView;
    }
}
