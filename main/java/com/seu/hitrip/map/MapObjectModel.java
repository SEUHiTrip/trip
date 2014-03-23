package com.seu.hitrip.map;

import android.location.Location;

public class MapObjectModel 
{
	private int x;
	private int y;
	private int id;
	private String caption;
	private Location location;
	private int mp3;
	
	public MapObjectModel(int id, Location location, String caption,int mp3)
	{
		this.location = location;
		this.caption = caption;
		this.id = id;
		this.mp3=mp3;
	}
	
	public MapObjectModel(int id, int x, int y, String caption,int mp3)
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.caption = caption;
		this.mp3=mp3;
	}

	public int getId() 
	{
		return id;
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
	
	public int getMp3() {
		return mp3;
	}

}
