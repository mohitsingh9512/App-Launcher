package com.mohitsingh9512gmail.applist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.view.View;

//Implementing the interface OnTabSelectedListener to our MainActivity
//This interface would help in swiping views
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    SharedPreferences sharedPreferences5;
    public static final String MyPREFERENCES5 = "AppSettings" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("All Apps"));
        tabLayout.addTab(tabLayout.newTab().setText("Favourite"));
        tabLayout.addTab(tabLayout.newTab().setText("Category"));
        tabLayout.addTab(tabLayout.newTab().setText("Analysis"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(1);
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        /*
         * Check the order and filter of menu item
         */

        int order_Alphabetically=0;
        int filter_SysApps=0;
        sharedPreferences5=getSharedPreferences(MyPREFERENCES5, Context.MODE_APPEND);

        if(sharedPreferences5.getInt("order_Alphabetically",-1)==-1)
        {
            SharedPreferences.Editor editor=sharedPreferences5.edit();
            editor.putInt("order_Alphabetically",0);
            editor.putInt("filter_SysApps",0);
            editor.commit();

        }
        else
        {
            order_Alphabetically=sharedPreferences5.getInt("order_Alphabetically",-1);
            filter_SysApps=sharedPreferences5.getInt("filter_SysApps",-1);
        }

        getMenuInflater().inflate(R.menu.action_menu, menu);

        boolean state;
        boolean state1;
        if(order_Alphabetically==0)
        {
            state=false;
        }
        else
        {
            state=true;
        }
        MenuItem item = menu.findItem(R.id.orderAlphabetically);
        item.setChecked(state);


        if(filter_SysApps==0)
        {
            state1=false;
        }
        else
        {
            state1=true;
        }
        MenuItem item1 = menu.findItem(R.id.filterSysApps);
        item1.setChecked(state1);


        /*
        * Used for float service
        *
        MenuItem itemSwitch = menu.findItem(R.id.mySwitch);
        itemSwitch.setActionView(R.layout.use_switch);
        final Switch sw=(Switch)menu.findItem(R.id.mySwitch).getActionView().findViewById(R.id.action_switch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    Toast.makeText(getBaseContext(), "Switch button", Toast.LENGTH_SHORT).show();
                    startService(new Intent(getApplicationContext(), FloatingViewService.class));
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Switch button Removed", Toast.LENGTH_SHORT).show();
                    stopService(new Intent(getApplicationContext(), FloatingViewService.class));
                }
            }
        });
        */

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent intent= new Intent(MainActivity.this,Settings.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.orderAlphabetically){

            item.setChecked(!item.isChecked());
            int myInt = item.isChecked() ? 1 : 0;
            sharedPreferences5.edit().remove("order_Alphabetically");
            SharedPreferences.Editor editor = sharedPreferences5.edit();
            editor.putInt("order_Alphabetically",myInt );
            editor.apply();
        }

        if (id == R.id.filterSysApps){

            item.setChecked(!item.isChecked());
            int myInt = item.isChecked() ? 1 : 0;
            sharedPreferences5.edit().remove("filter_SysApps");
            SharedPreferences.Editor editor = sharedPreferences5.edit();
            editor.putInt("filter_SysApps",myInt );
            editor.apply();
        }


        return super.onOptionsItemSelected(item);
    }
}