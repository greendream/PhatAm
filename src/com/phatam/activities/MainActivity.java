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

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.phatam.R;
import com.phatam.adapters.SlidingMenuAdapter;
import com.phatam.config.GlobalData;
import com.phatam.entities.SlidingMenuListItem;
import com.phatam.fragment.ArtistFragment;
import com.phatam.fragment.CategoryFragment;
import com.phatam.fragment.ContactFragment;
import com.phatam.fragment.HomeFragment;
import com.phatam.fragment.IntroduceFragment;
import com.phatam.fragment.SettingFragment;
import com.phatam.interfaces.OnSlidingMenuItemClickedListener;
import com.phatam.websevice.ApiUrl;

public class MainActivity extends SherlockFragmentActivity implements
		OnQueryTextListener, OnItemClickListener {

	// Main menu on the left hand
	SlidingMenu mLeftSlidingMenu;
	ListView mListViewInLeftSlidingMenu;
	ArrayList<SlidingMenuListItem> mSlidingMenuListItems;
	SlidingMenuAdapter mSlidingMenuAdapter;

	// Favorite on the right hand
	// SlidingMenu mFavoriteVideo;

	FragmentManager mFragmentManager;
	ArtistFragment mAuthorsFragment;
	CategoryFragment mCategoryFragment;
	MenuItem mSearchMenuItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * Initial image loader
		 */
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		// Initialize ImageLoader with configuration.
		GlobalData.imageLoader = ImageLoader.getInstance();
		GlobalData.imageLoader.init(config);

		/**
		 * Initial the activity
		 */
		setContentView(R.layout.activity_main);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(R.color.blue)));

		/**
		 * Setting Sliding Menu
		 */
		LayoutInflater inflater = getLayoutInflater();
		View menu_frame = inflater.inflate(R.layout.layout_sliding_menu, null);
		// Initial list view for menu_frame
		mSlidingMenuAdapter = new SlidingMenuAdapter(this,createSlidingMenuListItems());
		mListViewInLeftSlidingMenu = (ListView) menu_frame.findViewById(R.id.list_view_in_sliding_menu);
		mListViewInLeftSlidingMenu.setAdapter(mSlidingMenuAdapter);
		mListViewInLeftSlidingMenu.setDivider(null);
		mListViewInLeftSlidingMenu.setOnItemClickListener(this);

		mLeftSlidingMenu = new SlidingMenu(this);
		mLeftSlidingMenu.setMode(SlidingMenu.LEFT);
		mLeftSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mLeftSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		mLeftSlidingMenu.setShadowDrawable(R.drawable.shadow);
		mLeftSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mLeftSlidingMenu.setFadeDegree(0.35f);
		mLeftSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		mLeftSlidingMenu.setMenu(menu_frame);

		/**
		 * Show the home fragment for start-up
		 */
		mFragmentManager = getSupportFragmentManager();
		HomeFragment homeFragment = new HomeFragment();
		homeFragment.setMainActivity(this);
		mFragmentManager.beginTransaction().replace(R.id.content_frame, homeFragment).commit();

	}

	public ArrayList<SlidingMenuListItem> createSlidingMenuListItems() {
		mSlidingMenuListItems = new ArrayList<SlidingMenuListItem>();

		// Create & Add Items "Trang chủ"
		mSlidingMenuListItems.add(new SlidingMenuListItem(
				SlidingMenuListItem.HOME_ITEM,
				R.drawable.ic_menu_home,
				this.getString(R.string.sliding_menu_lable_home),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						HomeFragment fragment = new HomeFragment();
						
						// Insert the fragment by replacing any existing
						// fragment
						mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

						// Rename the action bar
						getSupportActionBar().setTitle(R.string.app_name);
						
						// close the sliding menu
						mLeftSlidingMenu.toggle();
					}
				})
		);

		// Create & Add Items CATEGORY "PHẬT ÂM"
		mSlidingMenuListItems.add(new SlidingMenuListItem(
				SlidingMenuListItem.CATEGORY_NAME,
				0,
				this.getString(R.string.sliding_menu_category_phatam),
				"",
				null)
		);
		
		// Create & Add Items "Chuyên mục"
		mSlidingMenuListItems.add(new SlidingMenuListItem(
				SlidingMenuListItem.SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_category,
				this.getString(R.string.sliding_menu_lable_category),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						CategoryFragment fragment = new CategoryFragment();
						
						// Insert the fragment by replacing any existing
						// fragment
						mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

						// Rename the action bar
						getSupportActionBar().setTitle(R.string.sliding_menu_lable_category);
						
						// close the sliding menu
						mLeftSlidingMenu.toggle();
					}
				})
		);

		// Create & Add Items "Tác giả"
		mSlidingMenuListItems.add(new SlidingMenuListItem(
				SlidingMenuListItem.SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_artist,
				this.getString(R.string.sliding_menu_lable_artist),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						ArtistFragment fragment = new ArtistFragment();
						fragment.setUrl(ApiUrl.getAllArtistNameUrl(ApiUrl.ORDER_BY_ARTIST_VIDEO_COUNT, -1));
						
						// Insert the fragment by replacing any existing
						// fragment
						mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

						// Rename the action bar
						getSupportActionBar().setTitle(R.string.sliding_menu_lable_artist);
						
						// close the sliding menu
						mLeftSlidingMenu.toggle();
					}
				})
		);

		// Create & Add Items "Video nổi bật"
		mSlidingMenuListItems.add(new SlidingMenuListItem(
				SlidingMenuListItem.SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_top,
				this.getString(R.string.sliding_menu_lable_top_videos),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// close the sliding menu
						mLeftSlidingMenu.toggle();
						
						Intent i = new Intent(getApplicationContext(), PagerListVideoActivity.class);
						i.putExtra(PagerListVideoActivity.STR_URL, ApiUrl.getTopVideoUrl(ApiUrl.ORDER_BY_VIDEO_RATING, -1));
						i.putExtra(PagerListVideoActivity.STR_ACTION_BAR_TITLE, getResources().getString(R.string.sliding_menu_lable_top_videos));
						i.putExtra(PagerListVideoActivity.STR_DEFAULT_PAGE_INDEX, 0);
						startActivity(i);						
					}
				})
		);

		// Create & Add Items "Video mới"
		mSlidingMenuListItems.add(new SlidingMenuListItem(
				SlidingMenuListItem.SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_new,
				this.getString(R.string.sliding_menu_lable_new_videos),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// close the sliding menu
						mLeftSlidingMenu.toggle();
						
						Intent i = new Intent(getApplicationContext(), PagerListVideoActivity.class);
						i.putExtra(PagerListVideoActivity.STR_URL, ApiUrl.getNewVideoUrl(ApiUrl.ORDER_BY_VIDEO_RATING, -1));
						i.putExtra(PagerListVideoActivity.STR_ACTION_BAR_TITLE, getResources().getString(R.string.sliding_menu_lable_new_videos));
						i.putExtra(PagerListVideoActivity.STR_DEFAULT_PAGE_INDEX, 2);
						startActivity(i);					
					}
				})
		);

		// Create & Add Items "Video Ngẫu nhiên"
		mSlidingMenuListItems.add(new SlidingMenuListItem(
				SlidingMenuListItem.SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_random,
				this.getString(R.string.sliding_menu_lable_random_videos),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// close the sliding menu
						mLeftSlidingMenu.toggle();
						
						Intent i = new Intent(getApplicationContext(), PagerListVideoActivity.class);
						i.putExtra(PagerListVideoActivity.STR_URL, ApiUrl.getRandomVideoUrl(ApiUrl.ORDER_BY_VIDEO_RATING, -1));
						i.putExtra(PagerListVideoActivity.STR_ACTION_BAR_TITLE,getResources().getString(R.string.sliding_menu_lable_random_videos));
						i.putExtra(PagerListVideoActivity.STR_DEFAULT_PAGE_INDEX, 3);
						startActivity(i);
					}
				})
		);
		

		// Create & Add Items CATEGORY "ỨNG DỤNG"
		mSlidingMenuListItems.add(new SlidingMenuListItem(
				SlidingMenuListItem.CATEGORY_NAME,
				0,
				this.getString(R.string.sliding_menu_category_aplication),
				"",
				null)
		);
		
		// Create & Add Items "Cài đặt"
		mSlidingMenuListItems.add(new SlidingMenuListItem(
				SlidingMenuListItem.SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_setting,
				this.getString(R.string.sliding_menu_lable_setting),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						SettingFragment fragment = new SettingFragment();
								
						// Insert the fragment by replacing any existing
						// fragment
						mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

						// Rename the action bar
						getSupportActionBar().setTitle(R.string.sliding_menu_lable_setting);
								
						// close the sliding menu
						mLeftSlidingMenu.toggle();
					}
				})
		);

		// Create & Add Items "Giới thiệu"
		mSlidingMenuListItems.add(new SlidingMenuListItem(
				SlidingMenuListItem.SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_introduce,
				this.getString(R.string.sliding_menu_lable_introduce),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						IntroduceFragment fragment = new IntroduceFragment();
								
						// Insert the fragment by replacing any existing
						// fragment
						mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

						// Rename the action bar
						getSupportActionBar().setTitle(R.string.sliding_menu_lable_introduce);
								
						// close the sliding menu
						mLeftSlidingMenu.toggle();
					}
				})
		);
		
		// Create & Add Items "Liên hệ"
		mSlidingMenuListItems.add(new SlidingMenuListItem(
				SlidingMenuListItem.SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_contact,
				this.getString(R.string.sliding_menu_lable_contact),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						ContactFragment fragment = new ContactFragment();
								
						// Insert the fragment by replacing any existing
						// fragment
						mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

						// Rename the action bar
						getSupportActionBar().setTitle(R.string.sliding_menu_lable_contact);
								
						// close the sliding menu
						mLeftSlidingMenu.toggle();
					}
				})
		);
		
		
		// Create & Add Items "Đóng ứng dụng"
		mSlidingMenuListItems.add(new SlidingMenuListItem(
				SlidingMenuListItem.SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_shoutdown,
				this.getString(R.string.sliding_menu_lable_shoutdown),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						MainActivity.this.finish();
					}
				})
		);
		
		return mSlidingMenuListItems;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		try {
			mSlidingMenuListItems.get(position).getOnItemClickHandler()
					.doAction();
		} catch (NullPointerException e) {

		}
	}

	@Override
	public void onBackPressed() {
		if (mLeftSlidingMenu.isMenuShowing()) {
			mLeftSlidingMenu.toggle();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			mLeftSlidingMenu.toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Create the search view
		SearchView searchView = new SearchView(getSupportActionBar()
				.getThemedContext());
		searchView.setQueryHint(getString(R.string.search_hint));
		searchView.setOnQueryTextListener(this);

		menu.add(getString(R.string.action_search))
				.setIcon(R.drawable.ic_action_search)
				.setActionView(searchView)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

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
		if (getSupportActionBar().getTitle().equals(
				getString(R.string.sliding_menu_lable_artist))) {
			iKeyWord.putExtra("PREFER_RESULT",
					SearchResultsActivity.PREFER_RESULT_ARTIST);
		} else if (getSupportActionBar().getTitle().equals(
				getString(R.string.sliding_menu_lable_category))) {
			iKeyWord.putExtra("PREFER_RESULT",
					SearchResultsActivity.PREFER_RESULT_VIDEO);
		}

		startActivity(iKeyWord);

		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
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
}
