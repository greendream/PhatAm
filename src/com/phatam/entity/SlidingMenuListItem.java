package com.phatam.entity;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.phatam.R;
import com.phatam.adapter.TwoTextArrayAdapter.RowType;

public class SlidingMenuListItem implements SlidingMenuItem {
    private final String mStr;
    private final Integer  mIcon;
    private final Context mContext;

    public SlidingMenuListItem(String text1, Integer image1, Context context) {
        this.mStr = text1;
        this.mIcon = image1;
        this.mContext = context;
    }

    @Override
    public int getViewType() {
        return RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_drawer, null);
            // Do some initialization
        } else {
            view = convertView;
        }
        TextView text1 = (TextView) view.findViewById(R.id.navi_menu_text);
        ImageView image1 = (ImageView) view.findViewById(R.id.navi_menu_icon);
        text1.setText(mStr);
        
        image1.setImageResource(mIcon);
        
        View divider =view.findViewById(R.id.divider);
        if ( text1.equals("Liên hệ")) {
        	divider.setBackgroundResource(R.color.nav_background);
        }
        
        return view;
    }

}
