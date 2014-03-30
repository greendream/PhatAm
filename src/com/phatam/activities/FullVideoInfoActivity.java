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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.greendream.openyoutube.MediaController.OnToggleFullScreenControler;
import com.greendream.openyoutube.OpenYouTubePlayerSupportFragment;
import com.greendream.openyoutube.OpenYouTubePlayerSupportFragment.OnRetrieveVideoInfoListener;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.phatam.BuildConfig;
import com.phatam.R;
import com.phatam.config.GlobalData;
import com.phatam.entities.Episode;
import com.phatam.entities.Tag;
import com.phatam.entities.VideoItem;
import com.phatam.fragment.EpisodeListViewFragment;
import com.phatam.fragment.RelativeVideoListViewFragment;
import com.phatam.interfaces.OnConnectionStatusChangeListener;
import com.phatam.playback.PhatAmConnectionStatusReceiver;
import com.phatam.playback.PhatAmPlayBackService;
import com.phatam.util.ConnectionUtil;
import com.phatam.websevice.OnGetJsonListener;
import com.phatam.websevice.ServiceGetVideoInfo;

public class FullVideoInfoActivity extends SherlockFragmentActivity implements
		OnClickListener, OnItemClickListener, OnToggleFullScreenControler, OnRetrieveVideoInfoListener, MediaPlayer.OnCompletionListener, OnConnectionStatusChangeListener {
	public static final String INFO_VIDEO_UNIQUE_ID = "unique_id";
	
	// Video Player
	private OpenYouTubePlayerSupportFragment mVideoPlayerFragment;
	private boolean mIsFullScreen;
	private RelativeLayout mLayoutVideo;
	private LinearLayout mOtherViews;
	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;

	// Tab manager
	public static final int TAB_INFO_VIDEOS = 0;
	public static final int TAB_EPISODE_VIDEOS = 1;
	public static final int TAB_RELATIVE_VIDEOS = 2;
	int mCurrentTab = TAB_INFO_VIDEOS;
	Button btnEpisodeTab;
	Button btnRelativeVideoTab;

	// UI controller
	ListView mListViewEpisode;

	// Adapter for relative and Episode video
	ArrayAdapter<String> mEpisodeAdapter;
	ArrayList<String> mListEpisodeData = new ArrayList<String>();

	VideoItem mVideoItem;

	Button btnVideoInfoTab;

	int mObligeScreenOrientation = -1; // default is not oblige screen
										// orientation
	boolean mRotateMatchedOrientation = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		PhatAmConnectionStatusReceiver.addConnectionObserver(this);
		
		super.onCreate(savedInstanceState);
		
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
		
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			mIsFullScreen = false;
		} else {
			mIsFullScreen = true;
		}

		// Change ActionBar background color
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(getResources().getColor(R.color.blue)));

		// Get full video info
		String strUniqueId = getIntent().getExtras().getString(INFO_VIDEO_UNIQUE_ID);
		mVideoItem = new VideoItem();
		mVideoItem.setUniqueId(strUniqueId);
		
		setContentView(R.layout.activity_full_video_info);
		((LinearLayout) findViewById(R.id.layoutVideoInfo))
				.setVisibility(View.GONE);

		mLayoutVideo = (RelativeLayout) findViewById(R.id.layoutVideo);
		mOtherViews = (LinearLayout) findViewById(R.id.otherViews);

		// Setting size for video preview area
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		final int h = displayMetrics.heightPixels;
		final int w = displayMetrics.widthPixels;
		SCREEN_WIDTH = (w < h) ? w : h;
		SCREEN_HEIGHT = (h > w) ? h : w;
		mLayoutVideo.setLayoutParams(new LinearLayout.LayoutParams(
				SCREEN_WIDTH, SCREEN_HEIGHT * 460 / 1280));

		getVideoFullInfo();

		doLayout();
		
		onConnectionStatusChange();
    	
	}

	public void getVideoFullInfo() {
		((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
		
		final ArrayList<Episode> episodes = new ArrayList<Episode>();
		final ArrayList<VideoItem> relatedVideos = new ArrayList<VideoItem>();
		final ArrayList<Tag> tags = new ArrayList<Tag>();

		ServiceGetVideoInfo serviceGetVideoInfo = new ServiceGetVideoInfo();
		serviceGetVideoInfo.addOnGetJsonListener(new OnGetJsonListener() {
			
			@Override
			public void onGetJsonFail(String response) {
				((ProgressBar) findViewById(R.id.progressBar))
				.setVisibility(View.GONE);
			}
			
			@Override
			public void onGetJsonCompleted(String response) {
				Log.i("GET FULL VIDEO INFO COMPLETED", response);

				JSONArray jArray = null;

				try {
					JSONObject jsonServerResponse = new JSONObject(response);
					
					// Get Episode video list
					jArray = jsonServerResponse.getJSONArray("episode");
					Log.v("EPISODE : array received", "" + jArray.toString());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject line_object = jArray.getJSONObject(i);
						String uniq_id = line_object.getString("uniq_id");
						String yt_id = line_object.getString("yt_id");
						String yt_thumb = line_object.getString("yt_thumb");
						String episode = line_object.getString("episode");
						int episode_id = line_object.getInt("episode_id");
						Episode item = new Episode(uniq_id, episode_id, yt_id,
								yt_thumb, episode);
						episodes.add(item);
					}

					
					// Get related video list
					jArray = jsonServerResponse.getJSONArray("related");
					Log.v("array received", "" + jArray.toString());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject line_object = jArray.getJSONObject(i);
						String uniq_id = line_object.getString("uniq_id");
						String artist = line_object.getString("artist");
						String video_title = line_object
								.getString("video_title");
						String description = line_object
								.getString("description");
						String yt_thumb = line_object.getString("yt_thumb");
						int site_views = line_object.getInt("site_views");
						//String mp3 = line_object.getString("audio");
						String yt_id = line_object.getString("yt_id");
						String category = line_object.getString("category");
						VideoItem item = new VideoItem(uniq_id, artist,
								video_title, description, yt_id, yt_thumb,
								site_views, category);
						relatedVideos.add(item);
					}

					// Get Tag
					jArray = jsonServerResponse.getJSONArray("tags");
					Log.v("TAG : array received", "" + jArray.toString());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject line_object = jArray.getJSONObject(i);
						String tag = line_object.getString("tag");
						String safe_tag = line_object.getString("safe_tag");
						Tag item = new Tag(tag, safe_tag);
						tags.add(item);
					}
				
					// Get video detail info
					JSONObject line_object = jsonServerResponse.getJSONObject("info");
					Log.v("INFO : object received", "" + line_object.toString());
					String uniq_id = line_object.getString("uniq_id");
					String artist = line_object.getString("artist");
					String video_title = line_object.getString("video_title");
					String description = line_object.getString("description");
					String yt_thumb = line_object.getString("yt_thumb");
					int site_views = line_object.getInt("site_views");
					String yt_id = line_object.getString("yt_id");
					String category = line_object.getString("category");
					mVideoItem = new VideoItem(uniq_id, artist, video_title,
							description, yt_id, yt_thumb, site_views, category);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				// set video properties
				mVideoItem.setEpisode(episodes);
				mVideoItem.setRelated(relatedVideos);
				mVideoItem.setTag(tags);

				((ProgressBar) findViewById(R.id.progressBar)).setVisibility(View.GONE);
				fillDataToComponent();
			}
		});
		
		serviceGetVideoInfo.getVideoInfo(mVideoItem.getUniqueId());

	}

	private void fillDataToComponent() {

		// Fill data about video info to UI
		btnVideoInfoTab = (Button) findViewById(R.id.btnVideoInfoTab);
		btnVideoInfoTab.setOnClickListener(this);

		btnEpisodeTab = (Button) findViewById(R.id.btnEpisodeTab);
		btnEpisodeTab.setOnClickListener(this);

		btnRelativeVideoTab = (Button) findViewById(R.id.btnRelativeVideoTab);
		btnRelativeVideoTab.setOnClickListener(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(mVideoItem.getVideoTitle());
		getSupportActionBar().setSubtitle(mVideoItem.getVideoArtist());

		// Fill data to Video-info Tab
		((TextView) findViewById(R.id.tvVideoTitle)).setText(": " + mVideoItem.getVideoTitle());
		((TextView) findViewById(R.id.tvVideoArtist)).setText(": " + mVideoItem.getVideoArtist());
		
		if (mVideoItem.getEpisodes().size() > 0) {
			((TextView) findViewById(R.id.tvEpisode)).setText(": " + mVideoItem.getEpisodeId() + "/" + (mVideoItem.getEpisodes().size() + 1));
		} else {
			((LinearLayout) findViewById(R.id.layoutEpisodeIndex)).setVisibility(View.GONE);
		}
		
		((TextView) findViewById(R.id.tvCategoryName)).setText(": " + getCategoryName(mVideoItem.getCategory()));
		((TextView) findViewById(R.id.tvYoutubeView)).setText(": " + mVideoItem.getYoutubeView() + "");
		
		if ("".equals(mVideoItem.getDescription())) {
			((TextView) findViewById(R.id.tvDescription)).setText("...");
		} else {
			((TextView) findViewById(R.id.tvDescription)).setText(Html
					.fromHtml(mVideoItem
					.getDescription()));
		}

		// Fill data to Episode Video Tab
		mVideoItem.sortEpisodeIncByEpisodeId();
		EpisodeListViewFragment fragmentEpisodeVideo = new EpisodeListViewFragment(
				mVideoItem);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.flEpisode, fragmentEpisodeVideo).commit();
		fragmentEpisodeVideo.setOnVideoItemClicked(this);

		// Fill data to Relative Video Tab
		RelativeVideoListViewFragment fragmentRelativeVideo = new RelativeVideoListViewFragment(
				mVideoItem);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.flRelativeVideo, fragmentRelativeVideo).commit();
		fragmentRelativeVideo.setOnVideoItemClicked(this);

		// Prepare for video player
		mVideoPlayerFragment = new OpenYouTubePlayerSupportFragment();
		mVideoPlayerFragment.setOnRetrieveVideoInfoListener(this);
		mVideoPlayerFragment.setFullScreenController(this);
		mVideoPlayerFragment.setOnMediaPlayerCompletionListener(this);
		
		mVideoPlayerFragment.setVideoIdUri(Uri.parse("ytv://" + mVideoItem.getYoutubeId()));
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.videoPlayerPlace, mVideoPlayerFragment).commit();

		// Show component when fill data completed
		if (mCurrentTab == TAB_INFO_VIDEOS) {
			((LinearLayout) findViewById(R.id.layoutVideoInfo))
					.setVisibility(View.VISIBLE);
		}

		if (PhatAmPlayBackService.getMediaPlayer() != null) {
			mVideoPlayerFragment.setMediaPlayer(PhatAmPlayBackService.getMediaPlayer());
		}
	}

	/**
	 * Get category name by id
	 * 
	 * @param categoryId
	 * @return
	 */
	private String getCategoryName(String categoryId) {
		if (categoryId == null) {
			return "";
		}

		String[] categoryIds = getResources().getStringArray(
				R.array.arr_cartegory_id);
		String[] categoryNames = getResources().getStringArray(
				R.array.arr_cartegory_name);

		for (int i = 0; i < categoryIds.length; i++) {
			if (categoryIds[i].equals(categoryId)) {
				return categoryNames[i];
			}
		}

		return "";
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
		switch (v.getId()) {
		case R.id.btnVideoInfoTab:
			mCurrentTab = TAB_INFO_VIDEOS;
			btnVideoInfoTab.setTextColor(getResources().getColor(R.color.blue));
			((RelativeLayout) findViewById(R.id.rlVideoInfoTabSelector))
					.setBackgroundColor(getResources().getColor(R.color.blue));

			btnEpisodeTab.setTextColor(0xff000000);
			((RelativeLayout) findViewById(R.id.rlEpisodeSelector))
					.setBackgroundColor(0x00000000);

			btnRelativeVideoTab.setTextColor(0xff000000);
			((RelativeLayout) findViewById(R.id.rlRelativeVideoSelector))
					.setBackgroundColor(0x00000000);

			((LinearLayout) findViewById(R.id.layoutVideoInfo))
					.setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.layoutEpisode))
					.setVisibility(View.GONE);
			((LinearLayout) findViewById(R.id.layoutRelativeVideo))
					.setVisibility(View.GONE);
			break;

		case R.id.btnEpisodeTab:
			mCurrentTab = TAB_EPISODE_VIDEOS;
			btnVideoInfoTab.setTextColor(0xff000000);
			((RelativeLayout) findViewById(R.id.rlVideoInfoTabSelector))
					.setBackgroundColor(0x00000000);

			btnEpisodeTab.setTextColor(getResources().getColor(R.color.blue));
			((RelativeLayout) findViewById(R.id.rlEpisodeSelector))
					.setBackgroundColor(getResources().getColor(R.color.blue));

			btnRelativeVideoTab.setTextColor(0xff000000);
			((RelativeLayout) findViewById(R.id.rlRelativeVideoSelector))
					.setBackgroundColor(0x00000000);

			((LinearLayout) findViewById(R.id.layoutVideoInfo))
					.setVisibility(View.GONE);
			((LinearLayout) findViewById(R.id.layoutEpisode))
					.setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.layoutRelativeVideo))
					.setVisibility(View.GONE);
			break;

		case R.id.btnRelativeVideoTab:
			mCurrentTab = TAB_RELATIVE_VIDEOS;
			btnVideoInfoTab.setTextColor(0xff000000);
			((RelativeLayout) findViewById(R.id.rlVideoInfoTabSelector))
					.setBackgroundColor(0x00000000);

			btnEpisodeTab.setTextColor(0xff000000);
			((RelativeLayout) findViewById(R.id.rlEpisodeSelector))
					.setBackgroundColor(0x00000000);

			btnRelativeVideoTab.setTextColor(getResources().getColor(
					R.color.blue));
			((RelativeLayout) findViewById(R.id.rlRelativeVideoSelector))
					.setBackgroundColor(getResources().getColor(R.color.blue));

			((LinearLayout) findViewById(R.id.layoutVideoInfo))
					.setVisibility(View.GONE);
			((LinearLayout) findViewById(R.id.layoutEpisode))
					.setVisibility(View.GONE);
			((LinearLayout) findViewById(R.id.layoutRelativeVideo))
					.setVisibility(View.VISIBLE);
			break;
		}

	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		try {
			switch (mCurrentTab) {
			case TAB_EPISODE_VIDEOS:
				playEpisode(pos);
				break;
			case TAB_RELATIVE_VIDEOS:
				mVideoItem.setUniqueId(mVideoItem.getRelated().get(pos)
						.getUniqueId());
				getVideoFullInfo();
				break;
			}

		} catch (Exception e) {
			if (BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		}

	}

	private void playEpisode(int pos) {
		Episode episode = mVideoItem.getEpisodes().get(pos);
		
		// Copy info of video playing
		String yt_id = mVideoItem.getYoutubeId();
		String yt_thumb = mVideoItem.getYoutubeThumb();
		int ep_id = mVideoItem.getEpisodeId();
		
		// Fill info of episode want to play to mVideoItem
		mVideoItem.setYoutubeId(episode.getYoutubeId());
		mVideoItem.setYoutubeThumb(episode.getYoutubeThumb());
		mVideoItem.setEpisodeId(episode.getEpisodeId());
		if (mVideoItem.getEpisodes().size() > 0) {
			((TextView) findViewById(R.id.tvEpisode)).setText(": " + mVideoItem.getEpisodeId() + "/" + (mVideoItem.getEpisodes().size() + 1));
		} else {
			((LinearLayout) findViewById(R.id.layoutEpisodeIndex)).setVisibility(View.GONE);
		}
		
		// Play the episode
		mVideoPlayerFragment = new OpenYouTubePlayerSupportFragment();
		mVideoPlayerFragment.setOnRetrieveVideoInfoListener(this);
		mVideoPlayerFragment.setOnMediaPlayerCompletionListener(this);
		mVideoPlayerFragment.setFullScreenController(this);
		mVideoPlayerFragment.setVideoIdUri(Uri.parse("ytv://"
				+ mVideoItem.getYoutubeId()));
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.videoPlayerPlace, mVideoPlayerFragment).commit();
		
		// Put info of lasted video into episode list
		episode.setYoutubeId(yt_id);
		episode.setYoutubeThumb(yt_thumb);
		episode.setEpisodeId(ep_id);
		mVideoItem.getEpisodes().set(pos, episode);
		
		// Fill data to Episode Video Tab
		mVideoItem.sortEpisodeIncByEpisodeId();
		EpisodeListViewFragment fragmentEpisodeVideo = new EpisodeListViewFragment(mVideoItem);
		getSupportFragmentManager().beginTransaction().replace(R.id.flEpisode, fragmentEpisodeVideo).commit();
		fragmentEpisodeVideo.setOnVideoItemClicked(this);
		
	}
	
	private boolean isEndOfEplisode = false;
	
	private void playNextEpisode() {
		// Find the next episode
		int i = 0;
		while (i < mVideoItem.getEpisodes().size()) {
			int k = mVideoItem.getEpisodes().get(i).getEpisodeId(); 
			if (k > mVideoItem.getEpisodeId()) {
				playEpisode(i);
				break;
			} else {
				i++;
			}
		}
		
		if (i >= mVideoItem.getEpisodes().size()) {
			isEndOfEplisode = true;
		}
	}
	
	

	private void doLayout() {

		LinearLayout.LayoutParams layoutVideoParams = (LinearLayout.LayoutParams) mLayoutVideo
				.getLayoutParams();
		// When in full-screen, the visibility of all other views than the
		// player should be set to
		// GONE and the player should be laid out across the whole screen.
		// This layout is up to you - this is just a simple example
		// (vertically stacked boxes in
		// portrait, horizontally stacked in landscape).
		if (mIsFullScreen) {
			layoutVideoParams.width = LayoutParams.MATCH_PARENT;
			layoutVideoParams.height = LayoutParams.MATCH_PARENT;
			// requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
			getSupportActionBar().hide();
			// getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.anim.bg_transparent_gradient_top));
			mOtherViews.setVisibility(View.GONE);
			mLayoutVideo.setLayoutParams(layoutVideoParams);

			// Request full-screen feature
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			// Back-to non full-screen feature
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			// requestWindowFeature(Window.FEATURE_ACTION_BAR);
			getSupportActionBar().show();
			// getSupportActionBar().setBackgroundDrawable(new
			// ColorDrawable(getResources().getColor(R.color.blue)));
			mOtherViews.setVisibility(View.VISIBLE);
			mLayoutVideo.setLayoutParams(new LinearLayout.LayoutParams(
					SCREEN_WIDTH, SCREEN_HEIGHT * 460 / 1280));
		}

	}

	private OnMenuItemClickListener onShareListener = new OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Share handle
			Toast.makeText(FullVideoInfoActivity.this,
					"Chia sẻ video qua Facebook ^_^", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
	};

	private OnMenuItemClickListener onAddToFavoriteListener = new OnMenuItemClickListener() {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			// TODO Add video to favorite handle
			Toast.makeText(FullVideoInfoActivity.this,
					"Thêm video vô yêu thích ^_^", Toast.LENGTH_SHORT).show();
			return false;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem shareMenuItem = menu.add(getString(R.string.action_share));
		shareMenuItem.setIcon(R.drawable.ic_action_share);
		shareMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
				| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		shareMenuItem.setOnMenuItemClickListener(onShareListener);

		MenuItem addToFavoriteMenuItem = menu
				.add(getString(R.string.action_favorite));
		addToFavoriteMenuItem.setIcon(R.drawable.ic_action_favorite);
		addToFavoriteMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
				| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		addToFavoriteMenuItem
				.setOnMenuItemClickListener(onAddToFavoriteListener);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onToggleFullScreen() {

		mRotateMatchedOrientation = false;

		if (mIsFullScreen) {
			mObligeScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			mObligeScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}

	}

	@Override
	public boolean isFullScreen() {
		// TODO Auto-generated method stub
		return mIsFullScreen;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mIsFullScreen = false;
			doLayout();
			mVideoPlayerFragment.updateFullScreenStatus();
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		} else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mIsFullScreen = true;
			doLayout();
			mVideoPlayerFragment.updateFullScreenStatus();
		}
	}

	@Override
	public void onRetrieveVideoLength(String videoLength) {
		try {
		((TextView) findViewById(R.id.tvVideoLength)).setText(": " + videoLength);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onMediaPlayerPrepareCompleted(MediaPlayer mp) {
		// Must be set the MediaPlayer before start service
		PhatAmPlayBackService.setMediaPlayer(mp);
		
		// Service Initial
		final Intent serviceIntent = new Intent(getApplicationContext(), PhatAmPlayBackService.class);
		serviceIntent.putExtra(PhatAmPlayBackService.EXTRA_VIDEO_TITLE, mVideoItem.getVideoTitle());
		serviceIntent.putExtra(PhatAmPlayBackService.EXTRA_VIDEO_ARTIST, mVideoItem.getVideoArtist());
		startService(serviceIntent);
		
	}


	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		if (mVideoItem.getEpisodes() != null) {
			if (mVideoItem.getEpisodes().size() > 0 && isEndOfEplisode == false) {				
				playNextEpisode();						
			}
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


	@Override
	public void onConnectionStatusChange() {
		// TODO Auto-generated method stub
		if (ConnectionUtil.getConnectivityStatus(this) == ConnectionUtil.TYPE_NOT_CONNECTED) {
			this.findViewById(R.id.layoutConnectionError).setVisibility(View.VISIBLE);
			this.findViewById(R.id.layoutConnectionError).startAnimation(AnimationUtils.loadAnimation(this, R.animator.appear));
		} else {
			this.findViewById(R.id.layoutConnectionError).setVisibility(View.GONE);
			this.findViewById(R.id.layoutConnectionError).startAnimation(AnimationUtils.loadAnimation(this, R.animator.disappear));
		}
		
	}
	
}
