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

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
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
import com.phatam.R;
import com.phatam.adapters.SlidingMenuAdapter;
import com.phatam.config.GlobalData;
import com.phatam.fragment.FArtist;
import com.phatam.fragment.FCategory;
import com.phatam.fragment.FDonate;
import com.phatam.fragment.FFavoriteVideosListView;
import com.phatam.fragment.FHistoryVideosListView;
import com.phatam.fragment.FHome;
import com.phatam.fragment.FIntroduce;
import com.phatam.fragment.FLoadmoreListVideo;
import com.phatam.fragment.FSetting;
import com.phatam.interfaces.OnConnectionStatusChangeListener;
import com.phatam.interfaces.OnSlidingMenuItemClickedListener;
import com.phatam.model.MSlidingMenuListItem;
import com.phatam.playback.PhatAmConnectionStatusReceiver;
import com.phatam.util.UtilConnection;
import com.phatam.websevice.ApiUrl;
import com.phatam.websevice.OnGetJsonListener;
import com.phatam.websevice.ServiceGetRandomVideo;

public class MainActivity extends SherlockFragmentActivity implements
		OnQueryTextListener, OnItemClickListener, OnConnectionStatusChangeListener {

	// Main menu on the left hand
	SlidingMenu mLeftSlidingMenu;
	ListView mListViewInLeftSlidingMenu;
	ArrayList<MSlidingMenuListItem> mSlidingMenuListItems;
	SlidingMenuAdapter mSlidingMenuAdapter;

	// Favorite on the right hand
	SlidingMenu mFavoriteVideo;

	FragmentManager mFragmentManager;
	FArtist mAuthorsFragment;
	FCategory mCategoryFragment;
	MenuItem mSearchMenuItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		PhatAmConnectionStatusReceiver.addConnectionObserver(this);
		super.onCreate(savedInstanceState);

		GlobalData.context = getApplicationContext();

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
		mLeftSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mLeftSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		mLeftSlidingMenu.setShadowDrawable(R.drawable.shadow);
		mLeftSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mLeftSlidingMenu.setFadeDegree(0.35f);
		mLeftSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		mLeftSlidingMenu.setMenu(menu_frame);

		/**
		 * Show the home fragment for start-up
		 *
		 */
		
		FHome homeFragment = new FHome();
		homeFragment.setMainActivity(this);
		mFragmentManager = getSupportFragmentManager();
		mFragmentManager.beginTransaction().replace(R.id.content_frame, homeFragment).commit();

		onConnectionStatusChange();
    	
	}

	
	
	public ArrayList<MSlidingMenuListItem> createSlidingMenuListItems() {
		mSlidingMenuListItems = new ArrayList<MSlidingMenuListItem>();

		// Create & Add Items "Top menu group"
		mSlidingMenuListItems.add(new MSlidingMenuListItem(
				MSlidingMenuListItem.ITEM_TYPE_TOP_MENU_GROUP,
				new OnSlidingMenuItemClickedListener() {
					// Action for Home button
					
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						FHome fragment = new FHome();
						
						// Insert the fragment by replacing any existing
						// fragment
						mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

						// Rename the action bar
						getSupportActionBar().setTitle(R.string.app_name);
						
						// close the sliding menu
						mLeftSlidingMenu.toggle();
					}
				},
				new OnSlidingMenuItemClickedListener() {
					// Action for Favorite button
					
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						FFavoriteVideosListView fragment = new FFavoriteVideosListView();
						
						// Insert the fragment by replacing any existing
						// fragment
						mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
		
						// Rename the action bar
						getSupportActionBar().setTitle(R.string.str_favorite);
						
						// close the sliding menu
						mLeftSlidingMenu.toggle();
					}
				},
				new OnSlidingMenuItemClickedListener() {
					// Action for History button
					
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						FHistoryVideosListView fragment = new FHistoryVideosListView();
						
						// Insert the fragment by replacing any existing
						// fragment
						mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
		
						// Rename the action bar
						getSupportActionBar().setTitle(R.string.str_history);
						
						// close the sliding menu
						mLeftSlidingMenu.toggle();
					}
				})
		);

		// Create & Add Items CATEGORY "PHẬT ÂM"
		mSlidingMenuListItems.add(new MSlidingMenuListItem(
				MSlidingMenuListItem.ITEM_TYPE_CATEGORY_NAME,
				0,
				this.getString(R.string.sliding_menu_category_phatam),
				"",
				null)
		);
		
		// Create & Add Items "Chuyên mục"
		mSlidingMenuListItems.add(new MSlidingMenuListItem(
				MSlidingMenuListItem.ITEM_TYPE_SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_category,
				this.getString(R.string.sliding_menu_lable_category),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						FCategory fragment = new FCategory();
						
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
		mSlidingMenuListItems.add(new MSlidingMenuListItem(
				MSlidingMenuListItem.ITEM_TYPE_SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_artist,
				this.getString(R.string.sliding_menu_lable_artist),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						FArtist fragment = new FArtist();
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

		// Create & Add Items "Video mới"
		mSlidingMenuListItems.add(new MSlidingMenuListItem(
				MSlidingMenuListItem.ITEM_TYPE_SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_new,
				this.getString(R.string.sliding_menu_lable_new_videos),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						FLoadmoreListVideo fragment = new FLoadmoreListVideo();
						fragment.setUrl(ApiUrl.getNewVideosUrl(-1));
						
						// Insert the fragment by replacing any existing
						// fragment
						mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

						// Rename the action bar
						getSupportActionBar().setTitle(R.string.sliding_menu_lable_new_videos);
						
						// close the sliding menu
						mLeftSlidingMenu.toggle();				
					}
				})
		);
		
		// Create & Add Items "Video nổi bật"
		mSlidingMenuListItems.add(new MSlidingMenuListItem(
				MSlidingMenuListItem.ITEM_TYPE_SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_top,
				this.getString(R.string.sliding_menu_lable_top_videos),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						FLoadmoreListVideo fragment = new FLoadmoreListVideo();
						fragment.setUrl(ApiUrl.getTopVideoUrl(-1));
						
						// Insert the fragment by replacing any existing
						// fragment
						mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

						// Rename the action bar
						getSupportActionBar().setTitle(R.string.sliding_menu_lable_top_videos);
						
						// close the sliding menu
						mLeftSlidingMenu.toggle();				
					}
				})
		);

		// Create & Add Items "Video Ngẫu nhiên"
		mSlidingMenuListItems.add(new MSlidingMenuListItem(
				MSlidingMenuListItem.ITEM_TYPE_SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_random,
				this.getString(R.string.sliding_menu_lable_random_videos),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// close the sliding menu
						mLeftSlidingMenu.toggle();
						
						ServiceGetRandomVideo service = new ServiceGetRandomVideo();
						service.addOnGetJsonListener(new OnGetJsonListener() {
							
							@Override
							public void onGetJsonFail(String response) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onGetJsonCompleted(String response) {
								try {
									JSONObject json = new JSONObject(response);
									JSONArray array = json.getJSONArray("videos");
									JSONObject videoJsonObject = array.getJSONObject(0);
									String uniq_id = videoJsonObject.getString("uniq_id");
									
									// Start activity
									Intent i = new Intent(MainActivity.this, FullVideoInfoActivity.class);
									i.putExtra(FullVideoInfoActivity.INFO_VIDEO_UNIQUE_ID, uniq_id);
									startActivity(i);
								} catch (Exception e) {
									
								}
							}
						});
						
						service.getRandomVideo();
					}
				})
		);
		

		// Create & Add Items CATEGORY "ỨNG DỤNG"
		mSlidingMenuListItems.add(new MSlidingMenuListItem(
				MSlidingMenuListItem.ITEM_TYPE_CATEGORY_NAME,
				0,
				this.getString(R.string.sliding_menu_category_aplication),
				"",
				null)
		);
		
		// Create & Add Items "Cài đặt"
		mSlidingMenuListItems.add(new MSlidingMenuListItem(
				MSlidingMenuListItem.ITEM_TYPE_SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_setting,
				this.getString(R.string.sliding_menu_lable_setting),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						FSetting fragment = new FSetting();
								
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
		mSlidingMenuListItems.add(new MSlidingMenuListItem(
				MSlidingMenuListItem.ITEM_TYPE_SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_introduce,
				this.getString(R.string.sliding_menu_lable_introduce),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						FIntroduce fragment = new FIntroduce();
								
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
		
		// Create & Add Items "Ủng hộ"
		mSlidingMenuListItems.add(new MSlidingMenuListItem(
				MSlidingMenuListItem.ITEM_TYPE_SCREEN_IN_CATEGORY,
				R.drawable.ic_menu_donate,
				this.getString(R.string.sliding_menu_lable_donate),
				"",
				new OnSlidingMenuItemClickedListener() {
					@Override
					public void doAction() {
						// Create a new fragment and specify the planet to show
						// based on position
						FDonate fragment = new FDonate();
								
						// Insert the fragment by replacing any existing
						// fragment
						mFragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();

						// Rename the action bar
						getSupportActionBar().setTitle(R.string.sliding_menu_lable_donate);
								
						// close the sliding menu
						mLeftSlidingMenu.toggle();
					}
				})
		);
		
		
		// Create & Add Items "Đóng ứng dụng"
		mSlidingMenuListItems.add(new MSlidingMenuListItem(
				MSlidingMenuListItem.ITEM_TYPE_SCREEN_IN_CATEGORY,
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
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
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
