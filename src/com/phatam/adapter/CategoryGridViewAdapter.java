package com.phatam.adapter;

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
import com.phatam.activity.PagerListVideoActivity;
import com.phatam.util.AppStatus;
import com.phatam.util.BitmapUtils;

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
		boolean isOnline = false;
		if (AppStatus.getInstance(mContext).isOnline(mContext)) {
			isOnline = true;
		}

		Intent i = new Intent(mContext, PagerListVideoActivity.class);
		i.putExtra("type", PagerListVideoActivity.TYPE_CATEGORY_VIDEOS);
		i.putExtra("category_order", Integer.parseInt(v.getTag().toString()));
		i.putExtra("isOnline", isOnline);
		mContext.startActivity(i);
	}
}
