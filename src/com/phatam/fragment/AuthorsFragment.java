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
import android.content.Intent;
import android.os.AsyncTask;
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
import com.phatam.R;
import com.phatam.activity.PagerListVideoActivity;
import com.phatam.adapter.AuthorAdapter;
import com.phatam.entity.VideoItem;
import com.phatam.model.AuthorModel;
import com.phatam.util.AppStatus;
import com.phatam.websevice.RestClient;

@SuppressLint("NewApi")
public class AuthorsFragment extends SherlockFragment {
	// Sort type
	private static final String SORT_BY_ARTIST_NAME = "artist/artist/";
	private static final String SORT_BY_VIDEO_QUANTITY = "artist/cnt/";
	
	// Pager component
	private static final int PAGE_SIZE = 20;
	private static final int CONCURRENT_PAGE_AVAILABLE = 2;
	private int mStartPageIndex = 0;
	private int mEndPageIndex = mStartPageIndex + CONCURRENT_PAGE_AVAILABLE - 1;

	// ListView component
	public ArrayList<AuthorModel> mArrayListAuthorItem = new ArrayList<AuthorModel>();
	AuthorAdapter mAuthorAdapter;
	PullAndLoadListView mPullAndLoadListViewAuthor;
	private String mUrl;
	
	/**
	 * Disable StricMode
	 */
	static {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}
	
	public AuthorsFragment() {
		// Default sort by name
		this.mUrl = SORT_BY_ARTIST_NAME;
	}
	
	public void changeSortType(String sort_type) {
		this.mUrl = sort_type;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_author_list, container, false);
		
		mPullAndLoadListViewAuthor = (PullAndLoadListView) rootView.findViewById(R.id.list_view_author);
		mPullAndLoadListViewAuthor.setOnItemClickListener(OnAuthorListItemClick);
		mArrayListAuthorItem = new ArrayList<AuthorModel>();
		mAuthorAdapter = new AuthorAdapter(getSherlockActivity(), mArrayListAuthorItem);
		mPullAndLoadListViewAuthor.setAdapter(mAuthorAdapter);
		
		// Set a listener to be invoked when the list should be refreshed.
		mPullAndLoadListViewAuthor.setOnRefreshListener(new OnRefreshListener() {

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
		mPullAndLoadListViewAuthor.setOnLoadMoreListener(new OnLoadMoreListener() {
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

		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Get CONCURRENT_PAGE_AVAILABLE page and put to AuthorAdapter
		 new GetFirstPageAuthorListTask().execute();

	}
	
	private class GetFirstPageAuthorListTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

			for (int i = mStartPageIndex; i <= mEndPageIndex; i++) {
				ArrayList<AuthorModel> authorPage = getAuthorInPage(mUrl, i);
				mArrayListAuthorItem.addAll(authorPage);
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mAuthorAdapter.notifyDataSetChanged();
			mPullAndLoadListViewAuthor.onRefreshComplete();
			mPullAndLoadListViewAuthor.onRefresh();
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			mAuthorAdapter.notifyDataSetChanged();
			mPullAndLoadListViewAuthor.onRefreshComplete();
		}
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
	private ArrayList<AuthorModel> getAuthorInPage(String url, int pageIndex) {
		ArrayList<AuthorModel> mAuthorItems = new ArrayList<AuthorModel>();

		url = RestClient.BASE_URL + url + (pageIndex * PAGE_SIZE);
		
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
				String strArtist = line_object.getString("artist");
				AuthorModel item = new AuthorModel(strArtist);

				mAuthorItems.add(item);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mAuthorItems;
	}

	private class PullToRefreshDataTask extends AsyncTask<Void, Void, Void> {
		ArrayList<AuthorModel> arrPullToRefreshAuthorPage;
		
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
			arrPullToRefreshAuthorPage = getAuthorInPage(mUrl, mStartPageIndex);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (arrPullToRefreshAuthorPage.size() == 0) {
				// resume change
				mStartPageIndex = 0;
				mEndPageIndex = mStartPageIndex + CONCURRENT_PAGE_AVAILABLE - 1;
			} else {
				// Step 2. Delete PAGE_SIZE last video in VideoAdapter
				for (int i = 0; i < PAGE_SIZE; i++) {
					if (mArrayListAuthorItem.size() - 1 >= 0) {
						mArrayListAuthorItem.remove(mArrayListAuthorItem.size() - 1);
					}
				}				
				mArrayListAuthorItem.addAll(0, arrPullToRefreshAuthorPage);
			}
			// We need notify the adapter that the data have been changed
			mAuthorAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the Refresh task, has finished
			mPullAndLoadListViewAuthor.onRefreshComplete();

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			mPullAndLoadListViewAuthor.onRefreshComplete();
		}
	}

	private class LoadMoreDataTask extends AsyncTask<Void, Void, Void> {
		ArrayList<AuthorModel> arrLoadmoreAuthorPage;
		
		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

			// Step 1. Get PAGE_SIZE video of the page after mEndPageIndex to
			// VideoAdapter
			mStartPageIndex++;
			mEndPageIndex++;
			arrLoadmoreAuthorPage = getAuthorInPage(mUrl, mEndPageIndex);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (arrLoadmoreAuthorPage.size() == 0) {
				// resume change
				mStartPageIndex--;
				mEndPageIndex--;
			} else {
				// Step 2. Delete PAGE_SIZE first video in VideoAdapter
				for (int i = 0; i < PAGE_SIZE; i++) {
					if (mArrayListAuthorItem.size() > 0) {
						mArrayListAuthorItem.remove(0);
					}
				}
				mArrayListAuthorItem.addAll(arrLoadmoreAuthorPage);
			}

			// We need notify the adapter that the data have been changed
			mAuthorAdapter.notifyDataSetChanged();

			// Call onLoadMoreComplete when the LoadMore task, has finished
			mPullAndLoadListViewAuthor.setSelection(mArrayListAuthorItem.size() - PAGE_SIZE);

			mPullAndLoadListViewAuthor.onLoadMoreComplete();

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			mPullAndLoadListViewAuthor.onLoadMoreComplete();
		}
	}
	
	private OnItemClickListener OnAuthorListItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			boolean isOnline = false;
			if (AppStatus.getInstance(getActivity()).isOnline(getActivity())) {
				isOnline = true;
			}

			Intent i = new Intent(getActivity(), PagerListVideoActivity.class);
			i.putExtra("type", PagerListVideoActivity.TYPE_AUTHOR_VIDEOS);
			i.putExtra("author_name", mArrayListAuthorItem.get(position - 1).getName());
			i.putExtra("isOnline", isOnline);
			getActivity().startActivity(i);
		}
	};
}
