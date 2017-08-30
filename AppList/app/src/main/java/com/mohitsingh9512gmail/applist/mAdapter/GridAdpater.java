package com.mohitsingh9512gmail.applist.mAdapter;

/**
 * Created by Mohit Singh on 8/4/2016.
 */

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohitsingh9512gmail.applist.R;

import org.w3c.dom.Text;

import java.util.List;

public class GridAdpater extends BaseAdapter {

    List<PackageInfo> packageList;
    Activity context;
    PackageManager packageManager;

    public GridAdpater(Activity context, List<PackageInfo> packageList,
                       PackageManager packageManager) {
        super();
        this.context = context;
        this.packageList = packageList;
        this.packageManager = packageManager;
    }

    private class ViewHolder {
        ImageView apkName;
        TextView apkname1;
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

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.apkgrid_item, null);
            holder = new ViewHolder();

            holder.apkName = (ImageView) convertView.findViewById(R.id.grid_item_image);
            holder.apkname1=(TextView)convertView.findViewById(R.id.grid_item_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PackageInfo packageInfo = (PackageInfo) getItem(position);
        Drawable appIcon = packageManager
                .getApplicationIcon(packageInfo.applicationInfo);
        appIcon.setBounds(0, 0, 120, 120);
        holder.apkName.setImageDrawable(appIcon);
        String appName = packageManager.getApplicationLabel(
                packageInfo.applicationInfo).toString();
        holder.apkname1.setText(appName);
        return convertView;
    }
}
