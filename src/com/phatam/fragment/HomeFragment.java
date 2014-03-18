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


package com.phatam.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.phatam.R;
import com.phatam.activities.MainActivity;
import com.phatam.adapters.ListVideoAdapter;
import com.phatam.websevice.ApiUrl;

public class HomeFragment extends SherlockFragment {
	public static int NUM_PAGES = 3;
	public static int VIDEO_PAGE = 1;


	ListView mListViewNewVideo;
	ListVideoAdapter mListNewVideoAdapter;
	Button mButtons[] = new Button[3];
	Button mButtonMoreVideo;
	ViewPager mViewPager;

	private MainActivity mMainActivity;

	public void setMainActivity(MainActivity a) {
		mMainActivity = a;
		mMainActivity.getSupportActionBar().setTitle(R.string.app_name);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container, false);

		mButtons[0] = (Button) rootView.findViewById(R.id.home_fragment_btn_show_top_video);
		mButtons[1] = (Button) rootView.findViewById(R.id.home_fragment_btn_show_new_video);
		mButtons[2] = (Button) rootView.findViewById(R.id.home_fragment_btn_show_random_video);
		for (int i = 0; i < 3; i++) {
			mButtons[i].setOnClickListener(OnButtonClickListener);
			mButtons[i].setBackgroundColor(getResources().getColor(android.R.color.background_light));
			mButtons[i].setTag(i);
		}
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
			mButtons[1].setBackgroundResource(R.drawable.blue_gradient);
		else
			mButtons[1].setBackgroundResource(R.drawable.blue_gradient);

		/**
		 * Commit fragment into parent fragment
		 */
		LoadmoreListVideoFragment fragment = new LoadmoreListVideoFragment().setUrl(ApiUrl.getNewVideoUrl(ApiUrl.ORDER_BY_UPLOAD_DATE, -1));
		FragmentManager childmanager = getFragmentManager();
		childmanager.beginTransaction().replace(R.id.home_fragment_fragment, fragment).commit();

		return rootView;

	}

	// When click buttons to show type of video
	private View.OnClickListener OnButtonClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			for (int i = 0; i < 3; i++)
				mButtons[i].setBackgroundColor(getResources().getColor(
						android.R.color.background_light));
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
				v.setBackgroundResource(R.drawable.blue_gradient);
			else
				v.setBackgroundResource(R.drawable.blue_gradient);
			VIDEO_PAGE = Integer.parseInt(v.getTag().toString());
			Fragment fragment = null;
			switch (VIDEO_PAGE) {
			case 0:
				fragment = new LoadmoreListVideoFragment().setUrl(ApiUrl.getTopVideoUrl(ApiUrl.ORDER_BY_VIDEO_RATING, -1));
				break;
			case 1:
				fragment = new LoadmoreListVideoFragment().setUrl(ApiUrl.getNewVideoUrl(ApiUrl.ORDER_BY_UPLOAD_DATE, -1));
				break;
			case 2:
				fragment = new LoadmoreListVideoFragment().setUrl(ApiUrl.getRandomVideoUrl(ApiUrl.ORDER_BY_SITE_VIDEO_TITLE, -1));
				break;
			default:
				break;
			}
			FragmentManager childmanager = getFragmentManager();
			childmanager.beginTransaction()
					.replace(R.id.home_fragment_fragment, fragment).commit();
			Log.v("click button", v.getTag().toString());
		}
	};

}
