package com.phatam.websevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RestClient {

	public static final String BASE_URL = "http://phatam.com/rest/public/index.php/";

	private static AsyncHttpClient client = new AsyncHttpClient();

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	public static void GET(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void POST(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.setTimeout(100000);
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	/**
	 * This is Asyntask Download web-page source via URL run in Background
	 * 
	 * @author anhle
	 * 
	 */
	public static String getJSONFromUrl(String url) {
		url = BASE_URL + url;
		String response = "";
		Log.i("START RUN GET JSON TASK URL", url);

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

		} catch (Exception e) {
			response = e.toString();
		}

		return response;
	}
}