Phat_am
=======
When you first pull, lets add library "actionbarsherlock" into "library_slidingmenu".
Add library "actionbarsherlock" and "library_slidingmenu" into "Phat_am"



DONE:
1. GET category:
* http://phatam.com/rest/public/index.php/category
*{"error":false,"categories":[{"id":1,"parent_id":0,"tag":"phat-phap-nhiem-mau","name":"Ph\u1eadt ph\u00e1p nhi\u1ec7m m\u00e0u","total_videos":39,"position":9},{"id":2,"parent_id":0,"tag":"phim-truyen","name":"Phim truy\u1ec7n","total_videos":75,"position":7},{"id":3,"parent_id":0,"tag":"phim-tai-lieu","name":"Phim t\u00e0i li\u1ec7u","total_videos":816,"position":8},{"id":4,"parent_id":0,"tag":"tan-nhac-phat-giao","name":"T\u00e2n nh\u1ea1c Ph\u1eadt gi\u00e1o","total_videos":948,"position":4},{"id":5,"parent_id":0,"tag":"co-nhac-phat-giao","name":"C\u1ed5 nh\u1ea1c Ph\u1eadt gi\u00e1o","total_videos":114,"position":5},{"id":6,"parent_id":0,"tag":"nhac-thien","name":"Nh\u1ea1c Thi\u1ec1n","total_videos":143,"position":6},{"id":7,"parent_id":0,"tag":"anh-sang-phat-phap","name":"\u00c1nh s\u00e1ng Ph\u1eadt ph\u00e1p","total_videos":42,"position":10},{"id":500,"parent_id":0,"tag":"kinh-chu","name":"Kinh - Ch\u00fa","total_videos":634,"position":1},{"id":501,"parent_id":0,"tag":"phap-thoai","name":"Ph\u00e1p tho\u1ea1i","total_videos":9875,"position":2},{"id":502,"parent_id":0,"tag":"sach-noi","name":"S\u00e1ch n\u00f3i","total_videos":651,"position":3},{"id":503,"parent_id":0,"tag":"mot-ngay-an-lac","name":"M\u1ed9t ng\u00e0y an l\u1ea1c","total_videos":378,"position":11},{"id":504,"parent_id":2,"tag":"phim-hoat-hinh","name":"Phim ho\u1ea1t h\u00ecnh","total_videos":199,"position":1}]}
** Should save the id of category in device.

2. GET videos by category id
*http://phatam.com/rest/public/index.php/category/501

3. GET videos by category id with orderBy and offset (page)
* http://phatam.com/rest/public/index.php/category/501/added/15
** Available orderBy is video_title - Name, site_views - View Count, added - Date, rating - Rate, title - Title
** offset = page * video_per_page

4. GET all artist with orderBy and offset
* http://phatam.com/rest/public/index.php/artist/cnt/0
* http://phatam.com/rest/public/index.php/artist/artist/0
** Available orderBy in ['cnt', 'artist'] offset like above


5. GET videos by artist by name, orderBy and offset
* http://phatam.com/rest/public/index.php/artist/Th%C3%ADch%20%C4%90%E1%BB%A9c%20Ni%E1%BB%87m/added/0
** Name include unicode like "Thích Đức Niệm", Available orderBy is video_title - Name, site_views - View Count, added - Date, rating - Rate, title - Title

6. GET Top videos
http://phatam.com/rest/public/index.php/topvideos

7. GET latest videos
http://phatam.com/rest/public/index.php/latestvideos

8. GET radom videos
http://phatam.com/rest/public/index.php/randomvideos

8. GET video by uniq_id
*   http://phatam.com/rest/public/index.php/video/29cd5e6e5
** INFO include episode, related, same artist, best_in_category

9. GET videos by tags

