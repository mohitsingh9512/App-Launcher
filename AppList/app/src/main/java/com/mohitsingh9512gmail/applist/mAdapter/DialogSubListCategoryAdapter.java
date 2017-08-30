package com.mohitsingh9512gmail.applist.mAdapter;

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

import java.util.List;

/**
 * Created by Mohit Singh on 8/14/2016.
 */
public class DialogSubListCategoryAdapter extends BaseAdapter {
    List<PackageInfo> packageList;
    Activity context;
    PackageManager packageManager;

    public DialogSubListCategoryAdapter(Activity context, List<PackageInfo> packageList,
                      PackageManager packageManager) {
        super();
        this.context = context;
        this.packageList = packageList;
        this.packageManager = packageManager;
    }

    private class ViewHolder {
        ImageView imageView;
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
            convertView = inflater.inflate(R.layout.dialog_category_sublist_item, null);
            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_category_sublist_item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PackageInfo packageInfo = (PackageInfo) getItem(position);
        Drawable appIcon = packageManager
                .getApplicationIcon(packageInfo.applicationInfo);
        holder.imageView.setImageDrawable(appIcon);

        return convertView;
    }
}
