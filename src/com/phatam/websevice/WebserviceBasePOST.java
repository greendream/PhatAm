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


package com.phatam.websevice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class WebserviceBasePOST implements SubjectPOST {

	public static final int NOTIFY_POST_JSON_COMPLETED = 0;
	public static final int NOTIFY_POST_JSON_FAIL = 1;

	protected Vector<OnPostJsonListener> mOnPostJsonListeners = new Vector<OnPostJsonListener>();

	protected String mToken;
	protected String mResponse;
	protected JSONObject mRequestJSONObject = null;
	
	public void setToken(String token) {
		mToken = token;
	}
	
	protected void postJSONObject(String request, JSONObject object) {
		mResponse = "[]";
		mRequestJSONObject = object;
		PostJSONObjectTask task = new PostJSONObjectTask();
		task.execute(new String[] { request });

	}

	/**
	 * This is Asyntask Download web-page source via URL run in Background
	 * 
	 * @author anhle
	 * 
	 */
	protected class PostJSONObjectTask extends AsyncTask<String, Void, String> {
		Boolean isFail = true;
		@Override
		protected String doInBackground(String... urls) {
			
			String response = "";
			for (String url : urls) {
				try {
					Log.i("REQUEST -- POST JSON TASK", mRequestJSONObject.toString());
					HttpPost httpPost = new HttpPost(url);
			        httpPost.setEntity(new StringEntity(mRequestJSONObject.toString(), HTTP.UTF_8));
			        httpPost.setHeader("token", mToken);
			        httpPost.setHeader("Accept", "application/json");
			        httpPost.setHeader("Content-type", "application/json");
					isFail = false;
					
					HttpClient client = new DefaultHttpClient();					
					HttpResponse httpResponse = client.execute(httpPost);
					HttpEntity entity = httpResponse.getEntity();
					
					return EntityUtils.toString(entity);
				} catch (UnsupportedEncodingException e) {
					response = e.toString();
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					response = e.toString();
					e.printStackTrace();
				} catch (IOException e) {
					response = e.toString();
					e.printStackTrace();
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(String r) {
			mResponse = r;
			Log.i("RESPONSE -- POST JSON TASK", mResponse);
			
			if (isFail) {
				notifyChange(NOTIFY_POST_JSON_FAIL);
			} else {
				notifyChange(NOTIFY_POST_JSON_COMPLETED);
			}
		}

	}
	
	@Override
	public void addOnPostJsonListener(OnPostJsonListener o) {
		// TODO Auto-generated method stub
		mOnPostJsonListeners.add(o);
	}

	@Override
	public void removeOnPostJsonObserver(OnPostJsonListener o) {
		// TODO Auto-generated method stub
		mOnPostJsonListeners.remove(o);
	}

	@Override
	public void notifyChange(int type) {
		// TODO Auto-generated method stub
		for (OnPostJsonListener o : mOnPostJsonListeners) {
			switch (type) {

			case NOTIFY_POST_JSON_COMPLETED:
				o.onPostJsonCompleted(mResponse);
				break;

			case NOTIFY_POST_JSON_FAIL:
				o.onPostJsonFail(mResponse);
				break;
			}
		}
	}
}
