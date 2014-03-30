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
package com.phatam.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.phatam.R;

public class SplashActivity extends Activity implements AnimationListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		Animation zoomAnimation = AnimationUtils.loadAnimation(this, R.animator.anim_splash);
		zoomAnimation.setAnimationListener(this);
		((ImageView)findViewById(R.id.imgLogo)).startAnimation(zoomAnimation);
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// Open MainActivity when zoom animation end
		Intent mainActivityIntent = new Intent(this, MainActivity.class);
		this.startActivity(mainActivityIntent);
		this.overridePendingTransition(0, 0);
		this.finish();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

}