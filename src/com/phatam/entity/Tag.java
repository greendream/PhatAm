package com.phatam.entity;

public class Tag {
	String tag;
	String safe_tag;

	public Tag(String tag, String safe_tag) {
		this.tag = tag;
		this.safe_tag = safe_tag;
	}
	public String getTag(){
		return this.tag;
	}
	public String getSafeTag(){
		return this.safe_tag;
	}
}