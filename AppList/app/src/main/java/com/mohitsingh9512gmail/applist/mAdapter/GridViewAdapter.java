package com.mohitsingh9512gmail.applist.mAdapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mohitsingh9512gmail.applist.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mohit Singh on 8/12/2016.
 */
public class GridViewAdapter extends BaseAdapter {

    List<String> categories;
    Activity context;
    Resources res;
    public GridViewAdapter(Activity context, ArrayList<String> categories) {
        super();
        this.context = context;
        this.categories=categories;
        res=context.getResources();
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    public int getCount() {
        return categories.size();
    }

    public Object getItem(int position) {
        return categories.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_list_item, null);
            holder = new ViewHolder();

            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
            holder.textView = (TextView) convertView.findViewById(R.id.grid_item_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        String textName=(String)getItem(position);
        holder.textView.setText(textName);
        if(textName.charAt(0)=='A'||textName.charAt(0)=='H'||textName.charAt(0)=='0'||textName.charAt(0)=='V')
        {
            holder.imageView.setImageResource(R.drawable.grid_item_bg1);
        }
        else if(textName.charAt(0)=='B'||textName.charAt(0)=='I'||textName.charAt(0)=='P'||textName.charAt(0)=='W')
        {
            holder.imageView.setImageResource(R.drawable.grid_item_bg2);
        }
        else if(textName.charAt(0)=='C'||textName.charAt(0)=='J'||textName.charAt(0)=='Q'||textName.charAt(0)=='X')
        {
            holder.imageView.setImageResource(R.drawable.grid_item_bg3);
        }
        else if(textName.charAt(0)=='D'||textName.charAt(0)=='K'||textName.charAt(0)=='R'||textName.charAt(0)=='Y')
        {
            holder.imageView.setImageResource(R.drawable.grid_item_bg4);
        }
        else if(textName.charAt(0)=='E'||textName.charAt(0)=='L'||textName.charAt(0)=='S'||textName.charAt(0)=='Z')
        {
            holder.imageView.setImageResource(R.drawable.grid_item_bg5);
        }
        else if(textName.charAt(0)=='F'||textName.charAt(0)=='M'||textName.charAt(0)=='T')
        {
            holder.imageView.setImageResource(R.drawable.grid_item_bg6);
        }
        else if(textName.charAt(0)=='G'||textName.charAt(0)=='N'||textName.charAt(0)=='U')
        {
            holder.imageView.setImageResource(R.drawable.grid_item_bg7);
        }

        /*Random r = new Random();
        int i1 = r.nextInt(7)+1;
        String mDrawableName="grid_item_bg"+i1;
        int resID = res.getIdentifier(mDrawableName , "drawable", context.getPackageName());
        holder.imageView.setImageResource(resID);
        */

        return convertView;
    }
}

