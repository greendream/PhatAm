package com.phatam.sqlite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.phatam.entity.VideoInfo;
import com.phatam.entity.VideoItem;

public class PhatamSqliteHelper extends SQLiteOpenHelper {
	public static final String TABLE_VIDEO = "video";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_UNIQ = "uniq_id";
	public static final String COLUMN_ARTIST = "artist";
	public static final String COLUMN_VIDEOTITLE = "video_title";
	public static final String COLUMN_DESC = "description";
	public static final String COLUMN_YT_ID = "yt_id";
	public static final String COLUMN_YT_THUMB = "yt_thumb";
	public static final String COLUMN_SITE_VIEW = "site_views";
	public static final String COLUMN_MP3 = "mp3";

	public static final String TABLE_TOP_VIDEOS = "topvideos";
	public static final String DATE_ADDED = "date_added";
	public static final String VIDEO_ID = "uniq_id";

	public static final String TABLE_LATEST_VIDEOS = "latestvideos";

	private static final String DATABASE_NAME = "phatam.db";
	private static final int DATABASE_VERSION = 1;

	private static final String CREATE_TABLE_VIDEO = "create table "
			+ TABLE_VIDEO + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + DATE_ADDED
			+ " DATETIME," + COLUMN_ARTIST + " text not null,"
			+ COLUMN_VIDEOTITLE + " text not null," + COLUMN_DESC
			+ " text not null," + COLUMN_YT_ID + " text not null,"
			+ COLUMN_YT_THUMB + " text not null," + COLUMN_SITE_VIEW
			+ "integer," + COLUMN_MP3 + " text not null)";
	private static final String CREATE_TOP_VIDEOS = "create table "
			+ TABLE_TOP_VIDEOS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + DATE_ADDED
			+ " DATETIME," + VIDEO_ID + " text not null)";
	private static final String CREATE_LATEST_VIDEOS = "create table "
			+ TABLE_LATEST_VIDEOS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + DATE_ADDED
			+ " DATETIME," + VIDEO_ID + " text not null)";
	private static final String LOG = null;
	private static final String COLUMN_CATEGORY = "category";

	public PhatamSqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
		db.execSQL(CREATE_TABLE_VIDEO);
		db.execSQL(CREATE_TOP_VIDEOS);
		db.execSQL(CREATE_LATEST_VIDEOS);
	}

	// closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
    
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOP_VIDEOS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LATEST_VIDEOS);

		// create new tables
		onCreate(db);
	}

	/*
	 * Creating video
	 */
	public long createVideo(VideoInfo video) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("uniq_id", video.getUniqueId());
		values.put("artist", video.getArtist());
		values.put("video_title", video.getVideoTitle());
		values.put("description", video.getDescription());
		values.put("yt_id", video.getYoutubeId());
		values.put("yt_thumb", video.getYoutubeImage());
		values.put("site_views", video.getSiteView());
		values.put("mp3", video.getMp3());

		// insert row
		long video_id = db.insert(TABLE_VIDEO, null, values);

		return video_id;
	}

	public long insertTopVideos(String uniq_id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("uniq_id", uniq_id);
		values.put(DATE_ADDED, getDateTime());
		// insert row
		long video_id = db.insert(TABLE_TOP_VIDEOS, null, values);

		return video_id;
	}

	public long insertLatestVideos(String uniq_id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("uniq_id", uniq_id);
		values.put(DATE_ADDED, getDateTime());
		// insert row
		long video_id = db.insert(TABLE_LATEST_VIDEOS, null, values);

		return video_id;
	}

	public VideoItem getVideoItem(long video_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_VIDEO + " WHERE "
				+ COLUMN_ID + " = " + video_id;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		VideoItem td = new VideoItem();
		td.setVideoId(c.getString(c.getColumnIndex(COLUMN_UNIQ)));
		td.setVideoTitle(c.getString(c.getColumnIndex(COLUMN_VIDEOTITLE)));
		td.setMp3(c.getString(c.getColumnIndex(COLUMN_MP3)));
		td.setDesc(c.getString(c.getColumnIndex(COLUMN_DESC)));
		td.setSiteView(c.getInt(c.getColumnIndex(COLUMN_SITE_VIEW)));
		td.setYoutubeId(c.getString(c.getColumnIndex(COLUMN_YT_ID)));
		td.setYoutubeImage(c.getString(c.getColumnIndex(COLUMN_YT_THUMB)));
		return td;
	}

	public List<VideoItem> getAllVideos() {
		List<VideoItem> videos = new ArrayList<VideoItem>();
		String selectQuery = "SELECT  * FROM " + TABLE_VIDEO;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				VideoItem td = new VideoItem();
				td.setVideoId(c.getString(c.getColumnIndex(COLUMN_UNIQ)));
				td.setVideoTitle(c.getString(c
						.getColumnIndex(COLUMN_VIDEOTITLE)));
				td.setMp3(c.getString(c.getColumnIndex(COLUMN_MP3)));
				td.setDesc(c.getString(c.getColumnIndex(COLUMN_DESC)));
				td.setSiteView(c.getInt(c.getColumnIndex(COLUMN_SITE_VIEW)));
				td.setYoutubeId(c.getString(c.getColumnIndex(COLUMN_YT_ID)));
				td.setYoutubeImage(c.getString(c
						.getColumnIndex(COLUMN_YT_THUMB)));
				td.setCategory(c.getString(c.getColumnIndex(COLUMN_CATEGORY)));
				// adding to video list
				videos.add(td);
			} while (c.moveToNext());
		}

		return videos;
	}

	public List<VideoItem> getArtistVideos(String artist) {
		List<VideoItem> videos = new ArrayList<VideoItem>();
		String selectQuery = "SELECT  * FROM " + TABLE_VIDEO + "WHERE artist ="
				+ artist;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				VideoItem td = new VideoItem();
				td.setVideoId(c.getString(c.getColumnIndex(COLUMN_UNIQ)));
				td.setVideoTitle(c.getString(c
						.getColumnIndex(COLUMN_VIDEOTITLE)));
				td.setMp3(c.getString(c.getColumnIndex(COLUMN_MP3)));
				td.setDesc(c.getString(c.getColumnIndex(COLUMN_DESC)));
				td.setSiteView(c.getInt(c.getColumnIndex(COLUMN_SITE_VIEW)));
				td.setYoutubeId(c.getString(c.getColumnIndex(COLUMN_YT_ID)));
				td.setYoutubeImage(c.getString(c
						.getColumnIndex(COLUMN_YT_THUMB)));
				td.setCategory(c.getString(c.getColumnIndex(COLUMN_CATEGORY)));

				// adding to video list
				videos.add(td);
			} while (c.moveToNext());
		}

		return videos;
	}

	public List<VideoItem> getCategoryVideos(String category) {
		List<VideoItem> videos = new ArrayList<VideoItem>();
		String selectQuery = "SELECT  * FROM " + TABLE_VIDEO
				+ "WHERE category =" + category;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				VideoItem td = new VideoItem();
				td.setVideoId(c.getString(c.getColumnIndex(COLUMN_UNIQ)));
				td.setVideoTitle(c.getString(c
						.getColumnIndex(COLUMN_VIDEOTITLE)));
				td.setMp3(c.getString(c.getColumnIndex(COLUMN_MP3)));
				td.setDesc(c.getString(c.getColumnIndex(COLUMN_DESC)));
				td.setSiteView(c.getInt(c.getColumnIndex(COLUMN_SITE_VIEW)));
				td.setYoutubeId(c.getString(c.getColumnIndex(COLUMN_YT_ID)));
				td.setYoutubeImage(c.getString(c
						.getColumnIndex(COLUMN_YT_THUMB)));
				td.setCategory(c.getString(c.getColumnIndex(COLUMN_CATEGORY)));

				// adding to video list
				videos.add(td);
			} while (c.moveToNext());
		}

		return videos;
	}
	/*
	 * Updating a video
	 */
//	public int updateVideo(VideoItem video) {
//	    SQLiteDatabase db = this.getWritableDatabase();
//	 
//	    ContentValues values = new ContentValues();
//	    values.put(, todo.getNote());
//	    values.put(KEY_STATUS, todo.getStatus());
//	 
//	    // updating row
//	    return db.update(TABLE_TODO, values, KEY_ID + " = ?",
//	            new String[] { String.valueOf(todo.getId()) });
//	}
	@SuppressLint("SimpleDateFormat")
	private String getDateTime() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(Calendar.getInstance().getTime());
		return timeStamp;
	}

}
