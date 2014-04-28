package com.phatam.customviews;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.phatam.BuildConfig;
import com.phatam.R;
import com.phatam.activities.FullVideoInfoActivity;
import com.phatam.activities.PagerListVideoActivity;
import com.phatam.config.GlobalData;
import com.phatam.model.MCategoryReviewItem;
import com.phatam.websevice.ApiUrl;

public class ViewSixVideosCategoryPreview extends LinearLayout implements OnClickListener {

	private Context mContext;
	public static String CLASS_PATH = ViewSixVideosCategoryPreview.class.toString();

	private MCategoryReviewItem mCategoryReviewItem;
	private TextView tvCategoryName;

	private ImageView ivPreview1;
	private TextView tvVideoTitle1;
	private TextView tvVideoArtist1;

	private ImageView ivPreview2;
	private TextView tvVideoTitle2;
	private TextView tvVideoArtist2;

	private ImageView ivPreview3;
	private TextView tvVideoTitle3;
	private TextView tvVideoArtist3;

	private ImageView ivPreview4;
	private TextView tvVideoTitle4;
	private TextView tvVideoArtist4;

	private ImageView ivPreview5;
	private TextView tvVideoTitle5;
	private TextView tvVideoArtist5;

	private ImageView ivPreview6;
	private TextView tvVideoTitle6;
	private TextView tvVideoArtist6;
	

	public ViewSixVideosCategoryPreview(Context context) {
		super(context);
	}

	public ViewSixVideosCategoryPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void init(Context context) {
		mContext = context;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_six_videos_preview, this);

		// Load sub components
		tvCategoryName = (TextView) findViewById(R.id.tvCategoryName);
		tvCategoryName.setText(mCategoryReviewItem.getCategoryName());
		tvCategoryName.setOnClickListener(this);

		ivPreview1 = (ImageView) findViewById(R.id.ivPreview1);
		ivPreview1.setOnClickListener(this);
		tvVideoTitle1 = (TextView) findViewById(R.id.tvVideoTitle1);
		tvVideoArtist1 = (TextView) findViewById(R.id.tvVideoArtist1);

		ivPreview2 = (ImageView) findViewById(R.id.ivPreview2);
		ivPreview2.setOnClickListener(this);
		tvVideoTitle2 = (TextView) findViewById(R.id.tvVideoTitle2);
		tvVideoArtist2 = (TextView) findViewById(R.id.tvVideoArtist2);

		ivPreview3 = (ImageView) findViewById(R.id.ivPreview3);
		ivPreview3.setOnClickListener(this);
		tvVideoTitle3 = (TextView) findViewById(R.id.tvVideoTitle3);
		tvVideoArtist3 = (TextView) findViewById(R.id.tvVideoArtist3);

		ivPreview4 = (ImageView) findViewById(R.id.ivPreview4);
		ivPreview4.setOnClickListener(this);
		tvVideoTitle4 = (TextView) findViewById(R.id.tvVideoTitle4);
		tvVideoArtist4 = (TextView) findViewById(R.id.tvVideoArtist4);

		ivPreview5 = (ImageView) findViewById(R.id.ivPreview5);
		ivPreview5.setOnClickListener(this);
		tvVideoTitle5 = (TextView) findViewById(R.id.tvVideoTitle5);
		tvVideoArtist5 = (TextView) findViewById(R.id.tvVideoArtist5);
		
		ivPreview6 = (ImageView) findViewById(R.id.ivPreview6);
		ivPreview6.setOnClickListener(this);
		tvVideoTitle6 = (TextView) findViewById(R.id.tvVideoTitle6);
		tvVideoArtist6 = (TextView) findViewById(R.id.tvVideoArtist6);
	
		showVideoPreviewInfo();
	}


	public void setCategoryReviewItem(MCategoryReviewItem pCategoryReviewItem) {
		mCategoryReviewItem = pCategoryReviewItem;
	}
	/**
	 * Show video preview info
	 * 
	 * @param pVideoList
	 * @param p6RandomNewVideoList
	 */
	private void showVideoPreviewInfo() {
		
		try {
			GlobalData.imageLoader.displayImage(mCategoryReviewItem.getmLoadedVideoList().get(0).getYoutubeThumb(), ivPreview1, GlobalData.options, GlobalData.animateFirstListener);
			tvVideoTitle1.setText(mCategoryReviewItem.getmLoadedVideoList().get(0).getVideoTitle());
			tvVideoArtist1.setText(mCategoryReviewItem.getmLoadedVideoList().get(0).getArtistName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			GlobalData.imageLoader.displayImage(mCategoryReviewItem.getmLoadedVideoList().get(1).getYoutubeThumb(),ivPreview2, GlobalData.options, GlobalData.animateFirstListener);
			tvVideoTitle2.setText(mCategoryReviewItem.getmLoadedVideoList().get(1).getVideoTitle());
			tvVideoArtist2.setText(mCategoryReviewItem.getmLoadedVideoList().get(1).getArtistName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			GlobalData.imageLoader.displayImage(mCategoryReviewItem.getmLoadedVideoList().get(2).getYoutubeThumb(),ivPreview3, GlobalData.options, GlobalData.animateFirstListener);
			tvVideoTitle3.setText(mCategoryReviewItem.getmLoadedVideoList().get(2).getVideoTitle());
			tvVideoArtist3.setText(mCategoryReviewItem.getmLoadedVideoList().get(2).getArtistName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			GlobalData.imageLoader.displayImage(mCategoryReviewItem.getmLoadedVideoList().get(3).getYoutubeThumb(), ivPreview4, GlobalData.options, GlobalData.animateFirstListener);
			tvVideoTitle4.setText(mCategoryReviewItem.getmLoadedVideoList().get(3).getVideoTitle());
			tvVideoArtist4.setText(mCategoryReviewItem.getmLoadedVideoList().get(3).getArtistName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			GlobalData.imageLoader.displayImage(mCategoryReviewItem.getmLoadedVideoList().get(4).getYoutubeThumb(), ivPreview5, GlobalData.options, GlobalData.animateFirstListener);
			tvVideoTitle5.setText(mCategoryReviewItem.getmLoadedVideoList().get(4).getVideoTitle());
			tvVideoArtist5.setText(mCategoryReviewItem.getmLoadedVideoList().get(4).getArtistName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			GlobalData.imageLoader.displayImage(mCategoryReviewItem.getmLoadedVideoList().get(5).getYoutubeThumb(), ivPreview6, GlobalData.options, GlobalData.animateFirstListener);
			tvVideoTitle6.setText(mCategoryReviewItem.getmLoadedVideoList().get(5).getVideoTitle());
			tvVideoArtist6.setText(mCategoryReviewItem.getmLoadedVideoList().get(5).getArtistName());			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		try {
			v.startAnimation(AnimationUtils.loadAnimation(mContext, R.animator.fade_zoom_in));
			Intent itentFullVideoInfoActivity = new Intent(mContext, FullVideoInfoActivity.class);

			switch (v.getId()) {
			case R.id.tvCategoryName:
				Intent itentPagerListVideoActivity = new Intent(mContext, PagerListVideoActivity.class);
				itentPagerListVideoActivity.putExtra(PagerListVideoActivity.STR_URL, ApiUrl.getVideoInCategoryUrl(mCategoryReviewItem.getCategoryId(), "", 1));
				itentPagerListVideoActivity.putExtra(PagerListVideoActivity.STR_ACTION_BAR_TITLE, mCategoryReviewItem.getCategoryName());
				itentPagerListVideoActivity.putExtra(PagerListVideoActivity.STR_DEFAULT_PAGE_INDEX, 0);
				mContext.startActivity(itentPagerListVideoActivity);
				break;
			case R.id.ivPreview1:
				itentFullVideoInfoActivity.putExtra(FullVideoInfoActivity.INFO_VIDEO_UNIQUE_ID, mCategoryReviewItem.getmLoadedVideoList().get(0).getUniqueId());
				mContext.startActivity(itentFullVideoInfoActivity);
				break;
			case R.id.ivPreview2:
				itentFullVideoInfoActivity.putExtra(FullVideoInfoActivity.INFO_VIDEO_UNIQUE_ID, mCategoryReviewItem.getmLoadedVideoList().get(1).getUniqueId());
				mContext.startActivity(itentFullVideoInfoActivity);
				break;
			case R.id.ivPreview3:
				itentFullVideoInfoActivity.putExtra(FullVideoInfoActivity.INFO_VIDEO_UNIQUE_ID, mCategoryReviewItem.getmLoadedVideoList().get(2).getUniqueId());
				mContext.startActivity(itentFullVideoInfoActivity);
				break;
			case R.id.ivPreview4:
				itentFullVideoInfoActivity.putExtra(FullVideoInfoActivity.INFO_VIDEO_UNIQUE_ID, mCategoryReviewItem.getmLoadedVideoList().get(3).getUniqueId());
				mContext.startActivity(itentFullVideoInfoActivity);
				break;
			case R.id.ivPreview5:
				itentFullVideoInfoActivity.putExtra(FullVideoInfoActivity.INFO_VIDEO_UNIQUE_ID, mCategoryReviewItem.getmLoadedVideoList().get(4).getUniqueId());
				mContext.startActivity(itentFullVideoInfoActivity);
				break;
			case R.id.ivPreview6:
				itentFullVideoInfoActivity.putExtra(FullVideoInfoActivity.INFO_VIDEO_UNIQUE_ID, mCategoryReviewItem.getmLoadedVideoList().get(5).getUniqueId());
				mContext.startActivity(itentFullVideoInfoActivity);
				break;
			}

		} catch (Exception e) {
			if (BuildConfig.DEBUG) e.printStackTrace();
		}
	}
}
