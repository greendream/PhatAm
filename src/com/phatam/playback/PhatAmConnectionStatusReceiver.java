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

import java.util.Vector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.phatam.interfaces.OnConnectionStatusChangeListener;

public class PhatAmConnectionStatusReceiver extends BroadcastReceiver {
	private static Vector<OnConnectionStatusChangeListener> connectionObservers = new Vector<OnConnectionStatusChangeListener>();

	public static void addConnectionObserver(OnConnectionStatusChangeListener o) {
		connectionObservers.add(o);		
	}
	
	public static void removeConnectionObserver(OnConnectionStatusChangeListener o) {
		connectionObservers.remove(o);		
	}
	
	public static void notifyConnectionStatusChange() {
		for (OnConnectionStatusChangeListener o : connectionObservers) {
			try {
				o.onConnectionStatusChange();
			} catch (Exception e) {
				
			}
		}	
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
//		Toast.makeText(context, "PhatAmConnectionStatusReceiver: " + ConnectionUtil.getConnectivityStatusString(context) , Toast.LENGTH_LONG).show();
		notifyConnectionStatusChange();
	}

}
