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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.phatam.fragment.FArtist;
import com.phatam.fragment.FLoadmoreListVideo;
import com.phatam.websevice.ApiUrl;

public class SearchFragmentAdapter extends FragmentPagerAdapter {
    protected static final String[] CONTENT = new String[] { "VIDEO", "TÁC GIẢ" };
    private int mCount = CONTENT.length;
    private String mKeyWord;
    private FLoadmoreListVideo mFragmentVideoResult;
    private FArtist mFragmentAuthorResult;

    public SearchFragmentAdapter(FragmentManager fm, String pKeyWord) {
        super(fm);
        mKeyWord = pKeyWord.replace(" ", "%20");
        
        mFragmentVideoResult = new FLoadmoreListVideo().setUrl(ApiUrl.getSearchVideoUrl(mKeyWord, ApiUrl.ORDER_BY_UPLOAD_DATE, -1));
        
        mFragmentAuthorResult = new FArtist().setUrl(ApiUrl.getSerchArtistNameUrl(mKeyWord, ApiUrl.ORDER_BY_ARTIST_NAME, -1));
    }
    
    public void onKeyWordChange(String newKeyword) {
    	mKeyWord = newKeyword;
    	mFragmentVideoResult.onRefreshListDataByUrl(ApiUrl.getSearchVideoUrl(mKeyWord, ApiUrl.ORDER_BY_UPLOAD_DATE, -1));
        mFragmentAuthorResult.onRefreshListDataByUrl(ApiUrl.getSerchArtistNameUrl(mKeyWord, ApiUrl.ORDER_BY_ARTIST_NAME, -1));
    }

    @Override
    public Fragment getItem(int position) {
    	switch (position) {
    	case 0:
    		return mFragmentVideoResult;
    	case 1:
    		return mFragmentAuthorResult;
    	}
    	
    	return new Fragment();
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return SearchFragmentAdapter.CONTENT[position % CONTENT.length];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
    

}