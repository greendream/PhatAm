package com.phatam.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.phatam.R;
import com.phatam.model.AuthorModel;

public class AuthorAdapter extends BaseAdapter {
	
	private final Activity context;
	private  ArrayList<AuthorModel> mArrayListAuthorItem;
	
	public AuthorAdapter(Activity context, ArrayList<AuthorModel> listItem) {
		this.context = context;
		this.mArrayListAuthorItem = listItem;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView=convertView;
		if(rowView == null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView=inflater.inflate(R.layout.list_item_author, null);
		}
		
		TextView text = (TextView) rowView.findViewById(R.id.author_list_text);
        text.setText(mArrayListAuthorItem.get(position).getName());
		return rowView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayListAuthorItem.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}