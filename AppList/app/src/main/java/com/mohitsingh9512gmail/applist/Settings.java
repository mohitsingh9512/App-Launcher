package com.mohitsingh9512gmail.applist;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by Mohit Singh on 8/13/2016.
 */
public class Settings extends AppCompatActivity {

    TextView manageCategories;
    TextView themes;
    TextView drawer_view;
    final Context context=this;
    RadioGroup radioGroup;
    RadioGroup radioGroup1;
    RadioButton drawer_grid_view_rb;
    RadioButton drawer_list_view_rb;
    RadioButton radioAppTheme;
    RadioButton radioAppTheme1;
    Button btnSelectTheme;
    SharedPreferences sharedPreferences5;
    public static final String MyPREFERENCES5 = "AppSettings" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Settings");

        sharedPreferences5=getSharedPreferences(MyPREFERENCES5,MODE_APPEND);

        manageCategories=(TextView)findViewById(R.id.manageCategories_tv);
        //
        // themes=(TextView)findViewById(R.id.theme_tv);
        //
        drawer_view=(TextView)findViewById(R.id.drawer_view_tv);

        manageCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent manageCategory = new Intent(Settings.this,ManageCategory.class);
                startActivity(manageCategory);

            }
        });

        /*
         * The theme doesnt change .. problem in function or SDK or android support
         *
         *
        themes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(context);
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_theme);
                dialog.setTitle("Select Your Theme");

                radioGroup=(RadioGroup)dialog.findViewById(R.id.radioTheme);
                radioAppTheme=(RadioButton)dialog.findViewById(R.id.radioAppTheme);
                radioAppTheme1=(RadioButton)dialog.findViewById(R.id.radioAppTheme1);
                btnSelectTheme=(Button)dialog.findViewById(R.id.setTheme);
                btnSelectTheme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int selectedId =radioGroup.getCheckedRadioButtonId();
                        switch(selectedId)
                        {
                            case R.id.radioAppTheme:
                                getApplication().setTheme(R.style.AppTheme);
                                Toast.makeText(getApplicationContext(),"yes",Toast.LENGTH_LONG).show();
                                break;
                            case R.id.radioAppTheme1:
                                getApplication().setTheme(R.style.AppTheme1);
                                break;

                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        */

        drawer_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog1=new Dialog(context);
                //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.dialog_drawer_view);
                dialog1.setTitle("Select Your Theme");
                radioGroup1=(RadioGroup)dialog1.findViewById(R.id.drawer_view_rg);
                drawer_grid_view_rb=(RadioButton)dialog1.findViewById(R.id.drawer_view_grid_rb);
                drawer_list_view_rb=(RadioButton)dialog1.findViewById(R.id.drawer_view_list_rb);

                int grid_view_status=sharedPreferences5.getInt("grid_view_status",-1);
                if(grid_view_status==1)
                {
                    drawer_grid_view_rb.setChecked(true);
                }
                else
                {
                    drawer_grid_view_rb.setChecked(false);
                }

                btnSelectTheme=(Button)dialog1.findViewById(R.id.setDrawer);
                btnSelectTheme.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SharedPreferences.Editor editor=sharedPreferences5.edit();
                        int selectedId =radioGroup1.getCheckedRadioButtonId();
                        switch(selectedId)
                        {
                            case R.id.drawer_view_list_rb:
                                sharedPreferences5.edit().remove("grid_view_status");
                                editor.putInt("grid_view_status",0);
                                editor.apply();
                                break;
                            case R.id.drawer_view_grid_rb:
                                sharedPreferences5.edit().remove("grid_view_status");
                                editor.putInt("grid_view_status",1);
                                editor.apply();
                                break;

                        }
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
            }
        });

    }
}
