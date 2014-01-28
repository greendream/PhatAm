package com.phatam.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phatam.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.phatam.entity.Episode;
import com.phatam.entity.VideoItem;

public class EpisodeAdapter extends ArrayAdapter<Episode> {

	private final Activity mActivity;
	private VideoItem mVideoItem;
	private ArrayList<Episode> mEpisodeData;
	DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public EpisodeAdapter(Activity activity, VideoItem videoItem) {
		// TODO Auto-generated constructor stub
		super(activity, R.layout.list_item_episode, videoItem.getEpisodes());
		mVideoItem = videoItem;
		this.mActivity = activity;
		this.mEpisodeData = videoItem.getEpisodes();
		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(10)).build();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_item_episode, null);
		}
			
		((TextView) rowView.findViewById(R.id.episode_index)).setText("Phần " + mEpisodeData.get(position).getEpisodeId());
		((TextView) rowView.findViewById(R.id.video_list_astist)).setText("Tác giả: " + mVideoItem.getArtist())	;
		imageLoader.displayImage(mEpisodeData.get(position).getYoutubeThumb(),
				((ImageView) rowView.findViewById(R.id.video_list_image)), options, animateFirstListener);
		
		
		if (position % 2 == 0) {
			rowView.setBackgroundColor(mActivity.getResources().getColor(
					R.color.list_row_green));
		} else {
			rowView.setBackgroundColor(mActivity.getResources().getColor(
					R.color.white));
		}
		return rowView;
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