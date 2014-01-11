package com.phatam.entity;

import com.phatam.R;
import com.phatam.adapter.TwoTextArrayAdapter.RowType;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class HeaderItem implements SlidingMenuItem {
    private final String  name;

    public HeaderItem(String name) {
        this.name = name;
    }

    @Override
    public int getViewType() {
        return RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_drawer_header, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        TextView text = (TextView) view.findViewById(R.id.navi_menu_text2);
        text.setText(name);
        return view;
    }

}