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


import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.phatam.fragment.FImageInGallery;

public class ImagesGalleryFragmentPagerAdapter extends FragmentPagerAdapter {

	ArrayList<String> mImageUrls;
    
    public ImagesGalleryFragmentPagerAdapter(FragmentManager fm) {
    	super(fm);
    }

    /**
     * Must be set image url collection for view inside the ViewPager
     * @param pImageUrls
     */
    public void setImageUrls(ArrayList<String> pImageUrls) {
    	mImageUrls = pImageUrls;
    }
    
    @Override
    public Fragment getItem(int position) {
        return (new FImageInGallery()).setImageUrl(mImageUrls.get(position));
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }
}