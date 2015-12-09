package com.esri.android.viewer.draft;

import java.util.ArrayList;

import android.app.Application;
import android.view.MotionEvent;

public class PathStore{
	public class node{
		public long time;
		public float x;
		public float y;
		public int action;
		public int penWidth;
		public int penStyle;
		public int penColor;
		public static final int ACTION_DOWN=MotionEvent.ACTION_DOWN;
		public static final int ACTION_UP=MotionEvent.ACTION_UP;
		public static final int ACTION_MOVE=MotionEvent.ACTION_MOVE;
		
		public node() {
		}
	}
	private ArrayList<node> tempath;
	
	public PathStore(){
		tempath=new ArrayList<node>();
	}
	public ArrayList<node> getTempPath()
	{
		return tempath;
	}
	public void addNode(node tempnode){
		tempath.add(tempnode);
	}
	public void cleanStore()
	{
		tempath.clear();
	}
}
