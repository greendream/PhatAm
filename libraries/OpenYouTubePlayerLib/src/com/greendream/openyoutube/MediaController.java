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

import java.util.Formatter;
import java.util.Locale;

import android.content.Context;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * A view containing controls for a MediaPlayer. Typically contains the
 * buttons like "Play/Pause", "Rewind", "Fast Forward" and a progress
 * slider. It takes care of synchronizing the controls with the state
 * of the MediaPlayer.
 * <p>
 * The way to use this class is to instantiate it programmatically.
 * The MediaController will create a default set of controls
 * and put them in a window floating above your application. Specifically,
 * the controls will float above the view specified with setAnchorView().
 * The window will disappear if left idle for three seconds and reappear
 * when the user touches the anchor view.
 * <p>
 * Functions like show() and hide() have no effect when MediaController
 * is created in an xml layout.
 * 
 * MediaController will hide and
 * show the buttons according to these rules:
 * <ul>
 * <li> The "previous" and "next" buttons are hidden until setPrevNextListeners()
 *   has been called
 * <li> The "previous" and "next" buttons are visible but disabled if
 *   setPrevNextListeners() was called with null listeners
 * <li> The "rewind" and "fastforward" buttons are shown unless requested
 *   otherwise by using the MediaController(Context, boolean) constructor
 *   with the boolean set to false
 * </ul>
 */
public class MediaController extends RelativeLayout {

    private MediaPlayerControl  		mMediaPlayer;
    private OnToggleFullScreenControler	mOnToggleFullScreener;
    private Context						mContext;
    private View                		mRootViewControler;
    private ProgressBar         		mProgress;
    private TextView            		mEndTime, mCurrentTime;
    private boolean             		mShowing;
    private boolean           			mDragging;
    private static final int    		sDefaultTimeout = 3000;
    private static final int    		FADE_OUT = 1;
    private static final int    		SHOW_CONTROLER = 2;
    private boolean             		mUseFastForward;
    private boolean             		mFromXml;
    private boolean             		mListenersSet;
    private View.OnClickListener 		mNextListener, mPrevListener;
    StringBuilder               		mFormatBuilder;
    Formatter                   		mFormatter;
    private ImageButton         		mPauseButton;
    private ImageButton        			mFfwdButton;
    private ImageButton         		mRewButton;
    private ImageButton         		mNextButton;
    private ImageButton         		mPrevButton;
    private ImageButton         		mFullscreenToggleButton;
    
    public MediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mUseFastForward = true;
        mFromXml = true;
        makeControllerView();
    }

    public MediaController(Context context, boolean useFastForward) {
        super(context);
        mContext = context;
        mUseFastForward = useFastForward;
        makeControllerView();
    }

    public MediaController(Context context) {
        super(context);
        mContext = context;
        mUseFastForward = true;
        makeControllerView();
    }
    
    public void setMediaPlayer(MediaPlayerControl player) {
        mMediaPlayer = player;
        updatePausePlay();
    }
    
    public void setFullScreenController(OnToggleFullScreenControler screenController) {
    	mOnToggleFullScreener = screenController;
    	updateFullScreenStatus();
    }

    /**
     * Create the view that holds the widgets that control playback.
     * Derived classes can override this to create their own.
     * @return The controller view.
     * @hide This doesn't work as advertised
     */
    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRootViewControler = inflate.inflate(R.layout.media_controller, null);
        
        initControllerView(mRootViewControler);
        this.addView(mRootViewControler);
        
        return mRootViewControler;
    }

    private void initControllerView(View v) {
    	
    	// Top Controller
		// Fill data to preview
		/*((TextView) findViewById(R.id.tvPreviewVideoTitle)).setText(mVideoItem.getVideoTitle());
		((TextView) findViewById(R.id.tvPreviewVideoAuthor)).setText(mVideoItem.getArtist());
		((TextView) findViewById(R.id.tvPreviewVideoView)).setText(mVideoItem.getYoutubeView() + " lượt xem");*/
    	
    	// Center Controller
    	mPauseButton = (ImageButton) v.findViewById(R.id.pause);
        if (mPauseButton != null) {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }
        mFfwdButton = (ImageButton) v.findViewById(R.id.ffwd);
        if (mFfwdButton != null) {
            mFfwdButton.setOnClickListener(mFfwdListener);
            if (!mFromXml) {
                mFfwdButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
            }
        }

        mRewButton = (ImageButton) v.findViewById(R.id.rew);
        if (mRewButton != null) {
            mRewButton.setOnClickListener(mRewListener);
            if (!mFromXml) {
                mRewButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
            }
        }

        // By default these are hidden. They will be enabled when setPrevNextListeners() is called 
        mNextButton = (ImageButton) v.findViewById(R.id.next);
        if (mNextButton != null && !mFromXml && !mListenersSet) {
            mNextButton.setVisibility(View.GONE);
        }
        mPrevButton = (ImageButton) v.findViewById(R.id.prev);
        if (mPrevButton != null && !mFromXml && !mListenersSet) {
            mPrevButton.setVisibility(View.GONE);
        }

    	
    	// Bottom Controller
        mFullscreenToggleButton = (ImageButton) v.findViewById(R.id.btnFullScreenToggle);
        mFullscreenToggleButton.setOnClickListener(mFullScreenToggleListener);
        
        
        mProgress = (ProgressBar) v.findViewById(R.id.mediacontroller_progress);
        if (mProgress != null) {
            if (mProgress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) mProgress;
                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(1000);
        }

        mEndTime = (TextView) v.findViewById(R.id.time);
        mCurrentTime = (TextView) v.findViewById(R.id.time_current);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        installPrevNextListeners();
    }


    /**
     * Show the controller on screen. It will go away
     * automatically after 3 seconds of inactivity.
     */
    public void show() {
        show(sDefaultTimeout);
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 'timeout' milliseconds of inactivity.
     * @param timeout The timeout in milliseconds. Use 0 to show
     * the controller until hide() is called.
     */
    public void show(int timeout) {

        if (!mShowing) {
        	this.bringToFront();
        	this.setVisibility(View.VISIBLE);
            setProgress();

            mShowing = true;
        }
        updatePausePlay();
        
        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessage(SHOW_CONTROLER);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }
    
    public boolean isShowing() {
        return mShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        if (mShowing) {
            try {
                mHandler.removeMessages(SHOW_CONTROLER);
                this.setVisibility(View.GONE);
            } catch (IllegalArgumentException ex) {
                Log.w("MediaController", "already removed");
            }
            mShowing = false;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_CONTROLER:
                    pos = setProgress();
                    if (!mDragging && mShowing && mMediaPlayer.isPlaying()) {
                    	msg = obtainMessage(SHOW_CONTROLER);
                    	sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    
                    break;
            }
        }
    };

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int setProgress() {
        if (mMediaPlayer == null || mDragging) {
            return 0;
        }
        int position = mMediaPlayer.getCurrentPosition();
        int duration = mMediaPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgress.setProgress( (int) pos);
            }
            int percent = mMediaPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        if (mEndTime != null)
            mEndTime.setText(stringForTime(duration));
        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));

        return position;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        show(sDefaultTimeout);
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_DOWN && (
                keyCode ==  KeyEvent.KEYCODE_HEADSETHOOK ||
                keyCode ==  KeyEvent.KEYCODE_SPACE)) {
            doPauseResume();
            show(sDefaultTimeout);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN ||
                keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            hide();
        } else {
            show(sDefaultTimeout);
        }
        return super.dispatchKeyEvent(event);
    }

    private View.OnClickListener mPauseListener = new View.OnClickListener() {
        public void onClick(View v) {
            doPauseResume();
            show(sDefaultTimeout);
        }
    };

    public void updateFullScreenStatus() {
    	if (mRootViewControler == null)
            return;

        ImageButton button = (ImageButton) mRootViewControler.findViewById(R.id.btnFullScreenToggle);
        if (button == null)
            return;

        if (mOnToggleFullScreener.isFullScreen()) {
            button.setBackgroundResource(R.drawable.ic_media_fullscreen_shrink);
        } else {
        	button.setBackgroundResource(R.drawable.ic_media_fullscreen_stretch);
        }
    }
    
    private void updatePausePlay() {
        if (mRootViewControler == null)
            return;

        ImageButton button = (ImageButton) mRootViewControler.findViewById(R.id.pause);
        if (button == null)
            return;

        if (mMediaPlayer.isPlaying()) {
            button.setBackgroundResource(R.drawable.ic_media_pause);
        } else {
        	button.setBackgroundResource(R.drawable.ic_media_play);
        }
    }

    private void doPauseResume() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        } else {
            mMediaPlayer.start();
        }
        updatePausePlay();
    }

    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {
        long duration;
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);
            duration = mMediaPlayer.getDuration();
        }
        public void onProgressChanged(SeekBar bar, int progress, boolean fromtouch) {
            if (fromtouch) {
                mDragging = true;
                long newposition = (duration * progress) / 1000L;
                mMediaPlayer.seekTo( (int) newposition);
                if (mCurrentTime != null)
                    mCurrentTime.setText(stringForTime( (int) newposition));
            }
        }
        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null) {
            mPauseButton.setEnabled(enabled);
        }
        if (mFfwdButton != null) {
            mFfwdButton.setEnabled(enabled);
        }
        if (mRewButton != null) {
            mRewButton.setEnabled(enabled);
        }
        if (mNextButton != null) {
            mNextButton.setEnabled(enabled && mNextListener != null);
        }
        if (mPrevButton != null) {
            mPrevButton.setEnabled(enabled && mPrevListener != null);
        }        
        
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }

        super.setEnabled(enabled);
    }

    private View.OnClickListener mRewListener = new View.OnClickListener() {
        public void onClick(View v) {
            int pos = mMediaPlayer.getCurrentPosition();
            pos -= 5000; // milliseconds
            mMediaPlayer.seekTo(pos);
            setProgress();

            show(sDefaultTimeout);
        }
    };

    private View.OnClickListener mFfwdListener = new View.OnClickListener() {
        public void onClick(View v) {
            int pos = mMediaPlayer.getCurrentPosition();
            pos += 15000; // milliseconds
            mMediaPlayer.seekTo(pos);
            setProgress();

            show(sDefaultTimeout);
        }
    };
    
    private View.OnClickListener mFullScreenToggleListener = new View.OnClickListener() {
        public void onClick(View v) {
        	mOnToggleFullScreener.onToggleFullScreen();
        }
    };


    private void installPrevNextListeners() {
        if (mNextButton != null) {
            mNextButton.setOnClickListener(mNextListener);
            mNextButton.setEnabled(mNextListener != null);
        }

        if (mPrevButton != null) {
            mPrevButton.setOnClickListener(mPrevListener);
            mPrevButton.setEnabled(mPrevListener != null);
        }
    }

    public void setPrevNextListeners(View.OnClickListener next, View.OnClickListener prev) {
        mNextListener = next;
        mPrevListener = prev;
        mListenersSet = true;

        if (mRootViewControler != null) {
            installPrevNextListeners();
            
            if (mNextButton != null && !mFromXml) {
                mNextButton.setVisibility(View.VISIBLE);
            }
            if (mPrevButton != null && !mFromXml) {
                mPrevButton.setVisibility(View.VISIBLE);
            }
        }
    }

    
    public interface MediaPlayerControl {
        void    start();
        void    pause();
        int     getDuration();
        int     getCurrentPosition();
        void    seekTo(int pos);
        boolean isPlaying();
        int     getBufferPercentage();
    };
    
    public interface OnToggleFullScreenControler {

        void	onToggleFullScreen();
        boolean isFullScreen();
    }
    
	public void toggleFullScreen(boolean pIsFullScreen) {
		// TODO Auto-generated method stub
		
	}
	
	public String getVideoLength() {
		if (mEndTime != null) {
			return mEndTime.getText().toString();
		}
		
		return "-:-";
	}

}