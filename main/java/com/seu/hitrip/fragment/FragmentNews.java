package com.seu.hitrip.fragment;

import com.seu.hitrip.card.NewsCard;
import com.seu.hitrip.cose.R;
import com.seu.hitrip.web.WebGetTextTask;
import com.seu.hitrip.web.WebTask;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public int getTitleResourceId() {
        return R.string.fragment_news_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selfView = inflater.inflate(R.layout.fragment_news, container, false);

        final ArrayList<Card> cards = new ArrayList<Card>();

        final Bitmap bitmap_avatar = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
        Bitmap bitmap_pic_1 = BitmapFactory.decodeResource(getResources(), R.drawable.news_pic_1);
        final Bitmap bitmap_pic_2 = BitmapFactory.decodeResource(getResources(), R.drawable.news_pic_2);

        cards.add(NewsCard.getCard(getActivity(), "杨导", "湛蓝的天空","北京",NewsCard.ACTION_PIC,new Date(),bitmap_avatar,bitmap_pic_1));
        cards.add(NewsCard.getCard(getActivity(), "颢神", "茂密的竹林","南京",NewsCard.ACTION_PIC,new Date(),bitmap_avatar,bitmap_pic_2));

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        mCardArrayAdapter.setEnableUndo(true);
        listView = (CardListView) selfView.findViewById(R.id.news_list);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }

        return selfView;
    }
}
