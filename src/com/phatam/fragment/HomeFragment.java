package com.phatam.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.phatam.R;
import com.phatam.activity.MainActivity;
import com.phatam.activity.PagerListVideoActivity;
import com.phatam.adapter.CategoryWithNumberAdapter;
import com.phatam.adapter.ListVideoAdapter;
import com.phatam.model.CategoryWithNumberModel;
import com.phatam.model.VideoModel;
import com.phatam.util.ListViewUtils;

public class HomeFragment extends SherlockFragment {
	public static int NUM_PAGES = 3;
	public static int VIDEO_PAGE = 1;

	public ArrayList<CategoryWithNumberModel> mListCategoryWithNumberModel = new ArrayList<CategoryWithNumberModel>();
	ListView mListViewCategory;
	CategoryWithNumberAdapter mAdapterCategory;

	public ArrayList<VideoModel> mArrListNewVideoModel = new ArrayList<VideoModel>();
	ListView mListViewNewVideo;
	ListVideoAdapter mListNewVideoAdapter;
	Button mButtons[] = new Button[3];
	Button mButtonMoreVideo;
	ViewPager mViewPager;

	private MainActivity mMainActivity;

	public void setMainActivity(MainActivity a) {
		mMainActivity = a;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container, false);

		// Declare list view show info about how many category, author
		String[] arrListCategory = getResources().getStringArray(R.array.list_category_array);
		String number = "";
		mListCategoryWithNumberModel.clear();
		for (int i = 0; i < 2; i++) {
			if (i == 0) number = "12";
			if (i == 1) number = "100";
			if (i > 1) number = "";
			mListCategoryWithNumberModel.add(new CategoryWithNumberModel(arrListCategory[i], number));
		}

		mListViewCategory = (ListView) rootView.findViewById(R.id.home_fragment_list_description);
		mAdapterCategory = new CategoryWithNumberAdapter(getSherlockActivity(), mListCategoryWithNumberModel);
		mListViewCategory.setAdapter(mAdapterCategory);
		ListViewUtils.getListViewSize(mListViewCategory);
		mListViewCategory.setOnItemClickListener(onCategoryItemClick);

		mButtons[0] = (Button) rootView.findViewById(R.id.home_fragment_btn_show_top_video);
		mButtons[1] = (Button) rootView.findViewById(R.id.home_fragment_btn_show_new_video);
		mButtons[2] = (Button) rootView.findViewById(R.id.home_fragment_btn_show_random_video);
		for (int i = 0; i < 3; i++) {
			mButtons[i].setOnClickListener(OnButtonClickListener);
			mButtons[i].setBackgroundColor(getResources().getColor(android.R.color.background_light));
			mButtons[i].setTag(i);
		}
		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
			mButtons[1].setBackgroundResource(R.drawable.red_gradient);
		else
			mButtons[1].setBackgroundResource(R.drawable.red_gradient);

		/**
		 * Commit fragment into parent fragment
		 */
		PullAndLoadmoreListVideoFragment fragment = new PullAndLoadmoreListVideoFragment(PagerListVideoActivity.TYPE_NEW_VIDEOS, PullAndLoadmoreListVideoFragment.ORDER_BY_ADDED);
		FragmentManager childmanager = getFragmentManager();
		childmanager.beginTransaction().replace(R.id.home_fragment_fragment, fragment).commit();

		return rootView;

	}

	private OnItemClickListener onCategoryItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos,
				long arg3) {
			// TODO Auto-generated method stub
			FragmentManager manger = getFragmentManager();
			Fragment fragment = null;
			String screenTitle = "";
			switch (pos) {
			case 0:
				fragment = new CategoryFragment();
				screenTitle = getString(R.string.category);
				break;
			case 1:
				fragment = new AuthorsFragment();
				screenTitle = getString(R.string.author);
				break;			
			}

			manger.beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), fragment).addToBackStack(null).commit();
			mMainActivity.getSupportActionBar().setTitle(screenTitle);
		}
	};

	// When click buttons to show type of video
	private View.OnClickListener OnButtonClickListener = new View.OnClickListener() {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			for (int i = 0; i < 3; i++)
				mButtons[i].setBackgroundColor(getResources().getColor(
						android.R.color.background_light));
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
				v.setBackgroundResource(R.drawable.red_gradient);
			else
				v.setBackgroundResource(R.drawable.red_gradient);
			VIDEO_PAGE = Integer.parseInt(v.getTag().toString());
			Fragment fragment = null;
			switch (VIDEO_PAGE) {
			case 0:
				fragment = new PullAndLoadmoreListVideoFragment(PagerListVideoActivity.TYPE_TOP_VIDEOS, PullAndLoadmoreListVideoFragment.ORDER_BY_RATING);
				break;
			case 1:
				fragment = new PullAndLoadmoreListVideoFragment(PagerListVideoActivity.TYPE_NEW_VIDEOS, PullAndLoadmoreListVideoFragment.ORDER_BY_RATING);
				break;
			case 2:
				fragment = new PullAndLoadmoreListVideoFragment(PagerListVideoActivity.TYPE_RANDOM_VIDEOS, PullAndLoadmoreListVideoFragment.ORDER_BY_RATING);
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
