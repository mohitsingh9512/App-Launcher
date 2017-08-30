package com.mohitsingh9512gmail.applist.mFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
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

import com.mohitsingh9512gmail.applist.AppActivity;
import com.mohitsingh9512gmail.applist.R;
import com.mohitsingh9512gmail.applist.mAdapter.AnalysisAdapter;
import com.mohitsingh9512gmail.applist.mAdapter.ApkAdapter;
import com.mohitsingh9512gmail.applist.mAdapter.GridAdpater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Mohit Singh on 8/4/2016.
 */

//Our class extending fragment
public class Tab1 extends Fragment {

    PackageManager packageManager;
    ListView apkList;
    GridView apkList1;
    View view;
    PackageInfo packageInfo;
    List<PackageInfo> packageList;
    List<PackageInfo> packageList1;
    List<PackageInfo> packageList2;
    List<PackageInfo> packageList3;
    List<PackageInfo> temp;

    SharedPreferences sharedPreferences1;
    public static final String MyPREFERENCES1="Analysis";
    SharedPreferences sharedPreferences5;
    public static final String MyPREFERENCES5 = "AppSettings" ;
    SharedPreferences sharedPreferences6;
    SharedPreferences sharedPreferences7;
    public static final String MyPREFERENCES6="AppNames";
    public static final String MyPREFERENCES7="SortedAppNames";
    public static int maxAnalysisValue=0;
    int grid_view_status;
    /**
     * Return whether the given PackgeInfo represents a system package or not.
     * User-installed packages (Market or otherwise) should not be denoted as
     * system packages.
     *
     * @param pkgInfo
     * @return boolean
     */
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*
         * Check the View of the App List if grid_view_status = 1 the gird view is current Drawer View
         */
        sharedPreferences5=getActivity().getSharedPreferences(MyPREFERENCES5, Context.MODE_APPEND);
        sharedPreferences6=getActivity().getSharedPreferences(MyPREFERENCES6, Context.MODE_PRIVATE);
        sharedPreferences7=getActivity().getSharedPreferences(MyPREFERENCES7, Context.MODE_PRIVATE);


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

        /*
         * Inflate view according to drawer view Grid or List
         */
        if(grid_view_status==1)
        {
            view=inflater.inflate(R.layout.tab1_grid, container, false);
        }
        else
        {
            view=inflater.inflate(R.layout.tab1_list, container, false);
        }

        packageManager = getActivity().getPackageManager();

        sharedPreferences1=getActivity().getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences1.edit();

        packageList = packageManager
                .getInstalledPackages(PackageManager.GET_PERMISSIONS);

        temp=new ArrayList<PackageInfo>();
        for(PackageInfo pkgInfo : packageList)
        {

            String appName = packageManager.getApplicationLabel(
                    pkgInfo.applicationInfo).toString();
            if(appName.length()>=4)
            {
                String s2=appName.subSequence(0,4).toString();
                Log.v("tag",s2);
               if(s2.equals("com.")||pkgInfo.packageName.equalsIgnoreCase("com.android.keyguard"))
               {
                   Log.v("tag",s2);

               }else
               {
                   temp.add(pkgInfo);
               }
            }

        }

        packageList=temp;
        maxAnalysisValue=0;

        /*
         * Analysis Shared preference stores the no of clicks per app
         * This is to calculate total clicks over all apps
         */
        for(PackageInfo pi:packageList)
        {

            if(sharedPreferences1.getInt(pi.packageName,-1)==-1)
            {
                editor.putInt(pi.packageName,0);
            }
            else
            {
                maxAnalysisValue=maxAnalysisValue+sharedPreferences1.getInt(pi.packageName,0);
            }
        }
        editor.apply();
        AnalysisAdapter.maxAnalysisValue=maxAnalysisValue;

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
        /*
         * Changing list View according the view checked
         */
        packageList3=new ArrayList<PackageInfo>();
        packageList3=menuChecked();
        if(grid_view_status==1)
        {
            apkList1=(GridView)view.findViewById(R.id.gridView_tab1);

            apkList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    PackageInfo packageInfo=(PackageInfo)parent.getItemAtPosition(position);

                    Intent intent=new Intent(getActivity(),AppActivity.class);
                    intent.putExtra("PackageInfo",packageInfo);
                    startActivity(intent);
                    onPause();

                }
            });

            apkList1.setAdapter(new GridAdpater(getActivity(), packageList3, packageManager));
        }
        else
        {
            apkList = (ListView)view.findViewById(R.id.applist);

            apkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    PackageInfo packageInfo=(PackageInfo)parent.getItemAtPosition(position);

                    Intent intent=new Intent(getActivity(),AppActivity.class);
                    intent.putExtra("PackageInfo",packageInfo);
                    startActivity(intent);
                    onPause();

                }
            });
            apkList.setAdapter(new ApkAdapter(getActivity(), packageList3, packageManager));
        }
    }

    public List<PackageInfo> menuChecked()
    {
        packageList1 = new ArrayList<PackageInfo>();
        int filterSysApps=sharedPreferences5.getInt("filter_SysApps",-1);
        int orderAlphabetically=sharedPreferences5.getInt("order_Alphabetically",-1);
        if(filterSysApps==1 && orderAlphabetically==1) {
            /*To filter out System apps*/
            for (PackageInfo pi : packageList) {
                boolean b = isSystemPackage(pi);
                if (!b) {
                    packageList1.add(pi);
                }
            }

            sharedPreferences6.edit().clear().apply();
            sharedPreferences7.edit().clear().apply();

            SharedPreferences.Editor editor = sharedPreferences6.edit();
            for (PackageInfo pkgInfo : packageList1) {
                String appName = packageManager.getApplicationLabel(
                        pkgInfo.applicationInfo).toString();
                String appPackage = pkgInfo.packageName;
                editor.putString(appName, appPackage);

            }
            editor.apply();
            Map<String, ?> allEntries;
            allEntries = sharedPreferences6.getAll();

            ArrayList<String> appName = new ArrayList<String>();
            Iterator myVeryOwnIterator = allEntries.keySet().iterator();
            while (myVeryOwnIterator.hasNext()) {
                String key = (String) myVeryOwnIterator.next();
                String value = (String) allEntries.get(key);

                appName.add(key);

            }

            Collections.sort(appName);

            SharedPreferences.Editor editor1 = sharedPreferences7.edit();
            for (String s : appName) {
                String appPackage = sharedPreferences6.getString(s, "default");
                editor1.putString(s, appPackage);
            }
            editor1.apply();

            Map<String, ?> allEntries1;
            allEntries1 = sharedPreferences7.getAll();
            for (Map.Entry<String, ?> entry : allEntries1.entrySet()) {
                Log.v("tag", entry.getKey() + ": " + entry.getValue().toString());
            }

            if (!allEntries1.isEmpty()) {
                packageList2 = new ArrayList<PackageInfo>();
                Iterator myVeryOwnIterator1 = allEntries1.keySet().iterator();
                while (myVeryOwnIterator1.hasNext()) {
                    String key1 = (String) myVeryOwnIterator1.next();
                    String value1 = (String) allEntries1.get(key1);

                    packageInfo = new PackageInfo();
                    try {
                        packageInfo = packageManager.getPackageInfo(value1, PackageManager.GET_PERMISSIONS);

                    } catch (PackageManager.NameNotFoundException e) {

                        e.printStackTrace();
                    } catch (Exception i) {
                        Log.v("tag", "Exception" + i);
                    }

                    packageList2.add(packageInfo);
                }
            }
        }
        else if(orderAlphabetically==1) {
            Map<String, ?> allEntries;
            allEntries = sharedPreferences6.getAll();

            ArrayList<String> appName = new ArrayList<String>();
            Iterator myVeryOwnIterator = allEntries.keySet().iterator();
            while (myVeryOwnIterator.hasNext()) {
                String key = (String) myVeryOwnIterator.next();
                String value = (String) allEntries.get(key);

                appName.add(key);

            }

            Collections.sort(appName);

            SharedPreferences.Editor editor1 = sharedPreferences7.edit();
            for (String s : appName) {
                String appPackage = sharedPreferences6.getString(s, "default");
                editor1.putString(s, appPackage);
            }
            editor1.apply();

            Map<String, ?> allEntries1;
            allEntries1 = sharedPreferences7.getAll();

            for (Map.Entry<String, ?> entry : allEntries1.entrySet()) {
                Log.v("tag", entry.getKey() + ": " + entry.getValue().toString());
            }

            if (!allEntries1.isEmpty()) {
                packageList2 = new ArrayList<PackageInfo>();
                Iterator myVeryOwnIterator1 = allEntries1.keySet().iterator();
                while (myVeryOwnIterator1.hasNext()) {
                    String key1 = (String) myVeryOwnIterator1.next();
                    String value1 = (String) allEntries1.get(key1);

                    packageInfo = new PackageInfo();
                    try {
                        packageInfo = packageManager.getPackageInfo(value1, PackageManager.GET_PERMISSIONS);
                        Log.v("tag", "inside try " + packageInfo.packageName);

                    } catch (PackageManager.NameNotFoundException e) {

                        e.printStackTrace();
                    } catch (Exception i) {
                        Log.v("tag", "Exception" + i);
                    }

                    packageList2.add(packageInfo);
                }
            }
        }
        else if(filterSysApps==1) {
            /*To filter out System apps*/
            packageList2 = new ArrayList<PackageInfo>();
            for (PackageInfo pi : packageList) {
                boolean b = isSystemPackage(pi);
                if (!b) {
                    packageList2.add(pi);
                }
            }
        }
        else
        {
            packageList2 = new ArrayList<PackageInfo>();
            packageList2=packageList;
        }
        return packageList2;
    }
}