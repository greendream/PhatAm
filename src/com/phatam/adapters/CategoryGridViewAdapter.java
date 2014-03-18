
/*
 * Copyright (C) 2014  Le Ngoc Anh <greendream.ait@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 */

package com.phatam.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.phatam.R;
import com.phatam.activities.PagerListVideoActivity;
import com.phatam.util.BitmapUtils;
import com.phatam.websevice.ApiUrl;

/**
 * 
 * @author manish.s
 * 
 */
public class CategoryGridViewAdapter extends BaseAdapter implements
		OnTouchListener, OnClickListener {

	Context mContext;
	int layoutResourceId;
	int[] mArrIconRes;
	int mImageWidth;
	int mImageHeight;

	public CategoryGridViewAdapter(Context context, int[] arrIconRes,
			int cellWidth, int cellHeight) {
		super();

		this.mContext = context;
		this.mArrIconRes = arrIconRes;
		this.mImageWidth = cellWidth;
		this.mImageHeight = cellHeight;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrIconRes.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView ivImageHolder = null;

		if (convertView != null) {
			return convertView;
		}

		ivImageHolder = new ImageView(mContext);
		ivImageHolder.setLayoutParams(new GridView.LayoutParams(mImageWidth,mImageHeight));
		ivImageHolder.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(mContext.getResources(),
						mArrIconRes[position], mImageWidth, mImageHeight, 0));
		ivImageHolder.setOnTouchListener(this);
		ivImageHolder.setOnClickListener(this);

		ivImageHolder.setTag(position);
		return ivImageHolder;

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Animation aniButtonPress = AnimationUtils.loadAnimation(mContext,
					R.animator.button_press);
			v.startAnimation(aniButtonPress);
		}

		return false;
	}

	@Override
	public void onClick(View v) {

		Intent i = new Intent(mContext, PagerListVideoActivity.class);
		i.putExtra(PagerListVideoActivity.STR_URL, ApiUrl.getVideoInCategoryUrl(mContext.getResources().getStringArray(R.array.arr_cartegory_id)[Integer.parseInt(v.getTag().toString())], "", 1));
		i.putExtra(PagerListVideoActivity.STR_ACTION_BAR_TITLE, mContext.getResources().getStringArray(R.array.arr_cartegory_name)[Integer.parseInt(v.getTag().toString())]);
		i.putExtra(PagerListVideoActivity.STR_DEFAULT_PAGE_INDEX, 2);
		
		mContext.startActivity(i);
	}
}
