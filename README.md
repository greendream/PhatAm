Phat_Am (Phật Âm)
======================================================================================
When you first pull, lets add library "actionbarsherlock" into "library_slidingmenu".
Add library "actionbarsherlock", "library_slidingmenu" and "OpenYoutube library" into "Phat_am"


DONE:
1. GET category:
* http://phatam.com/rest/public/index.php/category
** Should save the id of category in device.


2. GET videos by CATEGORY_ID with ORDER_BY and OFFSET
* http://phatam.com/rest/public/index.php/category/CATEGORY_ID/ORDER_BY/OFFSET
** CATEGORY_ID: get by API 1.
** ORDER_BY:	
		"site_views" - sort by the number of Video view;
		"added" - sort by upload Date;
		"video_title" - sort by the Video title;
		"rating" - sort by the rating;

** OFFSET = page_index * 15 (page_index grown from 0 and will have 15 results per page)


3. GET all artist with ORDER_BY and OFFSET
* http://phatam.com/rest/public/index.php/artist/ORDER_BY/OFFSET
** ORDER_BY:	
		"cnt" - sort by the number of artist's video;
		"artist" - sort by the artist name;
** OFFSET = page_index * 20 (page_index grown from 0 and will have 20 results per page)


4. GET all videos of artist by ARTIST_NAME with ORDER_BY and OFFSET
* http://phatam.com/rest/public/index.php/artist/ARTIST_NAME/ORDER_BY/OFFSET
** ORDER_BY:	
		"site_views" - sort by the number of Video view;
		"added" - sort by upload Date;
		"video_title" - sort by the Video title;
		"rating" - sort by the rating;
** OFFSET = page_index * 15 (page_index grown from 0 and will have 15 results per page)
** Note: if ARTIST_NAME has space char you must replace by %20 anotation.
** Ex: ARTIST_NAME = "Thích Chân Quang" convert to ARTIST_NAME = "Thích%20Chân%20Quang"


5. GET the Top videos
* http://phatam.com/rest/public/index.php/topvideos/0
0 for get top 15 videos

6. GET latest videos
* http://phatam.com/rest/public/index.php/latestvideos/0


7. GET radom videos
* http://phatam.com/rest/public/index.php/randomvideos


8. GET video info by uniq_id
* http://phatam.com/rest/public/index.php/video/29cd5e6e5
** INFO video info detail and include: episode, related, same artist, best_in_category


9. GET search video by KEY_WORD with ORDER_BY and OFFSET
* http://www.phatam.com/rest/public/index.php/search/KEY_WORD/ORDER_BY/OFFSET
** ORDER_BY:
		"site_views" - sort by the number of Video view;
		"added" - sort by upload Date;
		"video_title" - sort by the Video title;
		"rating" - sort by the rating;
** OFFSET = page_index * 15 (page_index grown from 0 and will have 15 results per page)
**Ex: http://www.phatam.com/rest/public/index.php/search/X%C3%A3%20h%E1%BB%99i%20cong%20b%E1%BA%B1ng/added/15


10. GET search artist by ARTIST_NAME with ORDER_BY and OFFSET
*http://www.phatam.com/rest/public/index.php/search_artist/ARTIST_NAME/ORDER_BY/OFFSET
** ARTIST_NAME: your search keyword;
** ORDER_BY:	
		"cnt" - sort by Video name;
		"artist" - sort by the artist name;
** OFFSET = page_index * 20 (page_index grown from 0 and will have 20 results per page)
** Ex: http://www.phatam.com/rest/public/index.php/search_artist/Hoa/cnt/20

11. GET videos by tags
* http://www.phatam.com/rest/public/index.php/......

-----------------------------------------------------------------------------------------
