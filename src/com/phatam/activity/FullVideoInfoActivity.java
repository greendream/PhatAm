package com.phatam.activity;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.phatam.R;
import com.phatam.config.GlobalData;
import com.phatam.entity.VideoItem;
import com.phatam.fragment.PullAndLoadmoreListVideoFragment;
import com.phatam.websevice.RestClientAPI;

public class FullVideoInfoActivity extends SherlockActivity {

	ImageView mImageViewBigImage;
	ListView mListViewEpisode;
	ArrayAdapter<String> mEpisodeAdapter;
	ArrayList<String> mListEpisodeData = new ArrayList<String>();
	TextView mTextViewVideoTitle;
	TextView mTextViewVideoArtist;
	
	VideoItem mVideoItem; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get full video info
		String ShowVideoFrom = getIntent().getExtras().getString("ShowFrom");
		if ("LoadmoreListVideoFragment".equals(ShowVideoFrom)) {
			mVideoItem = PullAndLoadmoreListVideoFragment.selectedVideoItem;
		}
		RestClientAPI.getVideoFullInfo(mVideoItem);
		
		// Fill data about video info to UI
		setContentView(R.layout.activity_video_info);
		mImageViewBigImage = (ImageView) findViewById(R.id.videoinfo_image);
		GlobalData.imageLoader.displayImage(mVideoItem.getYoutubeImage(), mImageViewBigImage);
		
		mTextViewVideoTitle = (TextView) findViewById(R.id.videoinfo_video_name);
		mTextViewVideoTitle.setText(mVideoItem.getVideoTitle());
		
		mTextViewVideoArtist = (TextView) findViewById(R.id.videoinfo_video_author);
		mTextViewVideoArtist.setText(mVideoItem.getArtist());
		
		mListViewEpisode = (ListView) findViewById(R.id.videoinfo_list_video);
		mListEpisodeData.add("Phần 1");
		mListEpisodeData.add("Phần 2");
		mListEpisodeData.add("Phần 3");
		mListEpisodeData.add("Phần 4");
		mEpisodeAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_row_simple, mListEpisodeData);		
		mListViewEpisode.setAdapter(mEpisodeAdapter);
		mListViewEpisode.setBackgroundColor(getResources().getColor(R.color.list_row_green));
		mListViewEpisode.setOnItemClickListener(OnEpisodeItemClick);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.full_video_title);
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

}
