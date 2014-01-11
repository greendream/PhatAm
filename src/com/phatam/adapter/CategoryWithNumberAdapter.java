package com.phatam.adapter;

import java.util.ArrayList;
import com.phatam.R;
import com.phatam.model.CategoryWithNumberModel;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryWithNumberAdapter extends ArrayAdapter<CategoryWithNumberModel>{
	
	private final Activity context;
	private  ArrayList<CategoryWithNumberModel> list;
	public CategoryWithNumberAdapter(Activity context, ArrayList<CategoryWithNumberModel> list) {
		// TODO Auto-generated constructor stub
		super(context, R.layout.list_item_category_with_number, list);
		this.context = context;
		this.list = list;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		return super.getView(position, convertView, parent);
//		Log.v("in get view of category", "asdasda");
		View rowView=convertView;
		if(rowView == null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView=inflater.inflate(R.layout.list_item_category_with_number, null);
		}
//		rowView.setOnClickListener(new View.OnClickListener() {
//			
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				ViewHolder viewHold=(ViewHolder)v.getTag();
//				Model_Category element = (Model_Category)viewHold.check.getTag();
//				element.setSelected(!((Model_Category)viewHold.check.getTag()).getSelected());
//				viewHold.check.setChecked(element.getSelected());
//				
////				Toast.makeText(context, "You choose:"+((Model)viewHold.check.getTag()).getName()+";isChecked:"+((Model)viewHold.check.getTag()).getSelected(), Toast.LENGTH_SHORT).show();
//			}
//		});
		TextView text = (TextView) rowView.findViewById(R.id.category_list_text);
        text.setText(list.get(position).getName());
        
        TextView text1 = (TextView) rowView.findViewById(R.id.category_list_number);
        text1.setText(list.get(position).getNumber());
        if (position %2 ==0)
		{
			rowView.setBackgroundColor(context.getResources().getColor(R.color.list_row_green));
		}
		else
		{
			rowView.setBackgroundColor(context.getResources().getColor(R.color.white));
		}
		return rowView;
	}
	
}