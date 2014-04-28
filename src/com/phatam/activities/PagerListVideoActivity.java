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
package com.phatam.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.phatam.R;
import com.phatam.fragment.FLoadmoreListVideo;
import com.phatam.interfaces.OnConnectionStatusChangeListener;
import com.phatam.playback.PhatAmConnectionStatusReceiver;
import com.phatam.util.UtilConnection;
import com.phatam.websevice.ApiUrl;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

/**
 * Show video list sorted order by Views, Date, Title
 * Using FragmentPagerAdapter in ViewPager to create 3 page associated
 * 
 * @author anhle
 *
 */
public class PagerListVideoActivity extends SherlockFragmentActivity implements OnQueryTextListener, OnConnectionStatusChangeListener {

	private MenuItem mSearchMenuItem;

	public static final String STR_URL = "URL";
	public static final String STR_ACTION_BAR_TITLE = "ACTION_BAR_TITLE";
	public static final String STR_DEFAULT_PAGE_INDEX = "DEFAULT_PAGE_INDEX";
	
	private static String[] PAGE_TITLES = {"Ngày đăng", "Lượt xem", "Đánh giá", "Tiêu đề"};
	private static String[] ORDER_BY_TYPES = {ApiUrl.ORDER_BY_UPLOAD_DATE, ApiUrl.ORDER_BY_SITE_VIEW, ApiUrl.ORDER_BY_VIDEO_RATING, ApiUrl.ORDER_BY_VIDEO_TITLE};
	
	private ViewPager mPager;
	private ListVideoFragmentPagerAdapter adapter;
	private PageIndicator mIndicator;
	
	public String mUrl = "";
	public String mActionBarTitle = "";
	public int  mDefaulPageIndex = 0;
	

	@Override
	protected void onCreate(Bundle arg0) {
		PhatAmConnectionStatusReceiver.addConnectionObserver(this);
		
		super.onCreate(arg0);
		setContentView(R.layout.activity_video_view_pager);
		
		// Extract intent info
		Intent i = getIntent();
		mUrl = i.getStringExtra(STR_URL);
		mActionBarTitle = i.getStringExtra(STR_ACTION_BAR_TITLE);
		mDefaulPageIndex = i.getIntExtra(STR_DEFAULT_PAGE_INDEX, 0);

		// Change ActionBar Title and Background color
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
		getSupportActionBar().setTitle(mActionBarTitle);
		
		mPager = (ViewPager) findViewById(R.id.pager);
		adapter = new ListVideoFragmentPagerAdapter(getSupportFragmentManager(), this);
		mPager.setAdapter(adapter);

		mIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
		
		// Show default page index
		mPager.setCurrentItem(mDefaulPageIndex, true);
		
		// Show back icon in the left of Home button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		onConnectionStatusChange();
    	
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
		
		PagerListVideoActivity mActivity;

		public ListVideoFragmentPagerAdapter(FragmentManager fm, PagerListVideoActivity ac) {
			super(fm);
			mActivity = ac;
		}

		@Override
		public int getItemPosition(Object object) {
			return mPager.getCurrentItem();
		}

		@Override
		public Fragment getItem(int position) {
			/**********************************************************
			 * Will be remove when API complete
			 */
			if (getString(R.string.sliding_menu_lable_top_videos).equals(mActionBarTitle) ||
				getString(R.string.sliding_menu_lable_new_videos).equals(mActionBarTitle) ||
				getString(R.string.sliding_menu_lable_random_videos).equals(mActionBarTitle)) {
				return (new FLoadmoreListVideo().setUrl(mUrl));	
			}
			/**********************************************************/
			
            return (new FLoadmoreListVideo().setUrl(mUrl + ORDER_BY_TYPES[position] + "/"));
		}

		@Override
		public int getCount() {
			return PAGE_TITLES.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return (PAGE_TITLES[position % PAGE_TITLES.length]);
		}

	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) { 
        //Create the search view
        SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);

        menu.add("SearchAll")
            .setIcon(R.drawable.ic_action_search)
            .setActionView(searchView)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        mSearchMenuItem = menu.getItem(0);
        
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onQueryTextSubmit(String query) {
		if (mSearchMenuItem != null) {
        	mSearchMenuItem.collapseActionView();
        }
		
		Intent iKeyWord = new Intent(this, SearchResultsActivity.class);
		iKeyWord.putExtra("KEY_WORD", query);
		iKeyWord.putExtra("PREFER_RESULT", SearchResultsActivity.PREFER_RESULT_VIDEO);
		
		startActivity(iKeyWord);
        
        return false;
	}


	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// The rest of your onStart() code.
		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}
	

	@Override
	public void onConnectionStatusChange() {
		// TODO Auto-generated method stub
		if (UtilConnection.getConnectivityStatus(this) == UtilConnection.TYPE_NOT_CONNECTED) {
			this.findViewById(R.id.layoutConnectionError).setVisibility(View.VISIBLE);
			this.findViewById(R.id.layoutConnectionError).startAnimation(AnimationUtils.loadAnimation(this, R.animator.appear));
		} else {
			this.findViewById(R.id.layoutConnectionError).setVisibility(View.GONE);
			this.findViewById(R.id.layoutConnectionError).startAnimation(AnimationUtils.loadAnimation(this, R.animator.disappear));
		}
		
	}
}
