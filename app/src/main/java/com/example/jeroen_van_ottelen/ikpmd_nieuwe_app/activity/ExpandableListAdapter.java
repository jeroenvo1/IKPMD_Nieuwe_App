package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.R;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.models.Subject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jeroen_van_ottelen on 03-02-16.
 * Deze class heb ik gevonde op http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter
{
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private List<Subject> _subjects;


    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData, List<Subject> subjects)
    {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this._subjects = subjects;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {

        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(_subjects.get(groupPosition).getGrade() >= 7)
            {
                convertView = infalInflater.inflate(R.layout.list_group_green, null);
                TextView lblListHeader = (TextView) convertView.findViewById(R.id.listHeaderGreen);
                lblListHeader.setTypeface(null, Typeface.BOLD);
                lblListHeader.setText(headerTitle);
            }
            else if(_subjects.get(groupPosition).getGrade() >= 5.5 && _subjects.get(groupPosition).getGrade() < 7)
            {
                convertView = infalInflater.inflate(R.layout.list_group_orange, null);
                TextView lblListHeader = (TextView) convertView.findViewById(R.id.listHeaderOrange);
                lblListHeader.setTypeface(null, Typeface.BOLD);
                lblListHeader.setText(headerTitle);
            }
            else if(_subjects.get(groupPosition).getGrade() >0 && _subjects.get(groupPosition).getGrade() < 5.5)
            {
                convertView = infalInflater.inflate(R.layout.list_group_red, null);
                TextView lblListHeader = (TextView) convertView.findViewById(R.id.listHeaderRed);
                lblListHeader.setTypeface(null, Typeface.BOLD);
                lblListHeader.setText(headerTitle);
            } else
            {
                convertView = infalInflater.inflate(R.layout.list_group, null);
                TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
                lblListHeader.setTypeface(null, Typeface.BOLD);
                lblListHeader.setText(headerTitle);
            }
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
