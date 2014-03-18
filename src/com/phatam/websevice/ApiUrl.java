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

public class ApiUrl {
	
	public static final String SERVER_URL 				 	= "http://phatam.com/rest/public/index.php/";
	
	public static final String ORDER_BY_SITE_VIEW 		 	= "site_views";	// sort by the number of Video view.
	public static final String ORDER_BY_UPLOAD_DATE		 	= "added";		// sort by upload Date.
	public static final String ORDER_BY_SITE_VIDEO_TITLE 	= "title";		// sort by the Video title.
	public static final String ORDER_BY_VIDEO_RATING 	 	= "rating";		// sort by the Rating.
	public static final String ORDER_BY_ARTIST_VIDEO_COUNT	= "cnt";		// sort by Video name.
	public static final String ORDER_BY_ARTIST_NAME 	 	= "artist";		// sort by the artist name.

	public static final int VIDEO_PAGE_SIZE = 15;
	public static final int ARTIST_PAGE_SIZE = 20;

	/**
	 * 1. Get the URL of method get category ID
	 * 
	 * @return Destination API URL
	 */
	public static String getCategoryIdUrl() {
		return SERVER_URL + "category";
	}	
	
	/**
	 * 2. Get the URL of method get videos by CATEGORY_ID with ORDER_BY and OFFSET
	 *  
	 * @param CATEGORY_ID : get by the API 1 - getCategoryIdUrl()
	 * 
	 * @param ORDER_BY : sort method
	 * 
	 * @param OFFSET : page_index * 15 (page_index grown from 0 and will have 15 results per page)
	 * 
	 * @return Destination API URL
	 */
	public static final String getVideoInCategoryUrl(String CATEGORY_ID, String ORDER_BY, int OFFSET) {
		// when you don't know the ORDER_BY
		if ("".equals(ORDER_BY)) {
			return SERVER_URL + "category/" + CATEGORY_ID + "/";
		}
				
		// when you don't know the OFFSET
		if (OFFSET < 0) {
			return SERVER_URL + "category/" + CATEGORY_ID + "/" + ORDER_BY + "/";
		}
				
		// when you know every parameter
		return SERVER_URL + "category/" + CATEGORY_ID + "/" + ORDER_BY + "/" + OFFSET;
	}
	
	/**
	 * 3. Get the URL of method get all artist name with ORDER_BY and OFFSET
	 *  
	 * @param ORDER_BY : sort method
	 * 
	 * @param OFFSET : page_index * 20 (page_index grown from 0 and will have 20 results per page)
	 * 
	 * @return Destination API URL
	 */
	public static final String getAllArtistNameUrl(String ORDER_BY, int OFFSET) {		
		// when you don't know the OFFSET
		if (OFFSET < 0) {
			return SERVER_URL + "artist/" + ORDER_BY + "/";
		}
				
		// when you know every parameter		
		return SERVER_URL + "artist/" + ORDER_BY + "/" + OFFSET;
	}
	
	
	/**
	 * 4. Get the URL of method get all videos of artist by ARTIST_NAME with ORDER_BY and OFFSET
	 *  
	 * @param ORDER_BY : sort method
	 *        
	 * @param OFFSET : page_index * 15 (page_index grown from 0 and will have 15 results per page)
	 * 
	 * @Note: if ARTIST_NAME has space char you must replace by %20 annotation.
	 * 
	 * @Ex: ARTIST_NAME = "Thích Chân Quang" convert to ARTIST_NAME = "Thích%20Chân%20Quang"
	 *  
	 * @return Destination API URL
	 */
	public static final String getAllVideoOfArtistUrl(String ARTIST_NAME, String ORDER_BY, int OFFSET) {
		// when you don't know the ORDER_BY
		if ("".equals(ORDER_BY)) {
			return SERVER_URL + "artist/" + ARTIST_NAME.replace(" ", "%20") + "/";
		}
				
		// when you don't know the OFFSET
		if (OFFSET < 0) {
			return SERVER_URL + "artist/" + ARTIST_NAME.replace(" ", "%20") + "/" + ORDER_BY + "/";
		}
				
		// when you know every parameter
		return SERVER_URL + "artist/" + ARTIST_NAME.replace(" ", "%20") + "/" + ORDER_BY + "/" + OFFSET;
	}
	
	
	/**
	 * 5. Get the URL of method get Top videos list
	 *                
	 * @param OFFSET : page_index * 15 (page_index grown from 0 and will have 15 results per page)
	 *  
	 * @return Destination API URL
	 */
	public static final String getTopVideoUrl(String ORDER_BY, int OFFSET) {
		
		if (OFFSET < 0) {			
//			return SERVER_URL + "topvideos/" + ORDER_BY + "/";
		}
		
//		return SERVER_URL + "topvideos/" + ORDER_BY + "/" + OFFSET;
		return "http://phatam.com/rest/public/index.php/topvideos/";
	}
	
	/**
	 * 6. Get the URL of method get the Latest videos
	 *                
	 * @param OFFSET : page_index * 15 (page_index grown from 0 and will have 15 results per page)
	 *  
	 * @return Destination API URL
	 */
	public static final String getNewVideoUrl(String ORDER_BY, int OFFSET) {
		if (OFFSET < 0) {
//			return SERVER_URL + "latestvideos/" + ORDER_BY + "/";
		}
		
//		return SERVER_URL + "latestvideos/" + ORDER_BY + "/" + OFFSET;

		return "http://phatam.com/rest/public/index.php/latestvideos/";
	}
	
	/**
	 * 7. Get the URL of method get Random videos
	 *                
	 * @param OFFSET : page_index * 15 (page_index grown from 0 and will have 15 results per page)
	 *  
	 * @return Destination API URL
	 */
	public static final String getRandomVideoUrl(String ORDER_BY, int OFFSET) {
		if (OFFSET < 0) {
//			return SERVER_URL + "randomvideos/" + ORDER_BY + "/";
		}
		
//		return SERVER_URL + "randomvideos/" + ORDER_BY + "/" + OFFSET;
		return "http://phatam.com/rest/public/index.php/randomvideos";
	}
	
	/**
	 * 8. Get the URL of method get video info by UNIQUE_ID
	 *                
	 * @param UNIQUE_ID : the unig_id of video inside system
	 *  
	 * @return Destination API URL
	 */
	public static final String getVideoInfoUrl(String UNIQUE_ID) {
		return SERVER_URL + "video/" + "/" + UNIQUE_ID;
	}
	
	/**
	 * 9. Get the URL of method get Search video by KEY_WORD with ORDER_BY and OFFSET
	 *   
	 * @param KEY_WORD : the user query word
	 * 
	 * @param ORDER_BY : sort method
	 *               
	 * @param OFFSET : page_index * 15 (page_index grown from 0 and will have 15 results per page)
	 * 
	 * @return Destination API URL
	 */
	public static final String getSearchVideoUrl(String KEY_WORD, String ORDER_BY, int OFFSET) {
		// when you don't know the ORDER_BY
		if ("".equals(ORDER_BY)) {
			return SERVER_URL + "search/" + KEY_WORD + "/";
		}
				
		// when you don't know the OFFSET
		if (OFFSET < 0) {
			return SERVER_URL + "search/" + KEY_WORD + "/" + ORDER_BY + "/";
		}
				
		// when you know every parameter
		return SERVER_URL + "search/" + KEY_WORD + "/" + ORDER_BY + "/" + OFFSET;
	}

	
	/**
	 * 10. GET search artist by ARTIST_NAME with ORDER_BY and OFFSET
	 *  
	 * @param ARTIST_NAME: the artist name
	 * 
	 * @param ORDER_BY : sort method
	 *                             
	 * @param OFFSET : page_index * 20 (page_index grown from 0 and will have 20 results per page)
	 * 
	 * @return Destination API URL
	 */
	public static final String getSerchArtistNameUrl(String ARTIST_NAME, String ORDER_BY, int OFFSET) {
		
		// when you don't know the ORDER_BY
		if ("".equals(ORDER_BY)) {
			return SERVER_URL + "search_artist/" + ARTIST_NAME + "/";
		}
		
		// when you don't know the OFFSET
		if (OFFSET < 0) {
			return SERVER_URL + "search_artist/" + ARTIST_NAME + "/" + ORDER_BY + "/";
		}
		
		// when you know every parameter
		return SERVER_URL + "search_artist/" + ARTIST_NAME + "/" + ORDER_BY + "/" + OFFSET;
	}
	
	
}
