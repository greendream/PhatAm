package com.phatam.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragment;
import com.phatam.R;
import com.phatam.adapter.CategoryGridViewAdapter;

public class CategoryFragment extends SherlockFragment {

	GridView mGridViewCategory;
	ArrayList<Integer> mArrIconRes = new ArrayList<Integer>();
	CategoryGridViewAdapter mCategoryGridViewAdapter;
	int cellWidth;
	int cellHeight;
	int numColumns;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_category, container,
				false);

		int[] mArrIconRes = {
				R.drawable.icon_category1,
				R.drawable.icon_category2,
				R.drawable.icon_category3,
				R.drawable.icon_category4,
				R.drawable.icon_category5,
				R.drawable.icon_category6,
				R.drawable.icon_category7,
				R.drawable.icon_category8,
				R.drawable.icon_category9,
				R.drawable.icon_category10,
				R.drawable.icon_category11,
				R.drawable.icon_category12};

		computeCellSize();
		mGridViewCategory = (GridView) rootView.findViewById(R.id.gridViewCategory);
		mGridViewCategory.setNumColumns(numColumns);
		mCategoryGridViewAdapter = new CategoryGridViewAdapter(getSherlockActivity(), mArrIconRes, cellWidth, cellHeight);
		mGridViewCategory.setAdapter(mCategoryGridViewAdapter);

		return rootView;
	}
	
	void computeCellSize() {
		// Fetch screen height and width, to use as our max size when loading
		// images as this
		// activity runs full screen
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		(this.getActivity()).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		final int h = displayMetrics.heightPixels;
		final int w = displayMetrics.widthPixels;
		int SCREEN_WIDTH = (w < h) ? w : h;
		int SCREEN_HEIGHT = (h < w) ? w : h;
		
		cellWidth = (SCREEN_WIDTH - 40) / 3;
		cellHeight = cellWidth * 470 / 400;
		
		numColumns = SCREEN_WIDTH / cellWidth;
	}

}
