package com.phatam.fragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.phatam.BuildConfig;
import com.phatam.R;
import com.phatam.activity.FullVideoInfoActivity;
import com.phatam.activity.PagerListVideoActivity;
import com.phatam.adapter.ListVideoAdapter;
import com.phatam.entity.VideoItem;
import com.phatam.websevice.RestClient;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("ValidFragment")
public class PullAndLoadmoreListVideoFragment extends SherlockFragment {
	public static VideoItem selectedVideoItem;

	public static final String ORDER_BY_RATING = "rating";
	public static final String ORDER_BY_ADDED = "added";
	public static final String ORDER_BY_VIDEO_TITLE = "title";

	// About load content
	private static final int PAGE_SIZE = 15;
	private static final int CONCURRENT_PAGE_AVAILABLE = 2;
	private int mStartPageIndex = 0;
	private int mEndPageIndex = mStartPageIndex + CONCURRENT_PAGE_AVAILABLE - 1;

	private ArrayList<VideoItem> mArrayListVideoItem;
	private PullAndLoadListView mPullAndLoadListView;
	private View mContentView;
	private ListVideoAdapter mListVideoAdapter;
	private String mUrl;
	private String mOrderByType;

	/**
	 * Disable StricMode
	 */
	static {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}
	
	public PullAndLoadmoreListVideoFragment() {
		this.mUrl = PagerListVideoActivity.TYPE_TOP_VIDEOS;
		this.mOrderByType = ORDER_BY_ADDED;
	}

	public PullAndLoadmoreListVideoFragment(String videoUrl,
			String order_by_type) {
		this.mUrl = videoUrl;
		this.mOrderByType = order_by_type;

		if (BuildConfig.DEBUG) {
			Log.v("type", "" + videoUrl);
			Log.v("page_title", "" + order_by_type);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_pull_and_load_more_list_video, container, false);
		mPullAndLoadListView = (PullAndLoadListView) mContentView.findViewById(R.id.pull_and_load_more_list_view);

		mArrayListVideoItem = new ArrayList<VideoItem>();
		mListVideoAdapter = new ListVideoAdapter(getSherlockActivity(), mArrayListVideoItem);
		mPullAndLoadListView.setAdapter(mListVideoAdapter);
		mPullAndLoadListView.setOnItemClickListener(OnVideoListItemClick);
		
		// Set a listener to be invoked when the list should be refreshed.
		mPullAndLoadListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// In the AsyncTask we will be do 2 step below:				
				// Step 1. Get PAGE_SIZE video of the page before mStartPageIndex to
				// Step 2. Delete PAGE_SIZE last video in VideoAdapter
				// VideoAdapter
				
				Log.i("REFRESH", "PULL TO REFRESH");
				new PullToRefreshDataTask().execute();
			}
		});

		// set a listener to be invoked when the list reaches the end
		mPullAndLoadListView.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				// In the AsyncTask we will be do 2 step below:
				// Step 1. Get PAGE_SIZE video of the page after mEndPageIndex to
				// Step 2. Delete PAGE_SIZE first video in VideoAdapter
				// VideoAdapter
				Log.i("LOADMORE", "PULL TO LOADMORE");
				new LoadMoreDataTask().execute();
			}
		});

		return mContentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Get CONCURRENT_PAGE_AVAILABLE page and put to VideoAdapter
		 new GetFirstVideoListTask().execute();

	}

	/**
	 * Get video list from server and create new mListVideoItem for adapter to
	 * show the list view to UI
	 * 
	 * @param url
	 * @param orderbyType
	 * @param pageIndex
	 *            : page index in result list
	 */
	private ArrayList<VideoItem> getVideoInPage(String url, String orderbyType,
			int pageIndex) {
		ArrayList<VideoItem> mVideoItems = new ArrayList<VideoItem>();

		/**
		 * If url is linked to "category" or "artist" it will be removed when the same API for
		 * other url was completed
		 */
		if (url.indexOf("gory/") > 0) {
			url = url + "/" + orderbyType + "/" + (pageIndex * PAGE_SIZE);
		} else if (url.indexOf("tist/") > 0) {
			url = url + "/added/" + (pageIndex * PAGE_SIZE);
		}

		url = RestClient.BASE_URL + url;
		
		Log.i("START RUN GET JSON TASK URL", url);
		String response = "";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

		try {
			HttpResponse execute = client.execute(httpGet);
			InputStream content = execute.getEntity().getContent();

			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					content));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
			}

			JSONObject result = new JSONObject(response);
			JSONArray jArray;
			jArray = result.getJSONArray("videos");
			
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject line_object = jArray.getJSONObject(i);
				String uniq_id = line_object.getString("uniq_id");
				String artist = line_object.getString("artist");
				String video_title = line_object.getString("video_title");
				String description = line_object.getString("description");
				String yt_thumb = line_object.getString("yt_thumb");
				int site_views = line_object.getInt("site_views");
				String yt_id = line_object.getString("yt_id");
				String category = line_object.getString("category");
				VideoItem item = new VideoItem(uniq_id, artist, video_title,
						description, yt_id, yt_thumb, site_views, category);

				mVideoItems.add(item);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mVideoItems;
	}

	private class GetFirstVideoListTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

			for (int i = mStartPageIndex; i <= mEndPageIndex; i++) {
				ArrayList<VideoItem> videoPage = getVideoInPage(mUrl, mOrderByType, i);
				mArrayListVideoItem.addAll(videoPage);
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mListVideoAdapter.notifyDataSetChanged();
			mPullAndLoadListView.onLoadMoreComplete();
			mPullAndLoadListView.onRefreshComplete();
			mPullAndLoadListView.onRefresh();
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			mListVideoAdapter.notifyDataSetChanged();
			mPullAndLoadListView.onLoadMoreComplete();
			mPullAndLoadListView.onRefreshComplete();
		}
	}
	
	private class PullToRefreshDataTask extends AsyncTask<Void, Void, Void> {
		ArrayList<VideoItem> arrPullToRefreshVideoPage;
		
		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

			// Step 1. Get PAGE_SIZE video of the page before mStartPageIndex to
			// VideoAdapter
			if (mStartPageIndex > 0) {
				mStartPageIndex--;
				mEndPageIndex--;
			}
			arrPullToRefreshVideoPage = getVideoInPage(mUrl, mOrderByType, mStartPageIndex);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (arrPullToRefreshVideoPage.size() == 0) {
				// resume change
				mStartPageIndex = 0;
				mEndPageIndex = mStartPageIndex + CONCURRENT_PAGE_AVAILABLE - 1;
			} else {
				// Step 2. Delete PAGE_SIZE last video in VideoAdapter
				for (int i = 0; i < PAGE_SIZE; i++) {
					if (mArrayListVideoItem.size() - 1 >= 0) {
						mArrayListVideoItem.remove(mArrayListVideoItem.size() - 1);
					}
				}				
				mArrayListVideoItem.addAll(0, arrPullToRefreshVideoPage);
			}
			// We need notify the adapter that the data have been changed
			mListVideoAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the Refresh task, has finished
			mPullAndLoadListView.onRefreshComplete();

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			mPullAndLoadListView.onRefreshComplete();
		}
	}

	private class LoadMoreDataTask extends AsyncTask<Void, Void, Void> {
		ArrayList<VideoItem> arrLoadmoreVideoPage;
		
		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

			// Step 1. Get PAGE_SIZE video of the page after mEndPageIndex to
			// VideoAdapter
			mStartPageIndex++;
			mEndPageIndex++;
			arrLoadmoreVideoPage = getVideoInPage(mUrl, mOrderByType, mEndPageIndex);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (arrLoadmoreVideoPage.size() == 0) {
				// resume change
				mStartPageIndex--;
				mEndPageIndex--;
			} else {
				// Step 2. Delete PAGE_SIZE first video in VideoAdapter
				for (int i = 0; i < PAGE_SIZE; i++) {
					if (mArrayListVideoItem.size() > 0) {
						mArrayListVideoItem.remove(0);
					}
				}
				mArrayListVideoItem.addAll(arrLoadmoreVideoPage);
			}

			// We need notify the adapter that the data have been changed
			mListVideoAdapter.notifyDataSetChanged();

			// Call onLoadMoreComplete when the LoadMore task, has finished
			mPullAndLoadListView.setSelection(mArrayListVideoItem.size() - PAGE_SIZE);

			mPullAndLoadListView.onLoadMoreComplete();

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			mPullAndLoadListView.onLoadMoreComplete();
		}
	}

	private OnItemClickListener OnVideoListItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			selectedVideoItem = mArrayListVideoItem.get(position - 1);

			Intent i = new Intent(getSherlockActivity(),
					FullVideoInfoActivity.class);
			i.putExtra("ShowFrom", "LoadmoreListVideoFragment");
			startActivity(i);
		}
	};

}
