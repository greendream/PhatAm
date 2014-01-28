package com.phatam.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.phatam.BuildConfig;
import com.phatam.R;
import com.phatam.entity.Episode;
import com.phatam.entity.Tag;
import com.phatam.entity.VideoItem;
import com.phatam.fragment.EpisodeListViewFragment;
import com.phatam.fragment.PullAndLoadmoreListVideoFragment;
import com.phatam.fragment.RelativeVideoListViewFragment;
import com.phatam.websevice.RestClient;
import com.phatam.youtubeapi.DeveloperKey;

public class FullVideoInfoActivity extends SherlockFragmentActivity implements OnClickListener, OnItemClickListener, OnInitializedListener, OnFullscreenListener {

	// Video Player
	private FrameLayout videoPlayerPlace;
	private YouTubePlayerSupportFragment videoPlayerFragment;
	private YouTubePlayer player;
	private boolean fullscreen;
	private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
		      ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
		      : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
	
	private RelativeLayout layoutVideo;
	private LinearLayout otherViews;
	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;
	
	/*
	 * YouTubeFailureRecoveryActivity.java example stuff
	 *
	 */
	
	private static final int RECOVERY_DIALOG_REQUEST = 1;
	

	// Tab manager	
	public static final int TAB_INFO_VIDEOS = 0;
	public static final int TAB_EPISODE_VIDEOS = 1;
	public static final int TAB_RELATIVE_VIDEOS = 2;	
	int mCurrentTab = TAB_INFO_VIDEOS;
	Button btnVideoInfoTab;
	Button btnEpisodeTab;
	Button btnRelativeVideoTab;
	
	// UI controler
	ImageView ivVideoPreviewImage;	
	ListView mListViewEpisode;
	
	// Adapter for relative and Episode video
	ArrayAdapter<String> mEpisodeAdapter;
	ArrayList<String> mListEpisodeData = new ArrayList<String>();
	
	VideoItem mVideoItem; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Chage Actionbar background color
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
				
		// Get full video info
		String ShowVideoFrom = getIntent().getExtras().getString("ShowFrom");
		if ("LoadmoreListVideoFragment".equals(ShowVideoFrom)) {
			mVideoItem = PullAndLoadmoreListVideoFragment.selectedVideoItem;
		}
		setContentView(R.layout.activity_full_video_info);

		((LinearLayout) findViewById(R.id.layoutVideoInfo)).setVisibility(View.GONE);
		((LinearLayout) findViewById(R.id.layoutPreviewInfo)).setVisibility(View.GONE);
		
		layoutVideo = (RelativeLayout) findViewById(R.id.layoutVideo);
		otherViews = (LinearLayout) findViewById(R.id.otherViews);
		
		// Setting size for video preview area
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		final int h = displayMetrics.heightPixels;
		final int w = displayMetrics.widthPixels;
		SCREEN_WIDTH = (w < h) ? w : h;
		SCREEN_HEIGHT = (h > w) ? h : w;
		layoutVideo.setLayoutParams(new LinearLayout.LayoutParams(SCREEN_WIDTH, SCREEN_HEIGHT * 460 / 1280));
		
		// Prepare for image loader
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.phat)
		.showImageOnFail(R.drawable.phat)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true).build();
		
		// Prepare for Video player
		videoPlayerPlace = (FrameLayout) findViewById(R.id.videoPlayerPlace);
		
		doLayout();
		
		getVideoFullInfo();
	}
	
	
	private void fillDataToComponent() {
		doLayout();
		
		// Fill data about video info to UI
		btnVideoInfoTab = (Button) findViewById(R.id.btnVideoInfoTab);
		btnVideoInfoTab.setOnClickListener(this);
		
		btnEpisodeTab = (Button) findViewById(R.id.btnEpisodeTab);
		btnEpisodeTab.setOnClickListener(this);
		
		btnRelativeVideoTab = (Button) findViewById(R.id.btnRelativeVideoTab);
		btnRelativeVideoTab.setOnClickListener(this);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.full_video_title);

		// Fill data to preview
		ivVideoPreviewImage = (ImageView) findViewById(R.id.ivVideoPreviewImage);
		imageLoader.displayImage(mVideoItem.getYoutubeImage(), ivVideoPreviewImage, options, animateFirstListener);
		((TextView) findViewById(R.id.tvPreviewVideoTitle)).setText(mVideoItem.getVideoTitle());
		((TextView) findViewById(R.id.tvPreviewVideoAuthor)).setText(mVideoItem.getArtist());
		((TextView) findViewById(R.id.tvPreviewVideoView)).setText(mVideoItem.getYoutubeView() + " lượt xem");
		
		// Fill data to Video-info Tab
		((TextView) findViewById(R.id.tvVideoTitle)).setText(": " + mVideoItem.getVideoTitle());
		((TextView) findViewById(R.id.tvAuthor)).setText(": " + mVideoItem.getArtist());
		((TextView) findViewById(R.id.tvCategoryName)).setText(": " + getCategoryName(mVideoItem.getCategory()));
		((TextView) findViewById(R.id.tvYoutubeView)).setText(": " + mVideoItem.getYoutubeView() + "");
		
		if ("".equals(mVideoItem.getDescription())) {
			((TextView) findViewById(R.id.tvDescription)).setText("...");			
		} else {
			((TextView) findViewById(R.id.tvDescription)).setText(mVideoItem.getDescription());	
		}
		
		// Fill data to Episode Video Tab
		mVideoItem.sortEpisodeIncByEpisodeId();
		EpisodeListViewFragment fragmentEpisodeVideo = new EpisodeListViewFragment(mVideoItem);
		getSupportFragmentManager().beginTransaction().replace(R.id.flEpisode, fragmentEpisodeVideo).commit();
		
		// Fill data to Relative Video Tab
		RelativeVideoListViewFragment fragmentRelativeVideo = new RelativeVideoListViewFragment(mVideoItem);
		getSupportFragmentManager().beginTransaction().replace(R.id.flRelativeVideo, fragmentRelativeVideo).commit();
		fragmentRelativeVideo.setOnVideoItemClicked(this);
		
		// Prepare for video player
		videoPlayerFragment = new YouTubePlayerSupportFragment();
		videoPlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.videoPlayerPlace, videoPlayerFragment).commit();
		if (player != null) {
			player.setPlayerStyle(PlayerStyle.MINIMAL);
		}
		
		// Show component when fill data completed
		if (mCurrentTab == TAB_INFO_VIDEOS) {
			((LinearLayout) findViewById(R.id.layoutVideoInfo)).setVisibility(View.VISIBLE);			
		}
		((LinearLayout) findViewById(R.id.layoutPreviewInfo)).setVisibility(View.VISIBLE);
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
		
		String[] categoryIds = getResources().getStringArray(R.array.arr_cartegory_id);
		String[] categoryNames = getResources().getStringArray(R.array.arr_cartegory_name);
		
		for (int i = 0; i < categoryIds.length; i++) {
			if (categoryIds[i].equals(categoryId)) {
				return categoryNames[i];
			}
		}
		
		return "";
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
	
	private OnItemClickListener OnEpisodeItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			Log.v("Click item Episode", "" + position);
			
			Intent i = new Intent(getApplicationContext(), VideoViewActivity.class);
//			i.putExtra("link", mListEpisodeData.get(position).toString());
			startActivity(i);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.btnVideoInfoTab:
			mCurrentTab = TAB_INFO_VIDEOS;
			btnVideoInfoTab.setTextColor(getResources().getColor(R.color.blue));
			((RelativeLayout) findViewById(R.id.rlVideoInfoTabSelector)).setBackgroundColor(getResources().getColor(R.color.blue));
			
			btnEpisodeTab.setTextColor(0xff000000);
			((RelativeLayout) findViewById(R.id.rlEpisodeSelector)).setBackgroundColor(0x00000000);
			
			btnRelativeVideoTab.setTextColor(0xff000000);
			((RelativeLayout) findViewById(R.id.rlRelativeVideoSelector)).setBackgroundColor(0x00000000);
			
			((LinearLayout) findViewById(R.id.layoutVideoInfo)).setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.layoutEpisode)).setVisibility(View.GONE);
			((LinearLayout) findViewById(R.id.layoutRelativeVideo)).setVisibility(View.GONE);
			break;
			
		case R.id.btnEpisodeTab:
			mCurrentTab = TAB_EPISODE_VIDEOS;
			btnVideoInfoTab.setTextColor(0xff000000);
			((RelativeLayout) findViewById(R.id.rlVideoInfoTabSelector)).setBackgroundColor(0x00000000);
			
			btnEpisodeTab.setTextColor(getResources().getColor(R.color.blue));
			((RelativeLayout) findViewById(R.id.rlEpisodeSelector)).setBackgroundColor(getResources().getColor(R.color.blue));
			
			btnRelativeVideoTab.setTextColor(0xff000000);
			((RelativeLayout) findViewById(R.id.rlRelativeVideoSelector)).setBackgroundColor(0x00000000);
			
			((LinearLayout) findViewById(R.id.layoutVideoInfo)).setVisibility(View.GONE);
			((LinearLayout) findViewById(R.id.layoutEpisode)).setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.layoutRelativeVideo)).setVisibility(View.GONE);
			break;
			
		case R.id.btnRelativeVideoTab:
			mCurrentTab = TAB_RELATIVE_VIDEOS;
			btnVideoInfoTab.setTextColor(0xff000000);
			((RelativeLayout) findViewById(R.id.rlVideoInfoTabSelector)).setBackgroundColor(0x00000000);
			
			btnEpisodeTab.setTextColor(0xff000000);
			((RelativeLayout) findViewById(R.id.rlEpisodeSelector)).setBackgroundColor(0x00000000);
			
			btnRelativeVideoTab.setTextColor(getResources().getColor(R.color.blue));
			((RelativeLayout) findViewById(R.id.rlRelativeVideoSelector)).setBackgroundColor(getResources().getColor(R.color.blue));
			
			((LinearLayout) findViewById(R.id.layoutVideoInfo)).setVisibility(View.GONE);
			((LinearLayout) findViewById(R.id.layoutEpisode)).setVisibility(View.GONE);
			((LinearLayout) findViewById(R.id.layoutRelativeVideo)).setVisibility(View.VISIBLE);
			break;
		}
		
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		try {
			mVideoItem = mVideoItem.getRelated().get(arg2);
			ivVideoPreviewImage.setBackgroundResource(R.drawable.phat);
			getVideoFullInfo();
		} catch (Exception e) {
			if (BuildConfig.DEBUG) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public void getVideoFullInfo() {
		((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
		
		final ArrayList<Episode> episodes = new ArrayList<Episode>();
		final ArrayList<VideoItem> related = new ArrayList<VideoItem>();
		final ArrayList<Tag> tags = new ArrayList<Tag>();
		String url = "video/" + mVideoItem.getUniqueId();
		
		// Run GET task to get full info of info
		RestClient.GET(url, null, new JsonHttpResponseHandler() {
			
			
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
				((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.GONE);
			}

			@Override
			public void onSuccess(JSONObject result) {
				Log.i("GET FULL VIDEO INFO COMPLETED", result.toString());
				
				JSONArray jArray;
				// Get Episode
				try {
					jArray = result.getJSONArray("tags");
					jArray = result.getJSONArray("episode");
					Log.v("array full video received", "" + jArray.toString());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject line_object = jArray.getJSONObject(i);
						String tag = line_object.getString("tag");
						String safe_tag = line_object.getString("safe_tag");
						Tag item = new Tag(tag, safe_tag);
						tags.add(item);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					jArray = result.getJSONArray("episode");
					Log.v("array received", "" + jArray.toString());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject line_object = jArray.getJSONObject(i);
						String uniq_id = line_object.getString("uniq_id");
						String yt_id = line_object.getString("yt_id");
						String yt_thumb = line_object.getString("yt_thumb");
						String episode = line_object.getString("episode");
						int episode_id = line_object.getInt("episode_id");
						Episode item = new Episode(uniq_id, episode_id, yt_id, yt_thumb, episode);
						episodes.add(item);
					}
					// Get related
					jArray = result.getJSONArray("related");
					Log.v("array received", "" + jArray.toString());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject line_object = jArray.getJSONObject(i);
						int id = line_object.getInt("id");
						String uniq_id = line_object.getString("uniq_id");
						String artist = line_object.getString("artist");
						String video_title = line_object
								.getString("video_title");
						String description = line_object
								.getString("description");
						String yt_thumb = line_object.getString("yt_thumb");
						int site_views = line_object.getInt("site_views");
						String mp3 = line_object.getString("audio");
						String yt_id = line_object.getString("yt_id");
						String category = line_object.getString("category");						 
						VideoItem item = new VideoItem(uniq_id, artist,
								video_title, description, yt_id, yt_thumb,
								site_views, category);
						related.add(item);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// set video properties
				mVideoItem.setEpisode(episodes);
				mVideoItem.setRelated(related);
				mVideoItem.setTag(tags);
				
				((ProgressBar)findViewById(R.id.progressBar)).setVisibility(View.GONE);
				fillDataToComponent();
			}
		});
		
	}
	
	
	// Setting for load image preview
	private ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 1000);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub
		
	}

	public YouTubePlayer.Provider getYouTubePlayerProvider() {
		  return this.videoPlayerFragment;
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider,
			YouTubePlayer player, boolean wasRestored) {
		this.player = player;
		
		// Specify that we want to handle fullscreen behavior ourselves.
		player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
		player.setOnFullscreenListener(this);
		if (!wasRestored) {
			player.cueVideo(mVideoItem.getYoutubeId());
		}
		
		doLayout();
	}


	@Override
	public void onFullscreen(boolean isFullscreen) {
		fullscreen = isFullscreen;
		doLayout();
	}
	

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		doLayout();
	}
	
	private void doLayout() {
		LinearLayout.LayoutParams layoutVideoParams = (LinearLayout.LayoutParams) layoutVideo.getLayoutParams();
		// When in fullscreen, the visibility of all other views than the
		// player should be set to
		// GONE and the player should be laid out across the whole screen.			
		// This layout is up to you - this is just a simple example
		// (vertically stacked boxes in
		// portrait, horizontally stacked in landscape).
		if (fullscreen || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			layoutVideoParams.width = LayoutParams.MATCH_PARENT;
			layoutVideoParams.height = LayoutParams.MATCH_PARENT;
			getSupportActionBar().hide();
			((RelativeLayout) findViewById(R.id.layoutPlayerMode)).setVisibility(View.GONE);
			((LinearLayout) findViewById(R.id.layoutPreviewInfo)).setVisibility(View.GONE);
			otherViews.setVisibility(View.GONE);
			if (player != null) {
				player.setPlayerStyle(PlayerStyle.DEFAULT);
			}
		} else {
			getSupportActionBar().show();
			((RelativeLayout) findViewById(R.id.layoutPlayerMode)).setVisibility(View.VISIBLE);
			((LinearLayout) findViewById(R.id.layoutPreviewInfo)).setVisibility(View.VISIBLE);
			otherViews.setVisibility(View.VISIBLE);
			layoutVideo.setLayoutParams(new LinearLayout.LayoutParams(SCREEN_WIDTH, SCREEN_HEIGHT * 460 / 1280));
			if (player != null) {
				player.setPlayerStyle(PlayerStyle.MINIMAL);
			}
		}
		
	}
	
}
