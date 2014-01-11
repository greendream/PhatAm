package com.phatam.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.phatam.R;
import com.phatam.adapter.ListVideoAdapter;
import com.phatam.entity.VideoItem;
import com.phatam.fragment.PullAndLoadmoreListVideoFragment;

/**
 * Show video list sorted order by Views, Date, Title
 * Using FragmentPagerAdapter in ViewPager to create 3 page associated
 * 
 * @author anhle
 *
 */
public class PagerListVideoActivity extends SherlockFragmentActivity {

	public static final String TYPE_NEW_VIDEOS = "latestvideos";
	
	public static final String TYPE_AUTHOR_VIDEOS = "artist";

	public static final String TYPE_CATEGORY_VIDEOS = "category";

	public static final String TYPE_RANDOM_VIDEOS = "randomvideos";

	public static final String TYPE_TOP_VIDEOS = "topvideos";

	ViewPager mViewPager;
	public ArrayList<VideoItem> list_model = new ArrayList<VideoItem>();
	ListView mListView;
	ListVideoAdapter mListVideoAdapter;

	String mType; // top,new,random video, category
	int mCategoryOrder; // use when type is "category"
	String mAuthorName; // use when type is artist
	int mCurrentPage;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_viewpager_titlestrip);

		Intent intent = getIntent();
		mType = intent.getStringExtra("type");
		if (mType.equals(TYPE_CATEGORY_VIDEOS)) {
			mCategoryOrder = intent.getIntExtra("category_order", 0);
		}
		
		if (mType.equals(TYPE_AUTHOR_VIDEOS)) {
			mAuthorName = intent.getStringExtra("author_name");
			mAuthorName = mAuthorName.replace(" ", "%20");	
		}
		
		mViewPager = (ViewPager) findViewById(R.id.pager);

		final ListVideoFragmentPagerAdapter adapter = new ListVideoFragmentPagerAdapter(
				getSupportFragmentManager(), this);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int position) {
				mViewPager.setCurrentItem(position, true);
				adapter.getItem(mViewPager.getCurrentItem()).getView();

			}

		});
		
		// Show default video order by type and Activity Title
		if (mType.equals(TYPE_TOP_VIDEOS)) {
			getSupportActionBar().setTitle(R.string.top_videos);
			mViewPager.setCurrentItem(0, true);
		}
		if (mType.equals(TYPE_AUTHOR_VIDEOS)) {
			getSupportActionBar().setTitle(R.string.new_videos);
			mViewPager.setCurrentItem(1, true);
		}
		if (mType.equals(TYPE_NEW_VIDEOS)) {
			getSupportActionBar().setTitle(R.string.new_videos);
			mViewPager.setCurrentItem(1, true);
		}
		if (mType.equals(TYPE_RANDOM_VIDEOS)) {
			getSupportActionBar().setTitle(R.string.random_videos);
			mViewPager.setCurrentItem(2, true);
		}
		if (mType.equals(TYPE_CATEGORY_VIDEOS)) {
			getSupportActionBar().setTitle(getResources().getStringArray(R.array.arr_cartegory_name)[mCategoryOrder]);
			mViewPager.setCurrentItem(1, true);
		}
		
		// Show back icon in the left of Home button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	}

	public String getUrl() {
		if (mType.equals(TYPE_CATEGORY_VIDEOS)) {
			return mType + "/" + getResources().getStringArray(R.array.arr_cartegory_id)[mCategoryOrder];
		}
		
		if (mType.equals(TYPE_AUTHOR_VIDEOS)){
			return mType + "/" + mAuthorName;
		}
		return this.mType;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public class ListVideoFragmentPagerAdapter extends FragmentPagerAdapter {
		
		private static final int NUM_PAGES = 3;
		PagerListVideoActivity mActivity;

		public ListVideoFragmentPagerAdapter(FragmentManager fm,
				PagerListVideoActivity ac) {
			super(fm);
			mActivity = ac;
		}

		@Override
		public int getItemPosition(Object object) {
			return mViewPager.getCurrentItem();
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fr;
            if (position == 0) {
                    fr = new PullAndLoadmoreListVideoFragment(mActivity.getUrl(), PullAndLoadmoreListVideoFragment.ORDER_BY_RATING);
            } else if (position == 1) {
                    fr = new PullAndLoadmoreListVideoFragment(mActivity.getUrl(), PullAndLoadmoreListVideoFragment.ORDER_BY_ADDED);
            } else {
                    fr = new PullAndLoadmoreListVideoFragment(mActivity.getUrl(), PullAndLoadmoreListVideoFragment.ORDER_BY_VIDEO_TITLE);
            }
            
            return fr;
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0) {
				return getString(R.string.type_video_rating);
			} else if (position == 1) {
				return getString(R.string.type_video_date);
			} else if (position == 2) {
				return getString(R.string.type_video_title);
			}
			return ("");
		}

	}

}
