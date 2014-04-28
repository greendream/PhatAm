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


package com.phatam.playback;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.phatam.R;
import com.phatam.activities.FullVideoInfoActivity;

public class PhatAmPlayBackService extends Service {

	final int NOTIFICATION_ID = 1;
	public static final String
		BROADCAST_PLAYBACK_STOP = "stop",
		BROADCAST_PLAYBACK_PAUSE = "pause";
	
	
	protected static MediaPlayer mMediaPlayer;
	public static final String EXTRA_VIDEO_TITLE = "video_title";
	public static final String EXTRA_VIDEO_ARTIST = "audio_artist";

	private String mVideoTitle = "";
	private String mVideoArtist = "";
	
	public static PhatAmPlayBackService instance;
	
	public static void setMediaPlayer(MediaPlayer mp) {
		mMediaPlayer = mp;
	}
	
	public static MediaPlayer getMediaPlayer() {
		return mMediaPlayer;
	}
	
	final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(BROADCAST_PLAYBACK_STOP))
				stopSelf();
			else if (action.equals(BROADCAST_PLAYBACK_PAUSE)) {
				if (mMediaPlayer.isPlaying())
					mMediaPlayer.pause();
				else
					mMediaPlayer.start();
			}
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		try {
			mVideoTitle = intent.getStringExtra(EXTRA_VIDEO_TITLE);
			mVideoArtist = intent.getStringExtra(EXTRA_VIDEO_ARTIST);
			showNotification();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BROADCAST_PLAYBACK_STOP);
		intentFilter.addAction(BROADCAST_PLAYBACK_PAUSE);
		registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	public void onDestroy() // called when the service is stopped
	{
		try {
			stopForeground(true);
			unregisterReceiver(broadcastReceiver);
		} catch (Exception e) {
			
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private PendingIntent makePendingIntent(String broadcast) {
		Intent intent = new Intent(broadcast);
		return PendingIntent
				.getBroadcast(getApplicationContext(), 0, intent, 0);
	}

	private void showNotification() {
		// Create notification
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(mVideoTitle)
				.setContentText(mVideoArtist)
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
				// audio url will show in notification
				.setContentIntent(
						PendingIntent.getActivity(getApplicationContext(), 0,
								new Intent(getApplicationContext(),
										FullVideoInfoActivity.class), 0))
				.addAction(R.drawable.ic_play, "Stop",
						makePendingIntent(BROADCAST_PLAYBACK_STOP))
				.addAction(R.drawable.ic_media_pause, "Pause",
						makePendingIntent(BROADCAST_PLAYBACK_PAUSE));

		// Show notification
		startForeground(NOTIFICATION_ID, notificationBuilder.build());
	}

}
