package com.phatam.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.phatam.R;
import com.phatam.adapter.EpisodeAdapter;
import com.phatam.entity.VideoItem;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("ValidFragment")
public class EpisodeListViewFragment extends SherlockFragment {
	private VideoItem mVideoItem;
	private ListView mEpisodeListView;
	private View mContentView;
	private EpisodeAdapter mEpisodeAdapter;

	// Event handler
	private OnItemClickListener OnVideoListItemClick;
	
	/**
	 * Disable StricMode
	 */
	static {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}
	
	public EpisodeListViewFragment() {
	}

	public EpisodeListViewFragment(VideoItem videoItem) {
		mVideoItem = videoItem;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_episodes, container, false);
		mEpisodeListView = (ListView) mContentView.findViewById(R.id.episode_list_view);
		mEpisodeAdapter = new EpisodeAdapter(getSherlockActivity(), mVideoItem);
		mEpisodeListView.setAdapter(mEpisodeAdapter);
		mEpisodeListView.setOnItemClickListener(OnVideoListItemClick);

		return mContentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Get CONCURRENT_PAGE_AVAILABLE page and put to VideoAdapter

	}

	public void setOnVideoItemClicked(OnItemClickListener onItemClick) {
		OnVideoListItemClick = onItemClick;
	}
	
//	OnVideoListItemClick = new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//				long arg3) {
//			selectedVideoItem = mArrayListVideoItem.get(position - 1);
//
//			Intent i = new Intent(getSherlockActivity(),
//					FullVideoInfoActivity.class);
//			i.putExtra("ShowFrom", "LoadmoreListVideoFragment");
//			startActivity(i);
//		}
//	};

}
