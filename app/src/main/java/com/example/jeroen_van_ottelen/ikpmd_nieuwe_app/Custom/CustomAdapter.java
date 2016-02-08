package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.Custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.R;
import com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.models.Subject;

import java.util.List;

/**
 * Created by jeroen_van_ottelen on 08-02-16.
 */
public class CustomAdapter extends ArrayAdapter<Subject>
{

    public CustomAdapter(Context context, int resource, List<Subject> objects)
    {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listview_items, null);
        }

        Subject subject = getItem(position);

        if (subject != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.id);
            TextView tt2 = (TextView) v.findViewById(R.id.categoryId);

            if (tt1 != null) {
                tt1.setText(subject.getName());
            }

            if (tt2 != null) {
                tt2.setText("Cijfer: " + subject.getGrade());
            }
        }

        return v;
    }
}
