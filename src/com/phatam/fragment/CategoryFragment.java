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

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;
import com.phatam.R;
import com.phatam.activities.PagerListVideoActivity;
import com.phatam.websevice.ApiUrl;

public class CategoryFragment extends SherlockFragment implements OnClickListener {

	private ImageView ivCategory1;
	private ImageView ivCategory2;
	private ImageView ivCategory3;
	private ImageView ivCategory4;
	private ImageView ivCategory5;
	private ImageView ivCategory6;
	private ImageView ivCategory7;
	private ImageView ivCategory8;
	private ImageView ivCategory9;
	private ImageView ivCategory10;
	private ImageView ivCategory11;
	private ImageView ivCategory12;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_category, container, false);
		
		ivCategory1 = (ImageView) rootView.findViewById(R.id.ivCategory1);
		ivCategory1.setOnClickListener(this);

		ivCategory2 = (ImageView) rootView.findViewById(R.id.ivCategory2);
		ivCategory2.setOnClickListener(this);
		
		ivCategory3 = (ImageView) rootView.findViewById(R.id.ivCategory3);
		ivCategory3.setOnClickListener(this);

		ivCategory4 = (ImageView) rootView.findViewById(R.id.ivCategory4);
		ivCategory4.setOnClickListener(this);
		
		ivCategory5 = (ImageView) rootView.findViewById(R.id.ivCategory5);
		ivCategory5.setOnClickListener(this);
		
		ivCategory6 = (ImageView) rootView.findViewById(R.id.ivCategory6);
		ivCategory6.setOnClickListener(this);
		
		ivCategory7 = (ImageView) rootView.findViewById(R.id.ivCategory7);
		ivCategory7.setOnClickListener(this);
		
		ivCategory8 = (ImageView) rootView.findViewById(R.id.ivCategory8);
		ivCategory8.setOnClickListener(this);
		
		ivCategory9 = (ImageView) rootView.findViewById(R.id.ivCategory9);
		ivCategory9.setOnClickListener(this);
		
		ivCategory10 = (ImageView) rootView.findViewById(R.id.ivCategory10);
		ivCategory10.setOnClickListener(this);
		
		ivCategory11 = (ImageView) rootView.findViewById(R.id.ivCategory11);
		ivCategory11.setOnClickListener(this);
		
		ivCategory12 = (ImageView) rootView.findViewById(R.id.ivCategory12);
		ivCategory12.setOnClickListener(this);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.animator.button_press));
		int pos = 0;
		
		switch (v.getId()) {
		case R.id.ivCategory1:
			pos = 0;
			break;
		case R.id.ivCategory2:
			pos = 1;
			break;
		case R.id.ivCategory3:
			pos = 2;
			break;
		case R.id.ivCategory4:
			pos = 3;
			break;
		case R.id.ivCategory5:
			pos = 4;
			break;
		case R.id.ivCategory6:
			pos = 5;
			break;
		case R.id.ivCategory7:
			pos = 6;
			break;
		case R.id.ivCategory8:
			pos = 7;
			break;
		case R.id.ivCategory9:
			pos = 8;
			break;
		case R.id.ivCategory10:
			pos = 9;
			break;
		case R.id.ivCategory11:
			pos = 10;
			break;
		case R.id.ivCategory12:
			pos = 11;
			break;
		}
		
		Intent i = new Intent(getActivity(), PagerListVideoActivity.class);
		i.putExtra(PagerListVideoActivity.STR_URL, ApiUrl.getVideoInCategoryUrl(getActivity().getResources().getStringArray(R.array.arr_cartegory_id)[pos], "", 1));
		i.putExtra(PagerListVideoActivity.STR_ACTION_BAR_TITLE, getActivity().getResources().getStringArray(R.array.arr_cartegory_name)[pos]);
		i.putExtra(PagerListVideoActivity.STR_DEFAULT_PAGE_INDEX, 2);
		
		getActivity().startActivity(i);
	}

}
