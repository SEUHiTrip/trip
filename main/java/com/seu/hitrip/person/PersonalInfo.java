package com.seu.hitrip.person;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;


import com.ls.widgets.map.model.MapObject;
import com.seu.hitrip.card.NewsCard;
import com.seu.hitrip.cose.R;
import com.seu.hitrip.map.MapObjectModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yqf on 3/31/14.
 */
public class PersonalInfo {

    public static People me;


    public static Resources resources;
    public static List<NewsCard.NewsInfo> personalNews = new ArrayList<NewsCard.NewsInfo>();
    public static Map<Integer,People> peopleMap = new HashMap<Integer, People>();

    public static void init(Resources resources){
        PersonalInfo.resources = resources;

        peopleMap.put(1,new People("颢神",resources.getDrawable(R.drawable.g)));
        peopleMap.put(2,new People("杨导",resources.getDrawable(R.drawable.avatar_small)));

        Bitmap bitmap_pic_1 = BitmapFactory.decodeResource(resources, R.drawable.news_pic_1);
        Bitmap bitmap_pic_2 = BitmapFactory.decodeResource(resources, R.drawable.news_pic_2);

        PersonalInfo.personalNews.add(new NewsCard.NewsInfo(PersonalInfo.peopleMap.get(2), "湛蓝的天空","北京",NewsCard.NewsInfo.ACTION_PIC,new Date(1396241179646l),bitmap_pic_1));
        PersonalInfo.personalNews.add(new NewsCard.NewsInfo(PersonalInfo.peopleMap.get(1), "茂密的竹林","南京",NewsCard.NewsInfo.ACTION_PIC,new Date(1396241353341l),bitmap_pic_2));

        me = peopleMap.get(1); // todo
    }

    public static void addNewsInfo(String msg, Bitmap pic){
        personalNews.add(new NewsCard.NewsInfo(me ,msg,"", NewsCard.NewsInfo.ACTION_PIC,new Date(),pic));
    }

    public static class People{
        public String name;
        public Drawable avatar;
        public Location currentLocation;
        public MapObject mapObject;
        public People(String name, Drawable avatar){
            this.name = name;
            this.avatar = avatar;
            currentLocation = new Location("server");
        }
    }
}
