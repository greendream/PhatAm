package com.phatam.activity;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RemoteViews;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.phatam.R;
import com.phatam.custom.VideoViewCustom;
import com.phatam.fragment.PullAndLoadmoreListVideoFragment;

public class VideoViewActivity extends SherlockFragmentActivity {

	VideoViewCustom vv;
	String videoUrl;
	MediaController mc;
	int MeasureWidth = 0;
	int MeasureHeight = 0;
	Point p = new Point();
	static int VIDEO_PAGE = 1;
	Button arrButton[] = new Button[3];
	Button btnMoreVideo;

	private static boolean activityVisible;

	public static boolean isActivityVisible() {
		return activityVisible;
	}

	public static void activityResumed() {
		activityVisible = true;
	}

	public static void activityPaused() {
		activityVisible = false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		VideoViewActivity.activityResumed();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		VideoViewActivity.activityPaused();
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_view);

		arrButton[0] = (Button) findViewById(R.id.videoview_btn_show_related_video);
		arrButton[1] = (Button) findViewById(R.id.videoview_btn_show_same_author_video);
		arrButton[2] = (Button) findViewById(R.id.videoview_btn_show_hot_video);
		for (int i = 0; i < 3; i++) {
			arrButton[i].setOnClickListener(OnButtonClickListener);
			arrButton[i].setBackgroundColor(getResources().getColor(
					android.R.color.background_light));
			arrButton[i].setTag(i);
		}
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
			arrButton[1].setBackgroundResource(R.drawable.red_gradient);
		else
			arrButton[1].setBackgroundResource(R.drawable.green_gradient);
		
		
		Fragment fragment = new PullAndLoadmoreListVideoFragment(PagerListVideoActivity.TYPE_NEW_VIDEOS, PullAndLoadmoreListVideoFragment.ORDER_BY_RATING);
//		Fragment fragment = new ListVideoSortedFragment("same_author", "rating");
		FragmentManager manager = getSupportFragmentManager();
		manager.beginTransaction().replace(R.id.videoview_fragment, fragment).commit();
		btnMoreVideo = (Button) findViewById(R.id.videoview_btn_show_more_video);
		btnMoreVideo.setOnClickListener(OnClickShowMoreVideo);

		vv = (VideoViewCustom) findViewById(R.id.VideoView);
		mc = new MediaController(VideoViewActivity.this, false);
		vv.setMediaController(mc);
		mc.show();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getWindowManager().getDefaultDisplay().getSize(p);
			MeasureWidth = p.x;
			MeasureHeight = p.y;
		} else {
			Display d = getWindowManager().getDefaultDisplay();
			MeasureWidth = d.getWidth();
			MeasureHeight = d.getHeight();
		}
		int smallWidth = MeasureWidth / 4 * 3;
		if (smallWidth > MeasureHeight / 5 * 2)
			smallWidth = MeasureHeight / 5 * 2;
		vv.setDimensions(smallWidth * 4 / 3, smallWidth);

		YourAsyncTask async = new YourAsyncTask();
		async.execute();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// CustomNotification();
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		Log.v("on configuration change", newConfig.orientation + "");
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

			vv.setDimensions(MeasureHeight, MeasureWidth);
			vv.getHolder().setFixedSize(MeasureHeight, MeasureWidth);

		} else {
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			int smallWidth = MeasureWidth / 4 * 3;
			if (smallWidth > MeasureHeight / 5 * 2)
				smallWidth = MeasureHeight / 5 * 2;
			vv.setDimensions(smallWidth * 4 / 3, smallWidth);
			vv.getHolder().setFixedSize(MeasureWidth, smallWidth);

		}
	}

	// When click buttons to show type of video
	private View.OnClickListener OnButtonClickListener = new View.OnClickListener() {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			for (int i = 0; i < 3; i++)
				arrButton[i].setBackgroundColor(getResources().getColor(
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
				// fragment = new ListVideoSortedFragment("related", "rating");
				break;
			case 1:
				fragment = new PullAndLoadmoreListVideoFragment(PagerListVideoActivity.TYPE_NEW_VIDEOS, PullAndLoadmoreListVideoFragment.ORDER_BY_RATING);
				// fragment = new ListVideoSortedFragment("same_author", "rating");
				break;
			case 2:
				fragment = new PullAndLoadmoreListVideoFragment(PagerListVideoActivity.TYPE_RANDOM_VIDEOS, PullAndLoadmoreListVideoFragment.ORDER_BY_RATING);
				// fragment = new ListVideoSortedFragment("random", "rating");
				break;
			default:
				break;
			}
			FragmentManager childmanager = getSupportFragmentManager();
			childmanager.beginTransaction()
					.replace(R.id.videoview_fragment, fragment).commit();
			Log.v("click button", v.getTag().toString());
		}
	};

	private class YourAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(VideoViewActivity.this, "",
					"Loading Video wait...", true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				String url = "http://www.youtube.com/watch?v=1FJHYqE0RDg";
				videoUrl = getUrlVideoRTSP(url);
				Log.e("Video url for playing=========>>>>>", videoUrl);
			} catch (Exception e) {
				Log.e("Login Soap Calling in Exception", e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			Log.v("prepare to post excuse", "1");
			vv.setVideoURI(Uri.parse(videoUrl));
			vv.setMediaController(mc);
			vv.requestFocus();
			vv.start();
			mc.show();
		}

	}

	public static String getUrlVideoRTSP(String urlYoutube) {
		try {
			String gdy = "http://gdata.youtube.com/feeds/api/videos/";
			DocumentBuilder documentBuilder = DocumentBuilderFactory
					.newInstance().newDocumentBuilder();
			String id = extractYoutubeId(urlYoutube);
			URL url = new URL(gdy + id);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			Document doc = documentBuilder.parse(connection.getInputStream());
			Element el = doc.getDocumentElement();
			NodeList list = el.getElementsByTagName("media:content");// /media:content
			String cursor = urlYoutube;
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node != null) {
					NamedNodeMap nodeMap = node.getAttributes();
					HashMap<String, String> maps = new HashMap<String, String>();
					for (int j = 0; j < nodeMap.getLength(); j++) {
						Attr att = (Attr) nodeMap.item(j);
						maps.put(att.getName(), att.getValue());
					}
					if (maps.containsKey("yt:format")) {
						String f = maps.get("yt:format");
						if (maps.containsKey("url")) {
							cursor = maps.get("url");
						}
						if (f.equals("1"))
							return cursor;
					}
				}
			}
			return cursor;
		} catch (Exception ex) {
			Log.e("Get Url Video RTSP Exception======>>", ex.toString());
		}
		return urlYoutube;

	}

	protected static String extractYoutubeId(String url)
			throws MalformedURLException {
		String id = null;
		try {
			String query = new URL(url).getQuery();
			if (query != null) {
				String[] param = query.split("&");
				for (String row : param) {
					String[] param1 = row.split("=");
					if (param1[0].equals("v")) {
						id = param1[1];
					}
				}
			} else {
				if (url.contains("embed")) {
					id = url.substring(url.lastIndexOf("/") + 1);
				}
			}
		} catch (Exception ex) {
			Log.e("Exception", ex.toString());
		}
		return id;
	}

	public void CustomNotification() {
		// Using RemoteViews to bind custom layouts into Notification
		RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.custom_notification);

		// Open NotificationView Class on Notification Click
		Intent intent = new Intent(this, VideoViewActivity.class);
		// Open NotificationView.java Activity
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this)
		// Set Icon
				.setSmallIcon(R.drawable.cd_disk)
				// Set Ticker Message
				.setTicker(getString(R.string.hello))
				// Dismiss Notification
				.setAutoCancel(true)
				// Set PendingIntent into Notification
				.setContentIntent(null)
				// Set RemoteViews into Notification
				.setContent(remoteViews);

		// Create Notification Manager
		NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Build Notification with Notification Manager
		notificationmanager.notify(0, builder.build());

	}

	private View.OnClickListener on_btn_play_click = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.v("YOu click play in notify", "");
		}
	};

	// Onclick button More (Them) to watch more video
	private View.OnClickListener OnClickShowMoreVideo = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// Top video
			if (VIDEO_PAGE == 0) {
				Intent i = new Intent(getApplicationContext(),
						PagerListVideoActivity.class);
				i.putExtra("type", 0);
				startActivity(i);
			}
			// New Video
			else if (VIDEO_PAGE == 1) {
				Intent i = new Intent(getApplicationContext(),
						PagerListVideoActivity.class);
				i.putExtra("type", 1);
				startActivity(i);
			}
			// Random Video
			else if (VIDEO_PAGE == 2) {
				Intent i = new Intent(getApplicationContext(),
						PagerListVideoActivity.class);
				i.putExtra("type", 2);
				startActivity(i);
			}
		}
	};

}
