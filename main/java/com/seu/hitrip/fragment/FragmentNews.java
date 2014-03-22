package com.seu.hitrip.fragment;

import com.seu.hitrip.card.NewsCard;
import com.seu.hitrip.cose.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Date;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by yqf on 3/9/14.
 */
public class FragmentNews extends BaseFragment {

    View self = null;
    CardListView listView = null;

    public FragmentNews(){
        super();
    }

    @Override
    public int getTitleResourceId() {
        return R.string.fragment_news_titile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        self = inflater.inflate(R.layout.fragment_news, container, false);

        ArrayList<Card> cards = new ArrayList<Card>();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.avatar);

        cards.add(NewsCard.getCard(getActivity(), "杨导", "呵呵","seu",NewsCard.ACTION_PIC,new Date(),bitmap));
        cards.add(NewsCard.getCard(getActivity(), "颢神", "嘿嘿","seu",NewsCard.ACTION_PIC,new Date(),bitmap));

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        mCardArrayAdapter.setEnableUndo(true);

        listView = (CardListView) self.findViewById(R.id.newsList);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }

        return self;
    }
}
