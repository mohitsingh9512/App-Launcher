package com.mohitsingh9512gmail.applist.mFragments;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mohitsingh9512gmail.applist.R;
import com.mohitsingh9512gmail.applist.mAdapter.DialogSubListCategoryAdapter;
import com.mohitsingh9512gmail.applist.mAdapter.GridViewAdapter;

import java.util.Map;

/**
 * Created by Mohit Singh on 8/5/2016.
 */
public class Tab3 extends Fragment {
    View view;
    GridView category_list;
    GridView category_sublist_grid;
    PackageManager packageManager;
    SharedPreferences sharedPreferences1;
    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences3;
    public static final String MyPREFERENCES1 = "Analysis" ;
    public static final String MyPREFERENCES2="AllCategory";
    public static final String MyPREFERENCES3 = "AppCategory" ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.tab3, container, false);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<String> list=new ArrayList<String>();

        sharedPreferences1=getActivity().getSharedPreferences(MyPREFERENCES1,Context.MODE_PRIVATE);
        sharedPreferences2=getActivity().getSharedPreferences(MyPREFERENCES2,Context.MODE_PRIVATE);
        sharedPreferences3=getActivity().getSharedPreferences(MyPREFERENCES3,Context.MODE_PRIVATE);

        packageManager = getActivity().getPackageManager();

        Map<String, ?> allEntries;
        allEntries = sharedPreferences2.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.v("tag", entry.getKey() + ": " + entry.getValue().toString());
        }

        if(!allEntries.isEmpty()) {
            Iterator myVeryOwnIterator = allEntries.keySet().iterator();
            while (myVeryOwnIterator.hasNext()) {
                String key = (String) myVeryOwnIterator.next();
                String value = (String) allEntries.get(key);
                list.add(value);
            }
        }

        Log.v("tag","onCreateView of Tab3");

        category_list = (GridView)view.findViewById(R.id.gridView1);
        category_list.setAdapter(new GridViewAdapter(getActivity(),list));
        category_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str=(String)parent.getItemAtPosition(position);

                final Dialog dialog = new Dialog(getActivity());
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_category_sublist);
                dialog.setTitle(str+ " CATEGORY");

                Context context=dialog.getContext();

                category_sublist_grid=(GridView)dialog.findViewById(R.id.gridView2);
                ArrayList<String> category_sublist=new ArrayList<String>();
                List<PackageInfo> packageList=new ArrayList<PackageInfo>();


                Map<String, ?> allEntries1;
                allEntries1 = sharedPreferences3.getAll();
                for (Map.Entry<String, ?> entry : allEntries1.entrySet()) {
                    Log.v("tag", entry.getKey() + ": " + entry.getValue().toString());
                }

                if(!allEntries1.isEmpty()) {
                    Iterator myVeryOwnIterator = allEntries1.keySet().iterator();
                    while (myVeryOwnIterator.hasNext()) {
                        String key1 = (String) myVeryOwnIterator.next();
                        String value1 = (String) allEntries1.get(key1);
                        if (value1.equalsIgnoreCase(str)) {
                            category_sublist.add(key1);
                            try {
                                packageList.add(packageManager.getPackageInfo(key1, 0));
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
                dialog.show();
                category_sublist_grid.setAdapter(new DialogSubListCategoryAdapter(getActivity(),packageList,packageManager));
                category_sublist_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PackageInfo packageInfo1=(PackageInfo)parent.getItemAtPosition(position);

                        try{
                            Intent intent = packageManager.getLaunchIntentForPackage(packageInfo1.packageName);

                            if(intent != null) {
                                SharedPreferences.Editor editor=sharedPreferences1.edit();
                                int ret=sharedPreferences1.getInt(packageInfo1.packageName,-1);
                                if(ret!=-1)
                                {
                                    ret++;
                                    editor.putInt(packageInfo1.packageName,ret);
                                    editor.apply();
                                }
                                startActivity(intent);
                            }
                        } catch(ActivityNotFoundException e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        } catch(Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

    }
}