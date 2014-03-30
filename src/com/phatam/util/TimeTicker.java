package com.phatam.util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import android.os.Handler;

import com.phatam.interfaces.OnTimerTickListeners;

public class TimeTicker {
	private int mInterval; // milliseconds
	private Vector<OnTimerTickListeners> mTimeTickerListeners = new Vector<OnTimerTickListeners>(); 
	final Handler mHandler = new Handler();
	private Timer mTimer;
	private TimerTask mTimerTask;
	
	// CONSTRUCTOR
	public TimeTicker() {
		mInterval = 1;
	}
	
	public TimeTicker(int pInterval) {
		if (pInterval < 1) {
			mInterval = 1;
		} else {
			mInterval = pInterval;
		}
	}

	// GETTER & SETTER
	public void setInterval(int pInterval) {
		if (pInterval < 1) {
			mInterval = 1;
		} else {
			mInterval = pInterval;
		}
	}
	
	public int getInterval() {
		return mInterval;
	}
	
	
	// OBJECT in Observer MANAGER
	
	public void addOnTimeTickerListener(OnTimerTickListeners l) {
		mTimeTickerListeners.add(l);
	}
	
	public void removeOnTimeTickerListener(OnTimerTickListeners l) {
		mTimeTickerListeners.remove(l);
	}
	
	// CONTROL	
	public void start() {
		mTimer = new Timer(true);
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
					mHandler.post(new Runnable() {
		            	@Override
		            	public void run() {
							notifyTimerTick();
		            	}
		        	});
			}
		};
		
		mTimer.schedule(mTimerTask, 0, mInterval); // 1000 = 1 second.
		
	}
	
	public void stop() {
		mTimer.cancel();
	}
	
	/**
	 * Trying notify to the observers event timer tick
	 */
	private void notifyTimerTick() {
		for (OnTimerTickListeners o : mTimeTickerListeners) {
			try {
				o.onTimerTick();
			} catch (Exception e) {
				
			}
		}		
	}
	
	
}
