package com.mohitsingh9512gmail.applist.mFragments;

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
import android.widget.ListView;
import android.widget.Toast;

import com.mohitsingh9512gmail.applist.R;
import com.mohitsingh9512gmail.applist.mAdapter.ApkAdapter;
import com.mohitsingh9512gmail.applist.mAdapter.GridAdpater;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Mohit Singh on 8/4/2016.
 */
//Our class extending fragment
public class Tab2 extends Fragment {

    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String MyPREFERENCES1 = "Analysis" ;
    SharedPreferences sharedPreferences5;
    public static final String MyPREFERENCES5 = "AppSettings" ;
    int grid_view_status;

    List<PackageInfo> packageList2;
    PackageManager packageManager;
    ListView apkList2;
    GridView apkList3;
    View view;
    PackageInfo packageInfo;

        //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.v("tag","onCreateView of Tab2");
        sharedPreferences5=getActivity().getSharedPreferences(MyPREFERENCES5, Context.MODE_APPEND);

        if(sharedPreferences5.getInt("grid_view_status",-1)==-1)
        {
            SharedPreferences.Editor editor=sharedPreferences5.edit();
            editor.putInt("grid_view_status",0);
            editor.apply();

        }
        else
        {
            grid_view_status=sharedPreferences5.getInt("grid_view_status",-1);
        }
        Log.v("tag","onCreateView of Tab1");
        if(grid_view_status==1)
        {
            view=inflater.inflate(R.layout.tab2_grid, container, false);
        }
        else
        {
            view=inflater.inflate(R.layout.tab2_list, container, false);
        }

        changeView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        changeView();
    }

    public void changeView()
    {
        packageManager=getActivity().getPackageManager();

        sharedPreferences=getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences1=getActivity().getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);


        Map<String, ?> allEntries;
        allEntries = sharedPreferences.getAll();

        if(!allEntries.isEmpty())
        {
            packageList2=new ArrayList<PackageInfo>();
            Iterator myVeryOwnIterator = allEntries.keySet().iterator();
            while(myVeryOwnIterator.hasNext()) {
                String key=(String)myVeryOwnIterator.next();
                String value=(String)allEntries.get(key);

                Log.v("tag",""+value);
                packageInfo=new PackageInfo();
                try {
                    packageInfo=packageManager.getPackageInfo(value,PackageManager.GET_PERMISSIONS);
                    Log.v("tag","inside try "+packageInfo.packageName);

                } catch (PackageManager.NameNotFoundException e) {

                    e.printStackTrace();
                }catch (Exception i)
                {
                    Log.v("tag","Exception"+i);
                }

                packageList2.add(packageInfo);
            }
            if(grid_view_status==1)
            {
                apkList3 = (GridView)view.findViewById(R.id.gridView_tab2);


                apkList3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                    }
                });
                apkList3.setAdapter(new GridAdpater(getActivity(), packageList2, packageManager));
            }
            else
            {
                apkList2 = (ListView)view.findViewById(R.id.applist2);

                apkList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                    }
                });
                apkList2.setAdapter(new ApkAdapter(getActivity(), packageList2, packageManager));
            }

        }

    }

}