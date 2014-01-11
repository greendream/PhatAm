package com.phatam.websevice;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.phatam.entity.ArtistItem;
import com.phatam.entity.CategoryItem;
import com.phatam.entity.Episode;
import com.phatam.entity.Tag;
import com.phatam.entity.VideoInfo;
import com.phatam.entity.VideoItem;

public class RestClientAPI {
	int limit = 15;

	public RestClientAPI() {
		// TODO Auto-generated constructor stub
	}

	public static List<CategoryItem> getAllCategory() throws JSONException {

		final List<CategoryItem> data = new ArrayList<CategoryItem>();
		Log.v("array received", "" + data.toString());
		RestClient.GET("category", null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject result) {
				JSONArray jArray;
				Log.v("array received", "" + result.toString());
				try {
					jArray = result.getJSONArray("videos");
					Log.v("array received", "" + jArray.toString());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject line_object = jArray.getJSONObject(i);
						int id = line_object.getInt("id");
						int parent_id = line_object.getInt("parent_id");
						String tag = line_object.getString("tag");
						String name = line_object.getString("name");
						int position = line_object.getInt("position");
						int total_videos = line_object.getInt("total_videos");
						CategoryItem item = new CategoryItem(id, parent_id,
								tag, name, position, total_videos);
						data.add(item);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return data;
	}

	// get videos by category with ordeBy and offset
	@SuppressLint("DefaultLocale")
	public static List<VideoItem> getVideoByCategory(int id, String orderBy,
			int offSet) {
		final List<VideoItem> data = new ArrayList<VideoItem>();
		String url = String.format("category/%d/%s/%d", id, orderBy, offSet);
		Log.v("url", url);
		RestClient.GET(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject result) {
				JSONArray jArray;
				Log.v("Result -------", result.toString());
				try {
					jArray = result.getJSONArray("videos");
					Log.v("array xxx received", "" + jArray.toString());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject line_object = jArray.getJSONObject(i);
						int id = line_object.getInt("id");
						String uniq_id = line_object.getString("uniq_id");
						String artist = line_object.getString("artist");
						String video_title = line_object
								.getString("video_title");
						String description = line_object
								.getString("description");
						String yt_thumb = line_object.getString("yt_thumb");
						int site_views = line_object.getInt("site_views");
						String mp3 = line_object.getString("audio");
						String yt_id = line_object.getString("yt_id");
						VideoItem item = new VideoItem(uniq_id, artist,
								video_title, description, yt_id, yt_thumb,
								site_views);
						data.add(item);
					}
//					RestClientUsage.getVideoFullInfo(data.get(0));
					RestClientAPI.getVideoRecommend(data.get(0), "same_artist");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return data;
	}

	public static VideoItem getVideoRecommend(VideoItem video, final String type) {
		final List<VideoInfo> related = new ArrayList<VideoInfo>();
		String url = String.format("video/%s/%s", video.getUniqueId(), type);
		RestClient.GET(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject result) {
				JSONArray jArray;
				try {
					// Get related
					jArray = result.getJSONArray(type);
					Log.v("array received", "" + jArray.toString());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject line_object = jArray.getJSONObject(i);
						int id = line_object.getInt("id");
						String uniq_id = line_object.getString("uniq_id");
						String artist = line_object.getString("artist");
						String video_title = line_object
								.getString("video_title");
						String description = line_object
								.getString("description");
						String yt_thumb = line_object.getString("yt_thumb");
						int site_views = line_object.getInt("site_views");
						String mp3 = line_object.getString("audio");
						String yt_id = line_object.getString("yt_id");
						VideoItem item = new VideoItem(uniq_id, artist,
								video_title, description, yt_id, yt_thumb,
								site_views);
						related.add(item);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// set properties
		if (type.equals("same_artist")) {
			video.setSameArtist(related);
		} else {
			video.setBestInCategory(related);
		}
		return video;
	}

	public static VideoItem getVideoFullInfo(VideoItem video) {
		final List<Episode> episodes = new ArrayList<Episode>();
		final List<VideoInfo> related = new ArrayList<VideoInfo>();
		final List<Tag> tags = new ArrayList<Tag>();
		String url = "video/" + video.getUniqueId();
		
		// Run GET task to get full info of info
		RestClient.GET(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject result) {
				Log.i("GET FULL VIDEO INFO COMPLETED", result.toString());
				
				JSONArray jArray;
				// Get Episode
				try {
					jArray = result.getJSONArray("tags");
					jArray = result.getJSONArray("episode");
					Log.v("array full video received", "" + jArray.toString());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject line_object = jArray.getJSONObject(i);
						String tag = line_object.getString("tag");
						String safe_tag = line_object.getString("safe_tag");
						Tag item = new Tag(tag, safe_tag);
						tags.add(item);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					jArray = result.getJSONArray("episode");
					Log.v("array received", "" + jArray.toString());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject line_object = jArray.getJSONObject(i);
						String uniq_id = line_object.getString("uniq_id");
						String yt_id = line_object.getString("yt_id");
						String episode = line_object.getString("episode");
						int episode_id = line_object.getInt("episode_id");
						Episode item = new Episode(uniq_id, episode_id, yt_id,
								episode);
						episodes.add(item);
					}
					// Get related
					jArray = result.getJSONArray("related");
					Log.v("array received", "" + jArray.toString());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject line_object = jArray.getJSONObject(i);
						int id = line_object.getInt("id");
						String uniq_id = line_object.getString("uniq_id");
						String artist = line_object.getString("artist");
						String video_title = line_object
								.getString("video_title");
						String description = line_object
								.getString("description");
						String yt_thumb = line_object.getString("yt_thumb");
						int site_views = line_object.getInt("site_views");
						String mp3 = line_object.getString("audio");
						String yt_id = line_object.getString("yt_id");
						VideoItem item = new VideoItem(uniq_id, artist,
								video_title, description, yt_id, yt_thumb,
								site_views);
						related.add(item);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		// set video properties
		video.setEpisode(episodes);
		video.setRelated(related);
		video.setTag(tags);
		return video;
	}

	// get Top, latest, random videos
	@SuppressLint("DefaultLocale")
	public static List<VideoItem> getMainVideos(String url) {
		final List<VideoItem> data = new ArrayList<VideoItem>();
		RestClient.GET(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject result) {
				JSONArray jArray;
				try {
					jArray = result.getJSONArray("videos");
					Log.v("array getMainVideos received", "" + jArray.toString());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject line_object = jArray.getJSONObject(i);
						int id = line_object.getInt("id");
						String uniq_id = line_object.getString("uniq_id");
						String artist = line_object.getString("artist");
						String video_title = line_object
								.getString("video_title");
						String description = line_object
								.getString("description");
						String yt_thumb = line_object.getString("yt_thumb");
						int site_views = line_object.getInt("site_views");
						String mp3 = line_object.getString("audio");
						String yt_id = line_object.getString("yt_id");
						VideoItem item = new VideoItem(uniq_id, artist,
								video_title, description, yt_id, yt_thumb,
								site_views);
						data.add(item);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return data;
	}
	
	@SuppressLint("DefaultLocale")
	public static List<ArtistItem> getArtist(String orderBy, int offSet) {
		final List<ArtistItem> data = new ArrayList<ArtistItem>();
		String url = String.format("artist/%s/%d", orderBy, offSet);
		RestClient.GET(url, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject result) {
				JSONArray jArray;
				try {
					jArray = result.getJSONArray("videos");
					Log.v("array artist received", "" + jArray.toString());
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject line_object = jArray.getJSONObject(i);
						int cnt = line_object.getInt("cnt");
						String artist = line_object.getString("artist");
						ArtistItem item = new ArtistItem(artist, cnt);
						data.add(item);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return data;
	}
}