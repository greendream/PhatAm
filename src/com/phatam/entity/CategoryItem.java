package com.phatam.entity;

public class CategoryItem {
	int id;
	int parent_id;
	String tag;
	String name;
	int position;
	int total_videos;
	
	public CategoryItem() {
		this.id = this.parent_id = this.position = this.total_videos = -1;
		this.tag = this.name = "";
		
	}
	public CategoryItem(int id, int parent_id, String tag, String name,
			int position, int total_videos) {
		this.id = id;
		this.parent_id = parent_id;
		this.tag = tag;
		this.name = name;
		this.position = position;
		this.total_videos = total_videos;
	}

	public int getId() {
		return this.id;
	}

	public int getParentId() {
		return this.parent_id;
	}

	public String getTag() {
		return this.tag;
	}

	public String getName() {
		return this.name;
	}

	public int getPostion() {
		return this.position;
	}

	public int getTotalVideos() {
		return this.total_videos;
	}
}
