package com.seu.hitrip.map;

import java.util.ArrayList;

public class MapObjectContainer 
{
	private ArrayList<MapObjectModel> container;
	
	public MapObjectContainer()
	{
		container = new ArrayList<MapObjectModel>();
	}

	
	public void addObject(MapObjectModel object) 
	{
		container.add(object);
	}
	
	
	public void removeObject(MapObjectModel object)
	{
		container.remove(object);
	}
	
	
	public MapObjectModel getObject(int index)
	{
		return container.get(index);
	}
	
	
	public int size()
	{
		return container.size();
	}

}
