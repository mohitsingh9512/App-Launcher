package com.mohitsingh9512gmail.applist;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.Toast;
import android.view.LayoutInflater;

import java.io.File;
import java.util.Iterator;

import java.util.Map;

/**
 * Created by Mohit Singh on 8/10/2016.
 */
public class AppActivity extends AppCompatActivity {
    Button btnLaunch,btnInfo,btnMarkFav,btnShare;
    Spinner spinnerCategory;
    PackageInfo packageInfo;
    PackageManager packageManager;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;
    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences3;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String MyPREFERENCES1 = "Analysis" ;
    public static final String MyPREFERENCES2 = "AllCategory" ;
    public static final String MyPREFERENCES3 = "AppCategory" ;
    final Context context=this;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity);
        sharedPreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences1=getSharedPreferences(MyPREFERENCES1, Context.MODE_PRIVATE);
        sharedPreferences2=getSharedPreferences(MyPREFERENCES2, Context.MODE_APPEND);
        sharedPreferences3=getSharedPreferences(MyPREFERENCES3, Context.MODE_APPEND);

        Bundle extras=getIntent().getExtras();

        if(extras!=null)
        {
            packageInfo=extras.getParcelable("PackageInfo");
        }
        else
        {
            Toast.makeText(getApplicationContext(),"This is null ",Toast.LENGTH_LONG).show();
        }

        packageManager=getPackageManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(getPackageManager().getApplicationLabel(packageInfo.applicationInfo));


        btnLaunch=(Button)findViewById(R.id.btnLaunch);
        btnInfo=(Button)findViewById(R.id.btnInfo);
        btnMarkFav=(Button)findViewById(R.id.btnMarkFav);
        btnShare=(Button)findViewById(R.id.btShare);
        spinnerCategory=(Spinner)findViewById(R.id.spinnerCategories);


        adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Select Your Category");
        adapter.add("New Category");

        populateSpinner();

        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                String selectedItem=parent.getSelectedItem().toString();

                if(selectedItem.equals("Select Your Category"))
                {
                    if(sharedPreferences3.getString(packageInfo.packageName,null)!=null)
                    {
                        String name=sharedPreferences3.getString(packageInfo.packageName,null);
                        int spinnerPosition = adapter.getPosition(name);
                        spinnerCategory.setSelection(spinnerPosition);

                    }
                }

                else if(selectedItem.equals("New Category"))
                {

                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.addcategory_prompt, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    final EditText userInput = (EditText) promptsView
                            .findViewById(R.id.editTextDialogUserInput);

                    // set dialog message
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            adapter.add(userInput.getText().toString());
                                            spinnerCategory.setAdapter(adapter);
                                            String compareValue =(userInput.getText().toString().toUpperCase());
                                            if (!compareValue.equals(null)) {
                                                int spinnerPosition = adapter.getPosition(compareValue);
                                                spinnerCategory.setSelection(spinnerPosition);
                                            }

                                            SharedPreferences.Editor editor2=sharedPreferences2.edit();
                                            editor2.putString(userInput.getText().toString(),userInput.getText().toString());
                                            editor2.apply();
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                            String compareValue = "Select Your Category";
                                            if (!compareValue.equals(null)) {
                                                int spinnerPosition = adapter.getPosition(compareValue);
                                                spinnerCategory.setSelection(spinnerPosition);
                                            }

                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                }
                else
                {
                    spinnerCategory.setSelection(position);
                    String category=spinnerCategory.getSelectedItem().toString();
                    if(sharedPreferences3.getString(packageInfo.packageName,null)==null)
                    {
                        SharedPreferences.Editor editor3=sharedPreferences3.edit();
                        editor3.putString(packageInfo.packageName,category);
                        editor3.apply();
                    }
                    else
                    {
                        sharedPreferences3.edit().remove(packageInfo.packageName).apply();
                        SharedPreferences.Editor editor3=sharedPreferences3.edit();
                        editor3.putString(packageInfo.packageName,category);
                        editor3.apply();

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMail(new File(packageInfo.applicationInfo.sourceDir));

            }
        });

        btnLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences1.edit();
                int ret=sharedPreferences1.getInt(packageInfo.packageName,-1);
                if(ret!=-1)
                {
                    ret++;
                    editor.putInt(packageInfo.packageName,ret);
                    editor.apply();
                }

                try {
                    Intent intent = packageManager.getLaunchIntentForPackage(packageInfo.packageName);

                    if (intent != null) {
                        startActivity(intent);
                    }
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        String ifStar=sharedPreferences.getString(packageInfo.packageName,null);
        if(ifStar!=null)
        {
            btnMarkFav.setBackgroundResource(R.mipmap.yellowstar_icon);
        }
        btnMarkFav.setTag("hollowStar");

        btnMarkFav.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor editor=sharedPreferences.edit();

            @Override
            public void onClick(View v) {
                if(btnMarkFav.getTag().equals("hollowStar"))
                {
                    btnMarkFav.setTag("yellowStar");
                    btnMarkFav.setBackgroundResource(R.mipmap.yellowstar_icon);
                    editor.putString(packageInfo.packageName,packageInfo.packageName);
                    editor.apply();
                }

                else if(btnMarkFav.getTag().equals("yellowStar"))
                {
                    btnMarkFav.setTag("hollowStar");
                    btnMarkFav.setBackgroundResource(R.mipmap.hollowstar_icon);
                    editor.remove(packageInfo.packageName+"");
                    editor.apply();
                }

            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),Details.class);
                intent.putExtra("PackageInfo",packageInfo);
                startActivity(intent);

            }
        });

    }

    public void populateSpinner()
    {
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
                adapter.add(value);
            }
        }

        spinnerCategory.setAdapter(adapter);

        Map<String, ?> allEntries2;
        allEntries2 = sharedPreferences2.getAll();
        for (Map.Entry<String, ?> entry : allEntries2.entrySet()) {
            Log.v("tag", entry.getKey() + ": " + entry.getValue().toString());
        }
        if(!allEntries2.isEmpty()) {
            Iterator myVeryOwnIterator2 = allEntries2.keySet().iterator();

            while (myVeryOwnIterator2.hasNext()) {
                String key2 = (String) myVeryOwnIterator2.next();
                String value2 = (String) allEntries2.get(key2);

                if(packageInfo.packageName.equalsIgnoreCase(key2))
                {
                    if (!value2.equals(null)) {
                        int spinnerPosition2 = adapter.getPosition(value2);
                        spinnerCategory.setSelection(spinnerPosition2);
                    }
                }
            }
        }
    }

    private void sendMail(File outFile) {
        Uri uriToZip = Uri.fromFile(outFile);
        String sendText = "";

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[] { "" });
        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, sendText);
        sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");

        sendIntent.setType("*/*");
        sendIntent.putExtra(android.content.Intent.EXTRA_STREAM, uriToZip);
        startActivity(Intent.createChooser(sendIntent, "Share via"));
    }
}
