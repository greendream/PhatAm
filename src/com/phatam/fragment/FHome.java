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
import com.phatam.config.GlobalData;
import com.phatam.customviews.ViewFourVideosCategoryPreview;
import com.phatam.customviews.ViewSixVideosCategoryPreview;
import com.phatam.model.MCategoryReviewItem;
import com.phatam.websevice.ApiUrl;

public class FHome extends SherlockFragment {
	private FragmentManager mFragmentManager; 
	private MainActivity mMainActivity;
	private ViewSixVideosCategoryPreview pvPhapThoai;
	private ViewFourVideosCategoryPreview pvKinhChu;
	private ViewFourVideosCategoryPreview pvSachNoi;
	private ViewFourVideosCategoryPreview pvTanNhacPhatGiao;
	private ViewFourVideosCategoryPreview pvCoNhacPhatGiao;
	private ViewFourVideosCategoryPreview pvNhacThien;
	private ViewFourVideosCategoryPreview pvPhimTruyen;
	private ViewFourVideosCategoryPreview pvPhimTaiLieu;
	private ViewFourVideosCategoryPreview pvPhatPhapNhiemMau;
	private ViewFourVideosCategoryPreview pvAnhSangPhatPhap;
	private ViewFourVideosCategoryPreview pvMotNgayAnLac;
	private ViewFourVideosCategoryPreview pvPhimHoatHinh;
	

	public void setMainActivity(MainActivity a) {
		mMainActivity = a;
		mMainActivity.getSupportActionBar().setTitle(R.string.app_name);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		mFragmentManager = getFragmentManager();

		// Show 5 new video in the banner auto sliding
		FVideoBannerAutoSliding bannerFragment = new FVideoBannerAutoSliding();
		bannerFragment.setVideoSourceLink(ApiUrl.getNewVideosUrl(0));				
		mFragmentManager.beginTransaction().replace(R.id.banner_gallery_new_video, bannerFragment).commit();
		
		// Phap-Thoai Category
		pvPhapThoai = (ViewSixVideosCategoryPreview) rootView.findViewById(R.id.pvPhapThoai);
		pvPhapThoai.setCategoryReviewItem(GlobalData.arrCategoryReviewItems.get(0));
		pvPhapThoai.init(getActivity());
		
		// Kinh-Chu Category
		pvKinhChu = (ViewFourVideosCategoryPreview) rootView.findViewById(R.id.pvKinhChu);
		pvKinhChu.setCategoryReviewItem(GlobalData.arrCategoryReviewItems.get(1));
		pvKinhChu.init(getActivity());
		
		// Sach-Noi Category
		pvSachNoi = (ViewFourVideosCategoryPreview) rootView.findViewById(R.id.pvSachNoi);
		pvSachNoi.setCategoryReviewItem(GlobalData.arrCategoryReviewItems.get(2));
		pvSachNoi.init(getActivity());
		
		// Tan-Nhac-Phat-Giao Category
		pvTanNhacPhatGiao = (ViewFourVideosCategoryPreview) rootView.findViewById(R.id.pvTanNhacPhatGiao);
		pvTanNhacPhatGiao.setCategoryReviewItem(GlobalData.arrCategoryReviewItems.get(3));
		pvTanNhacPhatGiao.init(getActivity());
		
		// Co-Nhac-Phat-Giao Category
		pvCoNhacPhatGiao = (ViewFourVideosCategoryPreview) rootView.findViewById(R.id.pvCoNhacPhatGiao);
		pvCoNhacPhatGiao.setCategoryReviewItem(GlobalData.arrCategoryReviewItems.get(4));
		pvCoNhacPhatGiao.init(getActivity());
				
		// Nhac-Thien Category
		pvNhacThien = (ViewFourVideosCategoryPreview) rootView.findViewById(R.id.pvNhacThien);
		pvNhacThien.setCategoryReviewItem(GlobalData.arrCategoryReviewItems.get(5));
		pvNhacThien.init(getActivity());
		
		// Phim-Truyen Category
		pvPhimTruyen = (ViewFourVideosCategoryPreview) rootView.findViewById(R.id.pvPhimTruyen);
		pvPhimTruyen.setCategoryReviewItem(GlobalData.arrCategoryReviewItems.get(6));
		pvPhimTruyen.init(getActivity());

		// Phim-Tai-Lieu Category
		pvPhimTaiLieu = (ViewFourVideosCategoryPreview) rootView.findViewById(R.id.pvPhimTaiLieu);
		pvPhimTaiLieu.setCategoryReviewItem(GlobalData.arrCategoryReviewItems.get(7));
		pvPhimTaiLieu.init(getActivity());
		
		// Phat-Phap-Nhiem-Mau Category
		pvPhatPhapNhiemMau = (ViewFourVideosCategoryPreview) rootView.findViewById(R.id.pvPhatPhapNhiemMau);
		pvPhatPhapNhiemMau.setCategoryReviewItem(GlobalData.arrCategoryReviewItems.get(8));
		pvPhatPhapNhiemMau.init(getActivity());
		
		// Anh-Sang-Phat-Phap Category
		pvAnhSangPhatPhap = (ViewFourVideosCategoryPreview) rootView.findViewById(R.id.pvAnhSangPhatPhap);
		pvAnhSangPhatPhap.setCategoryReviewItem(GlobalData.arrCategoryReviewItems.get(9));
		pvAnhSangPhatPhap.init(getActivity());
				
		// Mot-Ngay-An-Lac Category
		pvMotNgayAnLac = (ViewFourVideosCategoryPreview) rootView.findViewById(R.id.pvMotNgayAnLac);
		pvMotNgayAnLac.setCategoryReviewItem(GlobalData.arrCategoryReviewItems.get(10));
		pvMotNgayAnLac.init(getActivity());

		// Phim-Hoat-Hinh Category
		pvPhimHoatHinh = (ViewFourVideosCategoryPreview) rootView.findViewById(R.id.pvPhimHoatHinh);
		pvPhimHoatHinh.setCategoryReviewItem(GlobalData.arrCategoryReviewItems.get(11));
		pvPhimHoatHinh.init(getActivity());

		
			
		return rootView;

	}

	@Override
	public void onAttach(Activity activity) {
		((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.sliding_menu_lable_home);

		super.onAttach(activity);
	}
	
	
}
