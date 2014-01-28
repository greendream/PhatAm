package com.phatam.activity;

import com.phatam.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.phatam.adapter.TwoTextArrayAdapter;
import com.phatam.entity.HeaderItem;
import com.phatam.entity.SlidingMenuListItem;
import com.phatam.entity.SlidingMenuItem;
import com.phatam.fragment.AuthorsFragment;
import com.phatam.fragment.CategoryFragment;
import com.phatam.fragment.HomeFragment;
import com.phatam.util.AppStatus;

public class MainActivity extends SherlockFragmentActivity {

	SlidingMenu mSlidingMenu;
	ListView lvSlidingMenu;
	ArrayList<String> arrMenuTitle = new ArrayList<String>();
	private Menu option_menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_content);

		// Chage Actionbar background color
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
		
		LayoutInflater inflater = getLayoutInflater();
		View menu_frame = inflater.inflate(R.layout.layout_sliding_menu, null);

		// Setting Sliding menu behavior
		mSlidingMenu = new SlidingMenu(this);
		mSlidingMenu.setMode(SlidingMenu.LEFT);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		mSlidingMenu.setShadowDrawable(R.drawable.shadow);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		mSlidingMenu.setMenu(menu_frame);

		// Initial list view for menu_frame
		TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(this, getListMenuItem());
		lvSlidingMenu = (ListView) menu_frame.findViewById(R.id.list_view_in_sliding_menu);
		lvSlidingMenu.setAdapter(adapter);
		lvSlidingMenu.setOnItemClickListener(onItemClick);
		lvSlidingMenu.setDivider(null);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Show the home fragment for start-up 
		FragmentManager manager = getSupportFragmentManager();
		HomeFragment homeFragment = new HomeFragment();
		homeFragment.setMainActivity(this);
		manager.beginTransaction().replace(R.id.content_frame, homeFragment).commit();
	}

	private OnItemClickListener onItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			// TODO Auto-generated method stub
			FragmentManager manager = getSupportFragmentManager();
			Fragment fragment = null;
			String name = arrMenuTitle.get(position);
			mSlidingMenu.toggle();
			
			boolean isOnline = false;
			if (AppStatus.getInstance(getApplicationContext()).isOnline(getApplicationContext())) {
				isOnline = true;
			}
			
			if (name.equals("Home")) {
				HomeFragment homeFragment = new HomeFragment();
				homeFragment.setMainActivity(MainActivity.this);
				manager.beginTransaction()
						.replace(R.id.content_frame, homeFragment)
						.addToBackStack(null).commit();
				
			} else if (name.equals(getString(R.string.category))) {
				// Set the screen title
				getSupportActionBar().setTitle(name);
				fragment = new CategoryFragment();
				manager.beginTransaction()
						.replace(R.id.content_frame, fragment)
						.addToBackStack(null).commit();
				// onCreateOptionsMenuBase(option_menu);
			} else if (name.equals(getString(R.string.author))) {
				// Set the screen title
				getSupportActionBar().setTitle(name);
				fragment = new AuthorsFragment();
				manager.beginTransaction()
						.replace(R.id.content_frame, fragment)
						.addToBackStack(null).commit();
				onCreateOptionsMenu1(option_menu);
			} else if (name.equals(getString(R.string.top_videos))) {
				Intent i = new Intent(getApplicationContext(),
						PagerListVideoActivity.class);
				i.putExtra("type", PagerListVideoActivity.TYPE_TOP_VIDEOS);
				i.putExtra("isOnline", isOnline);
				startActivity(i);
				/*fragment = new VideoFragment(0);
				manager.beginTransaction().replace(R.id.content_frame,
				fragment).addToBackStack(null).commit();
				onCreateOptionsMenu1(option_menu);*/
			} else if (name.equals(getString(R.string.new_videos))) {
				Intent i = new Intent(getApplicationContext(),
						PagerListVideoActivity.class);
				i.putExtra("type", PagerListVideoActivity.TYPE_NEW_VIDEOS);
				i.putExtra("isOnline", isOnline);
				startActivity(i);
				
				// fragment = new video_fragment(1);
				// manager.beginTransaction().replace(R.id.content_frame,
				// fragment).addToBackStack(null).commit();
				// onCreateOptionsMenu1(option_menu);
			} else if (name.equals(getString(R.string.random_videos))) {
				Intent i = new Intent(getApplicationContext(),
						PagerListVideoActivity.class);
				i.putExtra("type", PagerListVideoActivity.TYPE_RANDOM_VIDEOS);
				i.putExtra("isOnline", isOnline);
				startActivity(i);
				// fragment = new video_fragment(2);
				// manager.beginTransaction().replace(R.id.content_frame,
				// fragment).addToBackStack(null).commit();
				// onCreateOptionsMenu1(option_menu);
			}
			
			

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.clear();
		getSupportMenuInflater().inflate(R.menu.main, menu);
		option_menu = menu;
		return true;
	}

	public boolean onCreateOptionsMenuBase(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getSupportMenuInflater();
		menu.clear();
		inflater.inflate(R.menu.main, menu);
		option_menu = menu;
		return true;
	}

	// @Override
	public boolean onCreateOptionsMenu1(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		menu.clear();
		inflater.inflate(R.menu.search_menu, menu);
		option_menu = menu;

		// Associate searchable configuration with the SearchView
		// SearchManager searchManager =
		// (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		// SearchView searchView =
		// (SearchView) menu.findItem(R.id.search).getActionView();
		// searchView.setSearchableInfo(
		// searchManager.getSearchableInfo(getComponentName()));
		//

		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mSlidingMenu.isMenuShowing()) {
			mSlidingMenu.toggle();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			Log.v("click option menu", "11111");
			mSlidingMenu.toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public List<SlidingMenuItem> getListMenuItem() {
		List<SlidingMenuItem> items = new ArrayList<SlidingMenuItem>();
		String category = getResources().getString(R.string.category_title);
		String[] list_category_array = getResources().getStringArray(R.array.list_category_array);
		
		final Integer[] Icons_category = {	R.drawable.ic_menu_category,
											R.drawable.ic_menu_people,
											R.drawable.ic_menu_top,
											R.drawable.ic_menu_new,
											R.drawable.ic_menu_random,
											R.drawable.ic_menu_contact
										};
		
		items.add(new SlidingMenuListItem("Home", R.drawable.home, getApplicationContext()));
		// Start to add category to list view navigation
		items.add(new HeaderItem(category));
		arrMenuTitle.add("Home");
		arrMenuTitle.add(category);

		for (int i = 0; i < list_category_array.length; i++) {
			items.add(new SlidingMenuListItem(list_category_array[i], Icons_category[i],
					getApplicationContext()));
			arrMenuTitle.add(list_category_array[i]);
		}

		String setting = getResources().getString(R.string.setting_title);
		String[] list_setting_array = getResources().getStringArray(
				R.array.list_setting_array);
		final Integer[] Icons_setting = { R.drawable.ic_menu_setting,
				R.drawable.ic_menu_delete

		};
		// Start to add setting to list view navigation
		items.add(new HeaderItem(setting));
		arrMenuTitle.add(setting);
		for (int i = 0; i < list_setting_array.length; i++) {
			items.add(new SlidingMenuListItem(list_setting_array[i], Icons_setting[i],
					getApplicationContext()));
			arrMenuTitle.add(list_setting_array[i]);
		}
		return items;
	}

}
