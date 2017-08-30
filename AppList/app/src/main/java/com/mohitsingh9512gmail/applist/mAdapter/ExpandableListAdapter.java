package com.mohitsingh9512gmail.applist.mAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mohitsingh9512gmail.applist.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	PackageManager packageManager;
	PackageInfo packageInfo;
	List<PackageInfo> packageList;
	private Context _context;
	SharedPreferences sharedPreferences3;
	SharedPreferences sharedPreferences6;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> _listDataChild;
	public static final String MyPREFERENCES3 = "AppCategory" ;
	public static final String MyPREFERENCES6="AppNames";

	public ExpandableListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, final ViewGroup parent) {

		final String childText = (String) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.manage_category_list_item, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.lblListItem);

		Button btnDeleteChild=(Button) convertView
				.findViewById(R.id.btnDelCat);

		txtListChild.setText(childText);

		 btnDeleteChild.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				PackageManager packageManager=_context.getPackageManager();
				sharedPreferences6=_context.getSharedPreferences(MyPREFERENCES6,Context.MODE_PRIVATE);
				Map<String, ?> allEntries1;
				allEntries1 = sharedPreferences6.getAll();
				if (!allEntries1.isEmpty()) {
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

						String appName = packageManager.getApplicationLabel(
								packageInfo.applicationInfo).toString();

						if(appName.equalsIgnoreCase(childText))
						{
							sharedPreferences3 = _context.getSharedPreferences(MyPREFERENCES3
									,Context.MODE_PRIVATE);

							Map<String, ?> allEntries2;
							allEntries2 = sharedPreferences3.getAll();
							for (Map.Entry<String, ?> entry : allEntries2.entrySet()) {
								Log.v("tag", entry.getKey() + ": " + entry.getValue().toString());
							}
							sharedPreferences3.edit().remove(value1).apply();
						}
					}
				}
			}
		});

		return convertView;

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.manage_category_list_group, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
