package com.seu.hitrip.map;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;

public class MapObjectModel 
{
	private int x;
	private int y;
	private String caption;
	private Location location;
	private Drawable pic;
	
	public MapObjectModel(Location location, String caption, Drawable pic)
	{
		this.location = location;
		this.caption = caption;
		this.pic = pic;
	}
	
	public MapObjectModel(int x, int y, String caption, Drawable pic)
	{
		this.x = x;
		this.y = y;
		this.caption = caption;
		this.pic=pic;
	}
	
	public int getX() 
	{
		return x;
	}


	public int getY() 
	{
		return y;
	}
	
	
	public Location getLocation()
	{
		return location;
	}
	
	
	public String getCaption()
	{
		return caption;
	}

    public Drawable getPic() { return pic; }
}
