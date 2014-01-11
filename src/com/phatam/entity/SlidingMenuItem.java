package com.phatam.entity;

import android.view.LayoutInflater;
import android.view.View;

public interface SlidingMenuItem {
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}