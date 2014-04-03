package com.seu.hitrip.fragment;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.seu.hitrip.card.NewsCard;
import com.seu.hitrip.R;
import com.seu.hitrip.person.PersonalInfo;
import com.seu.hitrip.web.WebGetFileTask;
import com.seu.hitrip.web.WebGetTextTask;
import com.seu.hitrip.web.WebTask;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by yqf on 3/9/14.
 */
public class FragmentNews extends BaseFragment {

    View selfView = null;
    CardListView listView = null;
    BootstrapButton refreshBtn = null;

    @Override
    public int getTitleResourceId() {
        return R.string.fragment_news_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selfView = inflater.inflate(R.layout.fragment_news, container, false);

        refreshBtn = (BootstrapButton) selfView.findViewById(R.id.news_refresh);

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshNews();
            }
        });

        final ArrayList<Card> cards = new ArrayList<Card>();


        for(NewsCard.NewsInfo info : PersonalInfo.personalNewsSet ){
            cards.add(NewsCard.getCard(getActivity(), info));
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        mCardArrayAdapter.setEnableUndo(true);
        listView = (CardListView) selfView.findViewById(R.id.news_list);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }

        return selfView;
    }

    public void refreshNews(){
        WebTask task = new WebGetTextTask(WebTask.SERVER_ADDRESS,"/i/news/feed/list",null) {
            @Override
            protected void onPostExecute(Object o) {
                String string = (String)o;
                Log.i("web",string);
                try {
                    JSONObject object = new JSONObject(string);
                    JSONArray array = object.getJSONArray("list");
                    if(array == null) {
                        Log.e("web","array is null");
                        return;
                    }

                    for (int i = 0; i<array.length();i++){
                        JSONObject record = array.getJSONObject(i);
                        if(record == null) {
                            Log.e("web","record is null");
                            return;
                        }
                        final Date postTime = new Date( new Long(record.getString("post_time")));
                        final int actionType = new Integer(record.getString("action_type"));
                        final int userId = new Integer(record.getString("user_id"));
                        final String location = record.getString("location");
                        final String msg = record.getString("msg");
                        JSONObject pic = record.getJSONObject("pic");

                        String path = pic.getString("path");

                        WebTask task_pic = new WebGetFileTask(WebTask.SERVER_ADDRESS,"/images/"+path,null){
                            @Override
                            protected void onPostExecute(Object o) {
                                Bitmap bitmap = (Bitmap) o;
                                Log.i("web","pic"+o.toString());

                                addNewsToSet(userId,msg,postTime,actionType,location,bitmap);

                            }
                        };
                        task_pic.execute(WebGetTextTask.PIC_BITMAP);

                    }

                } catch (JSONException e) {
                    Log.e("web",e.toString());
                    e.printStackTrace();
                }
            }
        };
        task.execute();
    }

    public void addNewsToSet(int userId,String msg,Date postTime,int actionType,String location,Bitmap bitmap){
        PersonalInfo.People people = PersonalInfo.peopleMap.get(userId);
        if(people == null) return;

        NewsCard.NewsInfo info = new NewsCard.NewsInfo(people,msg,location,actionType,postTime,bitmap);

        if(PersonalInfo.personalNewsSet.contains(info)) return;

        PersonalInfo.personalNewsSet.add(info);

        final ArrayList<Card> cards = new ArrayList<Card>();


        for(NewsCard.NewsInfo i : PersonalInfo.personalNewsSet ){
            cards.add(NewsCard.getCard(getActivity(), i));
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        mCardArrayAdapter.setEnableUndo(true);
        listView = (CardListView) selfView.findViewById(R.id.news_list);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
    }
}
