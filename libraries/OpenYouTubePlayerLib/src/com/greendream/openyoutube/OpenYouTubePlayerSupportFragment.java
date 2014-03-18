/*
 * Copyright (C) 2014 Le Ngoc Anh <greendream.ait@gmail.com>
 * 
 * Copyright (C) 2006 The Android Open Source Project
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
 */

package com.greendream.openyoutube;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.greendream.openyoutube.MediaController.OnToggleFullScreenControler;
import com.keyes.youtube.PlaylistId;
import com.keyes.youtube.VideoId;
import com.keyes.youtube.YouTubeId;
import com.keyes.youtube.YouTubeUtility;

/**
 * <p>
 * Activity that will play a video from YouTube. A specific video or the latest
 * video in a YouTube playlist can be specified in the intent used to invoke
 * this activity. The data of the intent can be set to a specific video by using
 * an Intent data URL of the form:
 * </p>
 * 
 * <pre>
 *     ytv://videoid
 * </pre>
 * 
 * <p>
 * where
 * 
 * <pre>
 * videoid
 * </pre>
 * 
 * is the ID of the YouTube video to be played.
 * </p>
 * 
 * <p>
 * If the user wishes to play the latest video in a YouTube playlist, the Intent
 * data URL should be of the form:
 * </p>
 * 
 * <pre>
 *     ytpl://playlistid
 * </pre>
 * 
 * <p>
 * where
 * 
 * <pre>
 * playlistid
 * </pre>
 * 
 * is the ID of the YouTube playlist from which the latest video is to be
 * played.
 * </p>
 * 
 * <p>
 * Code used to invoke this intent should look something like the following:
 * </p>
 * 
 * <pre>
 * Intent lVideoIntent = new Intent(null, Uri.parse(&quot;ytpl://&quot;
 * 		+ YOUTUBE_PLAYLIST_ID), this, OpenYouTubePlayerActivity.class);
 * startActivity(lVideoIntent);
 * </pre>
 * 
 * <p>
 * There are several messages that are displayed to the user during various
 * phases of the video load process. If you wish to supply text other than the
 * default english messages (e.g., internationalization, etc.), you can pass the
 * text to be used via the Intent's extended data. The messages that can be
 * customized include:
 * 
 * <ul>
 * <li>com.keyes.video.msg.init - activity is initializing.</li>
 * <li>com.keyes.video.msg.detect - detecting the bandwidth available to
 * download video.</li>
 * <li>com.keyes.video.msg.playlist - getting latest video from playlist.</li>
 * <li>com.keyes.video.msg.token - retrieving token from YouTube.</li>
 * <li>com.keyes.video.msg.loband - buffering low-bandwidth.</li>
 * <li>com.keyes.video.msg.hiband - buffering hi-bandwidth.</li>
 * <li>com.keyes.video.msg.error.title - dialog title displayed if anything goes
 * wrong.</li>
 * <li>com.keyes.video.msg.error.msg - message displayed if anything goes wrong.
 * </li>
 * </ul>
 * 
 * <p>
 * For example:
 * </p>
 * 
 * <pre>
 *      Intent lVideoIntent = new Intent(null, Uri.parse("ytpl://"+YOUTUBE_PLAYLIST_ID), this, OpenYouTubePlayerActivity.class);
 *      lVideoIntent.putExtra("com.keyes.video.msg.init", getString("str_video_intro"));
 *      lVideoIntent.putExtra("com.keyes.video.msg.detect", getString("str_video_detecting_bandwidth"));
 *      ...
 *      startActivity(lVideoIntent);
 * </pre>
 * 
 * @author David Keyes
 * @modify Le Ngoc Anh - greendream.ait@gmail.com
 * 
 */
public class OpenYouTubePlayerSupportFragment extends Fragment {
	public static final String SCHEME_YOUTUBE_VIDEO = "ytv";
	public static final String SCHEME_YOUTUBE_PLAYLIST = "ytpl";
	
	static final String YOUTUBE_VIDEO_INFORMATION_URL = "http://www.youtube.com/get_video_info?&video_id=";
	static final String YOUTUBE_PLAYLIST_ATOM_FEED_URL = "http://gdata.youtube.com/feeds/api/playlists/";

	protected ProgressBar mProgressBar;
	protected TextView mProgressMessage;
	protected static VideoView mVideoPlayer;
	protected MediaController mMediaController;

	public final static String MSG_INIT = "com.keyes.video.msg.init";
	protected String mMsgInit = "Initializing";

	public final static String MSG_DETECT = "com.keyes.video.msg.detect";
	protected String mMsgDetect = "Detecting Bandwidth";

	public final static String MSG_PLAYLIST = "com.keyes.video.msg.playlist";
	protected String mMsgPlaylist = "Determining Latest Video in YouTube Playlist";

	public final static String MSG_TOKEN = "com.keyes.video.msg.token";
	protected String mMsgToken = "Retrieving YouTube Video Token";

	public final static String MSG_LO_BAND = "com.keyes.video.msg.loband";
	protected String mMsgLowBand = "Buffering Low-bandwidth Video";

	public final static String MSG_HI_BAND = "com.keyes.video.msg.hiband";
	protected String mMsgHiBand = "Buffering High-bandwidth Video";

	public final static String MSG_ERROR_TITLE = "com.keyes.video.msg.error.title";
	protected String mMsgErrorTitle = "Communications Error";

	public final static String MSG_ERROR_MSG = "com.keyes.video.msg.error.msg";
	protected String mMsgError = "An error occurred during the retrieval of the video.  This could be due to network issues or YouTube protocols.  Please try again later.";

	/** Background task on which all of the interaction with YouTube is done */
	protected static QueryYouTubeTask mQueryYouTubeTask;

	protected String mVideoId = null;

	protected Uri mVideoIdUri;
	
	public OpenYouTubePlayerSupportFragment setVideoIdUri(Uri uri) {
		mVideoIdUri = uri;
		return this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle pSavedInstanceState) {

		super.onCreate(pSavedInstanceState);

		// create the layout of the view
		View returnView = setupView();

		// determine the messages to be displayed as the view loads the video
		extractMessages();

		// set the flag to keep the screen ON so that the video can play without
		// the screen being turned off
		getActivity().getWindow().setFlags(
				android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		mProgressBar.bringToFront();
		mProgressBar.setVisibility(View.VISIBLE);
		mProgressMessage.setText(mMsgInit);

		// extract the play-list or video id from the intent that started this
		// video
		if (mVideoIdUri == null) {
			Log.i(this.getClass().getSimpleName(),
					"No video ID was specified in the intent.  Closing video activity.");
			OpenYouTubePlayerSupportFragment.this.onDestroy();
		}
		String lVideoSchemeStr = mVideoIdUri.getScheme();
		String lVideoIdStr = mVideoIdUri.getEncodedSchemeSpecificPart();
		if (lVideoIdStr == null) {
			Log.i(this.getClass().getSimpleName(),
					"No video ID was specified in the intent.  Closing video activity.");
			OpenYouTubePlayerSupportFragment.this.onDestroy();
		}
		if (lVideoIdStr.startsWith("//")) {
			if (lVideoIdStr.length() > 2) {
				lVideoIdStr = lVideoIdStr.substring(2);
			} else {
				Log.i(this.getClass().getSimpleName(),
						"No video ID was specified in the intent.  Closing video activity.");
				OpenYouTubePlayerSupportFragment.this.onDestroy();
			}
		}

		// /////////////////
		// extract either a video id or a play-list id, depending on the URI
		// scheme
		YouTubeId lYouTubeId = null;
		if (lVideoSchemeStr != null && lVideoSchemeStr.equalsIgnoreCase(SCHEME_YOUTUBE_PLAYLIST)) {
			lYouTubeId = new PlaylistId(lVideoIdStr);
		}

		else if (lVideoSchemeStr != null
				&& lVideoSchemeStr.equalsIgnoreCase(SCHEME_YOUTUBE_VIDEO)) {
			lYouTubeId = new VideoId(lVideoIdStr);
		}

		if (lYouTubeId == null) {
			Log.i(this.getClass().getSimpleName(),
					"Unable to extract video ID from the intent.  Closing video activity.");
			OpenYouTubePlayerSupportFragment.this.onDestroy();
			return returnView;
		}

		mQueryYouTubeTask = (QueryYouTubeTask) new QueryYouTubeTask().execute(lYouTubeId);

		return returnView;
	}

	/**
	 * Determine the messages to display during video load and initialization.
	 */
	private void extractMessages() {
		 mMsgInit = getString(R.string.mMsgInit);
		 mMsgDetect = getString(R.string.mMsgDetect);
		 mMsgPlaylist = getString(R.string.mMsgPlaylist);
		 mMsgToken = getString(R.string.mMsgToken);
		 mMsgLowBand = getString(R.string.mMsgLowBand);
		 mMsgHiBand = getString(R.string.mMsgHiBand);
		 mMsgErrorTitle = getString(R.string.mMsgErrorTitle);
		 mMsgError = getString(R.string.mMsgError);
	}

	/**
	 * Create the view in which the video will be rendered.
	 */
	private View setupView() {
		RelativeLayout lRelLayout = new RelativeLayout(getActivity());
		lRelLayout.setId(2);
		lRelLayout.setGravity(Gravity.CENTER);
		lRelLayout.setBackgroundColor(Color.BLACK);
		RelativeLayout.LayoutParams lRelLayoutParms = new RelativeLayout.LayoutParams(
														ViewGroup.LayoutParams.FILL_PARENT,
														ViewGroup.LayoutParams.FILL_PARENT);
		lRelLayout.setLayoutParams(lRelLayoutParms);

		mVideoPlayer = new VideoView(getActivity());
		mVideoPlayer.setId(3);
		RelativeLayout.LayoutParams lVideoViewLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		lVideoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		lVideoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		lVideoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		lVideoViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		mVideoPlayer.setLayoutParams(lVideoViewLayoutParams);
		lRelLayout.addView(mVideoPlayer);

		mMediaController = new MediaController(OpenYouTubePlayerSupportFragment.this.getActivity(), false);
		mMediaController.setFullScreenController(mToggleFullScreenerControl);
		mMediaController.setId(4);
		RelativeLayout.LayoutParams lMediaControllerLayoutParam = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		mMediaController.setLayoutParams(lMediaControllerLayoutParam);
		lRelLayout.addView(mMediaController);
		mMediaController.setVisibility(View.GONE);
		
		mProgressBar = new ProgressBar(getActivity());
		mProgressBar.setIndeterminate(true);
		mProgressBar.setVisibility(View.VISIBLE);
		mProgressBar.setEnabled(true);
		mProgressBar.setId(5);
		android.widget.RelativeLayout.LayoutParams lProgressBarLayoutParms = new android.widget.RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lProgressBarLayoutParms.addRule(RelativeLayout.CENTER_IN_PARENT);
		mProgressBar.setLayoutParams(lProgressBarLayoutParms);
		lRelLayout.addView(mProgressBar);

		mProgressMessage = new TextView(getActivity());
		mProgressMessage.setId(6);
		android.widget.RelativeLayout.LayoutParams lProgressMsgLayoutParms = new android.widget.RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lProgressMsgLayoutParms.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lProgressMsgLayoutParms.addRule(RelativeLayout.BELOW, 5);
		mProgressMessage.setLayoutParams(lProgressMsgLayoutParms);
		mProgressMessage.setTextColor(Color.LTGRAY);
		mProgressMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		mProgressMessage.setText("...");
		lRelLayout.addView(mProgressMessage);

		return lRelLayout;
	}

	
	
	public void updateProgress(String pProgressMsg){
		try {
			mProgressMessage.setText(pProgressMsg);
		} catch(Exception e) {
			Log.e(this.getClass().getSimpleName(), "Error updating video status!", e);
		}
	}

	private class ProgressUpdateInfo {

		public String mMsg;

		public ProgressUpdateInfo(String pMsg) {
			mMsg = pMsg;
		}
	}

	/**
	 * Task to figure out details by calling out to YouTube GData API. We only
	 * use public methods that don't require authentication.
	 * 
	 */
	private class QueryYouTubeTask extends
			AsyncTask<YouTubeId, ProgressUpdateInfo, Uri> {

		private boolean mShowedError = false;

		@Override
		protected Uri doInBackground(YouTubeId... pParams) {
			String lUriStr = null;
			String lYouTubeFmtQuality = "17"; // 3gpp medium quality, which
												// should be fast enough to view
												// over EDGE connection
			String lYouTubeVideoId = null;

			if (isCancelled())
				return null;

			try {

				publishProgress(new ProgressUpdateInfo(mMsgDetect));

				WifiManager lWifiManager = (WifiManager) OpenYouTubePlayerSupportFragment.this.getActivity()
						.getSystemService(Context.WIFI_SERVICE);
				TelephonyManager lTelephonyManager = (TelephonyManager) OpenYouTubePlayerSupportFragment.this.getActivity()
						.getSystemService(Context.TELEPHONY_SERVICE);

				// //////////////////////////
				// if we have a fast connection (wifi or 3g), then we'll get a
				// high quality YouTube video
				if ((lWifiManager.isWifiEnabled()
						&& lWifiManager.getConnectionInfo() != null && lWifiManager
						.getConnectionInfo().getIpAddress() != 0)
						|| ((lTelephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS
								||

								/*
								 * Dicky... using literals to make backwards
								 * compatible with 1.5 and 1.6
								 */
								lTelephonyManager.getNetworkType() == 9 /* HSUPA */
								|| lTelephonyManager.getNetworkType() == 10 /* HSPA */
								|| lTelephonyManager.getNetworkType() == 8 /* HSDPA */
								|| lTelephonyManager.getNetworkType() == 5 /* EVDO_0 */|| lTelephonyManager
								.getNetworkType() == 6 /* EVDO A */)

						&& lTelephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED)) {
					lYouTubeFmtQuality = "18";
				}
				
				
				// /////////////////////////////////
				// if the intent is to show a playlist, get the latest video id
				// from the playlist, otherwise the video
				// id was explicitly declared.
				if (pParams[0] instanceof PlaylistId) {
					publishProgress(new ProgressUpdateInfo(mMsgPlaylist));
					lYouTubeVideoId = YouTubeUtility
							.queryLatestPlaylistVideo((PlaylistId) pParams[0]);
				}

				else if (pParams[0] instanceof VideoId) {
					lYouTubeVideoId = pParams[0].getId();
				}

				mVideoId = lYouTubeVideoId;

				publishProgress(new ProgressUpdateInfo(mMsgToken));

				if (isCancelled())
					return null;

				// //////////////////////////////////
				// calculate the actual URL of the video, encoded with proper
				// YouTube token
				lUriStr = YouTubeUtility.calculateYouTubeUrl(
						lYouTubeFmtQuality, true, lYouTubeVideoId);

				if (isCancelled())
					return null;

				if (lYouTubeFmtQuality.equals("17")) {
					publishProgress(new ProgressUpdateInfo(mMsgLowBand));
				} else {
					publishProgress(new ProgressUpdateInfo(mMsgHiBand));
				}

			} catch (Exception e) {
				Log.e(this.getClass().getSimpleName(),
						"Error occurred while retrieving information from YouTube.",
						e);
			}

			if (lUriStr != null) {
				return Uri.parse(lUriStr);
			} else {
				return null;
			}
		}

		@Override
		protected void onPostExecute(Uri pResult) {
			super.onPostExecute(pResult);

			try {
				if (isCancelled())
					return;

				if (pResult == null) {
					throw new RuntimeException("Invalid NULL Url.");
				}

				mVideoPlayer.setVideoURI(pResult);

				if (isCancelled())
					return;

				// Add listeners for finish of video
				mVideoPlayer.setOnCompletionListener(mOnMediaPlayerCompletionListener);

				if (isCancelled())
					return;

				mVideoPlayer.setMediaController(mMediaController);
				
				// Hide loading progress when video prepare completed
				mVideoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

							@Override
							public void onPrepared(MediaPlayer pMp) {
								if (isCancelled())
									return;
								OpenYouTubePlayerSupportFragment.this.mProgressBar
										.setVisibility(View.GONE);
								OpenYouTubePlayerSupportFragment.this.mProgressMessage
										.setVisibility(View.GONE);
								
								mMediaController.show();
								mOnRetrieveVideoInfoListener.onRetrieveVideoLength(mMediaController.getVideoLength());
								mOnRetrieveVideoInfoListener.onMediaPlayerPrepareCompleted(mVideoPlayer.getMediaPLayer());
							}

						});
				

				if (isCancelled())
					return;

				mVideoPlayer.requestFocus();
				
			} catch (Exception e) {
				Log.e(this.getClass().getSimpleName(), "Error playing video!",
						e);

				if (!mShowedError) {
					showErrorAlert();
				}
			}
		}

		private void showErrorAlert() {

			try {
				Builder lBuilder = new AlertDialog.Builder(
						OpenYouTubePlayerSupportFragment.this.getActivity());
				lBuilder.setTitle(mMsgErrorTitle);
				lBuilder.setCancelable(false);
				lBuilder.setMessage(mMsgError);

				lBuilder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface pDialog,
									int pWhich) {
								OpenYouTubePlayerSupportFragment.this.onDestroy();
							}

						});

				AlertDialog lDialog = lBuilder.create();
				lDialog.show();
			} catch (Exception e) {
				Log.e(this.getClass().getSimpleName(),
						"Problem showing error dialog.", e);
			}
		}

		@Override
		protected void onProgressUpdate(ProgressUpdateInfo... pValues) {
			super.onProgressUpdate(pValues);

			OpenYouTubePlayerSupportFragment.this.updateProgress(pValues[0].mMsg);
		}

	}
	
//	private int stopPosition = 0;
	@Override
	public void onPause() {
		// Saving the video play state
//		stopPosition = 0;
//		try {
//			stopPosition = mVideoPlayer.getCurrentPosition();
//			mVideoPlayer.pause();
//		} catch (Exception e) {
//			
//		}
		
//		Toast.makeText(this.getActivity(), "Destroy --> stopPlayback", 1000).show();
		super.onPause();
	}

	@Override
	public void onStop() {
//		Toast.makeText(this.getActivity(), "onStop", 2000).show();
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
//		Toast.makeText(this.getActivity(), "Destroy --> stopPlayback", 1000).show();
		
		YouTubeUtility.markVideoAsViewed(this.getActivity(), mVideoId);

		if (mQueryYouTubeTask != null) {
			mQueryYouTubeTask.cancel(true);
		}

		if (mVideoPlayer != null) {
//			mVideoPlayer.stopPlayback();
		}

		// clear the flag that keeps the screen ON
		getActivity().getWindow().clearFlags(
				android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//		this.mQueryYouTubeTask = null;
//		this.mVideoPlayer = null;

	}
	
	@Override
	public void onResume() {
//		try {
//			mVideoPlayer.seekTo(stopPosition);
//			mVideoPlayer.start();
//		} catch (Exception e) {
//			
//		}
		super.onResume();
	}
	
	/**
	 * If Activity contain this Fragment want to handle full-screen event via the media controller
	 * it must be implement the ToggleFullScreenerControl and use this method to listen this event.
	 * 
	 * @param toggleScreenController : activity that implemented the ToggleFullScreenerControl interface
	 */
	public void setFullScreenController(OnToggleFullScreenControler toggleScreenController) {
		mToggleFullScreenerControl = toggleScreenController;
		
		if (mMediaController != null) {
			mMediaController.setFullScreenController(mToggleFullScreenerControl);
		}
    }
	
	private OnToggleFullScreenControler mToggleFullScreenerControl;
	
	public void updateFullScreenStatus() {
		if (mMediaController != null) {
			mMediaController.updateFullScreenStatus();
		}
	}
	

    public interface OnRetrieveVideoInfoListener {
		public void onRetrieveVideoLength(String videoLength);
		public void onMediaPlayerPrepareCompleted(MediaPlayer mp);
	}
    
    private OnRetrieveVideoInfoListener mOnRetrieveVideoInfoListener;
	public void setOnRetrieveVideoInfoListener(OnRetrieveVideoInfoListener l) {
		mOnRetrieveVideoInfoListener = l;
	}
	
	public void setMediaPlayer(MediaPlayer mp) {
		if (mVideoPlayer != null){
			mVideoPlayer.setMediaPLayer(mp);
		}
	}
	
	// This class is a bridge to connect UI with MediaPlayer event
	private OnCompletionListener mOnMediaPlayerCompletionListener;
	public OnCompletionListener getOnMediaPlayerCompletionListener() {
		return mOnMediaPlayerCompletionListener;
	}

	public void setOnMediaPlayerCompletionListener(OnCompletionListener listener) {
		mOnMediaPlayerCompletionListener = listener;
	}
	
}