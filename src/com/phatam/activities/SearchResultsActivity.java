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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.phatam.R;
import com.phatam.adapters.SearchFragmentAdapter;
import com.phatam.config.GlobalData;
import com.viewpagerindicator.UnderlinePageIndicator;

public class SearchResultsActivity extends SherlockFragmentActivity  implements OnQueryTextListener, OnClickListener {
 
	public static final int PREFER_RESULT_VIDEO = 0;
	public static final int PREFER_RESULT_ARTIST = 1;
	
	SearchFragmentAdapter mAdapter;
	private ViewPager mPager;
    private MenuItem mSearchMenuItem;
    private SearchView mSearchView;
    private String mKeyWord = "";
    private int mPeferResult;
    private Button btnVideoResult;
    private Button btnArtistResult;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

     // This configuration tuning is custom. You can tune every option, you
      		// may tune some of them,
      		// or you can create default configuration by
      		// ImageLoaderConfiguration.createDefault(this);
      		// method.
      		ImageLoaderConfiguration config = new ImageLoaderConfiguration
      				.Builder(this)
      				.threadPriority(Thread.NORM_PRIORITY - 2)
      				.denyCacheImageMultipleSizesInMemory()
      				.discCacheFileNameGenerator(new Md5FileNameGenerator())
      				.tasksProcessingOrder(QueueProcessingType.LIFO)
      				.build();
      		// Initialize ImageLoader with configuration.
      		GlobalData.imageLoader = ImageLoader.getInstance();
      		GlobalData.imageLoader.init(config);
      		
        Intent intent = getIntent();
        mKeyWord = intent.getStringExtra("KEY_WORD");
        if (mKeyWord != null) {
        	mKeyWord = mKeyWord.trim();
        	StringBuilder sb = new StringBuilder(mKeyWord);
        	int i = 0;
        	while(i < sb.length() - 1) {
        		if (sb.charAt(i) == ' ' && sb.charAt(i + 1) == ' ') {
        			sb.deleteCharAt(i);
        		} else {
        			i++;
        		}
        	}
        	
        	mKeyWord = sb.toString();
        }
        
        mPeferResult = intent.getIntExtra("PREFER_RESULT", 0);
        
		// Change ActionBar background color
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(mKeyWord);
        
        mAdapter = new SearchFragmentAdapter(getSupportFragmentManager(), mKeyWord);

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        UnderlinePageIndicator indicator = (UnderlinePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.setFades(false);
        
        // Select prefer result
        mPager.setCurrentItem(mPeferResult);
        
        btnVideoResult = (Button) findViewById(R.id.btnVideoResult);
        btnVideoResult.setOnClickListener(this);
        
        btnArtistResult = (Button) findViewById(R.id.btnArtistResult);
        btnArtistResult.setOnClickListener(this);
    }
    

	@Override
    public boolean onCreateOptionsMenu(Menu menu) { 
        //Create the search view
        mSearchView = new SearchView(getSupportActionBar().getThemedContext());
        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.setOnQueryTextListener(this);
        
        mSearchMenuItem = menu.add(getString(R.string.action_search));
        mSearchMenuItem.setIcon(R.drawable.ic_action_search);
        mSearchMenuItem.setActionView(mSearchView);
        mSearchMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onQueryTextSubmit(String query) {
		mKeyWord = query;		
		getSupportActionBar().setTitle(mKeyWord);
		
		mAdapter.onKeyWordChange(mKeyWord);
		
        return false;
	}


	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
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


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnVideoResult:
			mPager.setCurrentItem(0);
			break;
		case R.id.btnArtistResult:
			mPager.setCurrentItem(1);
			break;
		}
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
