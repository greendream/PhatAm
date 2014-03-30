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

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.phatam.R;
import com.phatam.activities.MainActivity;
import com.phatam.util.TimeTicker;
import com.phatam.websevice.ApiUrl;

public class HomeFragment extends SherlockFragment {
	private FragmentManager mFragmentManager; 
	private MainActivity mMainActivity;

	private TimeTicker mTimeTickerChangeVideoInCategory;

	public void setMainActivity(MainActivity a) {
		mMainActivity = a;
		mMainActivity.getSupportActionBar().setTitle(R.string.app_name);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		mFragmentManager = getFragmentManager();

		
		/**
		 * Commit fragment into parent fragment
		 */
		
		// Show 5 new video in the banner auto sliding
		VideoBannerAutoSlidingFragment bannerFragment = new VideoBannerAutoSlidingFragment();
		bannerFragment.setVideoSourceLink(ApiUrl.getNewVideoUrl(ApiUrl.ORDER_BY_UPLOAD_DATE, 5));				
		mFragmentManager.beginTransaction().replace(R.id.banner_gallery_new_video, bannerFragment).commit();
		

		// Create timer to control auto Sliding with interval 12 second per time
		mTimeTickerChangeVideoInCategory = new TimeTicker(12000);

		// Kinh-Chu Category		
		FourVideosPreviewFragment layoutKinhChuFragment = new FourVideosPreviewFragment();
		layoutKinhChuFragment.setCategoryIndex(7);
		mTimeTickerChangeVideoInCategory.addOnTimeTickerListener(layoutKinhChuFragment);
		layoutKinhChuFragment.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getActivity().getResources().getStringArray(R.array.arr_cartegory_id)[7], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		layoutKinhChuFragment.setCategoryName(getActivity().getResources().getStringArray(R.array.arr_cartegory_name)[7]);
		mFragmentManager.beginTransaction().replace(R.id.layoutKinhChu, layoutKinhChuFragment).commit();
		
		// Phap-Thoai Category
		FourVideosPreviewFragment layoutPhapThoaiFragment = new FourVideosPreviewFragment();
		layoutPhapThoaiFragment.setCategoryIndex(8);
		mTimeTickerChangeVideoInCategory.addOnTimeTickerListener(layoutPhapThoaiFragment);
		layoutPhapThoaiFragment.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getActivity().getResources().getStringArray(R.array.arr_cartegory_id)[8], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		layoutPhapThoaiFragment.setCategoryName(getActivity().getResources().getStringArray(R.array.arr_cartegory_name)[8]);			
		mFragmentManager.beginTransaction().replace(R.id.layoutPhapThoai, layoutPhapThoaiFragment).commit();

		// Sach-Noi Category
		FourVideosPreviewFragment layoutSachNoiFragment = new FourVideosPreviewFragment();
		layoutSachNoiFragment.setCategoryIndex(9);
		mTimeTickerChangeVideoInCategory.addOnTimeTickerListener(layoutSachNoiFragment);
		layoutSachNoiFragment.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getActivity().getResources().getStringArray(R.array.arr_cartegory_id)[9], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		layoutSachNoiFragment.setCategoryName(getActivity().getResources().getStringArray(R.array.arr_cartegory_name)[9]);			
		mFragmentManager.beginTransaction().replace(R.id.layoutSachNoi, layoutSachNoiFragment).commit();		
		
		// Tan-Nhac-Phat-Giao Category
		FourVideosPreviewFragment layoutTanNhacPhatGiaoFragment = new FourVideosPreviewFragment();
		layoutTanNhacPhatGiaoFragment.setCategoryIndex(3);
		mTimeTickerChangeVideoInCategory.addOnTimeTickerListener(layoutTanNhacPhatGiaoFragment);
		layoutTanNhacPhatGiaoFragment.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getActivity().getResources().getStringArray(R.array.arr_cartegory_id)[3], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		layoutTanNhacPhatGiaoFragment.setCategoryName(getActivity().getResources().getStringArray(R.array.arr_cartegory_name)[3]);
		mFragmentManager.beginTransaction().replace(R.id.layoutTanNhacPhatGiao, layoutTanNhacPhatGiaoFragment).commit();
		
		// Co-Nhac-Phat-Giao Category
		FourVideosPreviewFragment layoutCoNhacPhatGiaoFragment = new FourVideosPreviewFragment();
		layoutCoNhacPhatGiaoFragment.setCategoryIndex(4);
		mTimeTickerChangeVideoInCategory.addOnTimeTickerListener(layoutCoNhacPhatGiaoFragment);
		layoutCoNhacPhatGiaoFragment.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getActivity().getResources().getStringArray(R.array.arr_cartegory_id)[4], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		layoutCoNhacPhatGiaoFragment.setCategoryName(getActivity().getResources().getStringArray(R.array.arr_cartegory_name)[4]);
		mFragmentManager.beginTransaction().replace(R.id.layoutCoNhacPhatGiao, layoutCoNhacPhatGiaoFragment).commit();
				
		// Nhac-Thien Category
		FourVideosPreviewFragment layoutNhacThienFragment = new FourVideosPreviewFragment();
		layoutNhacThienFragment.setCategoryIndex(5);
		mTimeTickerChangeVideoInCategory.addOnTimeTickerListener(layoutNhacThienFragment);
		layoutNhacThienFragment.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getActivity().getResources().getStringArray(R.array.arr_cartegory_id)[5], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		layoutNhacThienFragment.setCategoryName(getActivity().getResources().getStringArray(R.array.arr_cartegory_name)[5]);
		mFragmentManager.beginTransaction().replace(R.id.layoutNhacThien, layoutNhacThienFragment).commit();
						
		// Phim-Truyen Category
		FourVideosPreviewFragment layoutPhimTruyenFragment = new FourVideosPreviewFragment();
		layoutPhimTruyenFragment.setCategoryIndex(1);
		mTimeTickerChangeVideoInCategory.addOnTimeTickerListener(layoutPhimTruyenFragment);
		layoutPhimTruyenFragment.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getActivity().getResources().getStringArray(R.array.arr_cartegory_id)[1], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		layoutPhimTruyenFragment.setCategoryName(getActivity().getResources().getStringArray(R.array.arr_cartegory_name)[1]);
		mFragmentManager.beginTransaction().replace(R.id.layoutPhimTruyen, layoutPhimTruyenFragment).commit();

		// Phim-Tai-Lieu Category
		FourVideosPreviewFragment layoutPhimTaiLieuFragment = new FourVideosPreviewFragment();
		layoutPhimTaiLieuFragment.setCategoryIndex(2);
		mTimeTickerChangeVideoInCategory.addOnTimeTickerListener(layoutPhimTaiLieuFragment);
		layoutPhimTaiLieuFragment.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getActivity().getResources().getStringArray(R.array.arr_cartegory_id)[2], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		layoutPhimTaiLieuFragment.setCategoryName(getActivity().getResources().getStringArray(R.array.arr_cartegory_name)[2]);
		mFragmentManager.beginTransaction().replace(R.id.layoutPhimTaiLieu, layoutPhimTaiLieuFragment).commit();
		
		// Phat-Phap-Nhiem-Mau Category
		FourVideosPreviewFragment layoutPhatPhapNhiemMauFragment = new FourVideosPreviewFragment();
		layoutPhatPhapNhiemMauFragment.setCategoryIndex(0);
		mTimeTickerChangeVideoInCategory.addOnTimeTickerListener(layoutPhatPhapNhiemMauFragment);
		layoutPhatPhapNhiemMauFragment.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getActivity().getResources().getStringArray(R.array.arr_cartegory_id)[0], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		layoutPhatPhapNhiemMauFragment.setCategoryName(getActivity().getResources().getStringArray(R.array.arr_cartegory_name)[0]);
		mFragmentManager.beginTransaction().replace(R.id.layoutPhatPhapNhiemMau, layoutPhatPhapNhiemMauFragment).commit();
		
		// Anh-Sang-Phat-Phap Category
		FourVideosPreviewFragment layoutAnhSangPhatPhapFragment = new FourVideosPreviewFragment();
		layoutAnhSangPhatPhapFragment.setCategoryIndex(6);
		mTimeTickerChangeVideoInCategory.addOnTimeTickerListener(layoutAnhSangPhatPhapFragment);
		layoutAnhSangPhatPhapFragment.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getActivity().getResources().getStringArray(R.array.arr_cartegory_id)[6], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		layoutAnhSangPhatPhapFragment.setCategoryName(getActivity().getResources().getStringArray(R.array.arr_cartegory_name)[6]);
		mFragmentManager.beginTransaction().replace(R.id.layoutAnhSangPhatPhap, layoutAnhSangPhatPhapFragment).commit();
				
		// Mot-Ngay-An-Lac Category
		FourVideosPreviewFragment layoutMotNgayAnLacFragment = new FourVideosPreviewFragment();
		layoutMotNgayAnLacFragment.setCategoryIndex(10);
		mTimeTickerChangeVideoInCategory.addOnTimeTickerListener(layoutMotNgayAnLacFragment);
		layoutMotNgayAnLacFragment.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getActivity().getResources().getStringArray(R.array.arr_cartegory_id)[10], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		layoutMotNgayAnLacFragment.setCategoryName(getActivity().getResources().getStringArray(R.array.arr_cartegory_name)[10]);
		mFragmentManager.beginTransaction().replace(R.id.layoutMotNgayAnLac, layoutMotNgayAnLacFragment).commit();

		// Phim-Hoat-Hinh Category
		FourVideosPreviewFragment layoutPhimHoatHinhFragment = new FourVideosPreviewFragment();
		layoutPhimHoatHinhFragment.setCategoryIndex(11);
		mTimeTickerChangeVideoInCategory.addOnTimeTickerListener(layoutPhimHoatHinhFragment);
		layoutPhimHoatHinhFragment.setUrlGetVideoList(ApiUrl.getVideoInCategoryUrl(getActivity().getResources().getStringArray(R.array.arr_cartegory_id)[11], ApiUrl.ORDER_BY_UPLOAD_DATE, 0));
		layoutPhimHoatHinhFragment.setCategoryName(getActivity().getResources().getStringArray(R.array.arr_cartegory_name)[11]);
		mFragmentManager.beginTransaction().replace(R.id.layoutPhimHoatHinh, layoutPhimHoatHinhFragment).commit();


		// Start timer to change preview video automatic
		// mTimeTickerChangeVideoInCategory.start();
		
		return rootView;

	}

	@Override
	public void onAttach(Activity activity) {
		((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.sliding_menu_lable_home);

		super.onAttach(activity);
	}
	
	
}
