package com.phatam.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockActivity;
import com.phatam.R;

public class VideoActivity extends SherlockActivity{
	
	String html = "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 95%; padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" src=\"http://www.youtube.com/embed/msw4ktrSWRw"
            + "?fs=0\" frameborder=\"0\">\n"
            + "</iframe>";
	
	WebView wv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_activity);
		
		// Chage Actionbar background color
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
		
		wv = (WebView)findViewById(R.id.webView); 
		wv.getSettings().setPluginState(PluginState.ON);
	    wv.getSettings().setJavaScriptEnabled(true);
	    wv.setWebChromeClient(new WebChromeClient());
	    wv.loadUrl("http://www.youtube.com/embed/msw4ktrSWRw?autoplay=1&vq=small");
//	        wv.loadDataWithBaseURL("", html , "text/html",  "UTF-8", "");

	}

}
