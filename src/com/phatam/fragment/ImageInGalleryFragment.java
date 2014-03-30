package com.phatam.fragment;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.phatam.R;
import com.phatam.config.GlobalData;

public final class ImageInGalleryFragment extends Fragment {
	public final static String TAG = "ImageInGalleryFragment";
	
	private String mImageUrl;
	private ImageView ivImage;
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public ImageInGalleryFragment() {
		mImageUrl = "";
	}
	
	public ImageInGalleryFragment setImageUrl(String pImageUrl) {
		mImageUrl = pImageUrl;
		return this;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.build();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(com.phatam.R.layout.fragment_image_in_gallery,container, false);
		
		ivImage = (ImageView) rootView.findViewById(R.id.ivImage);
		GlobalData.imageLoader.displayImage(mImageUrl, ivImage, options, animateFirstListener);
		
		return rootView;
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
