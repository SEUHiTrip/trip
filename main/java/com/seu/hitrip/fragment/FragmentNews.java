package com.seu.hitrip.fragment;

import com.seu.hitrip.until.R;

import com.seu.hitrip.card.newsTextCard;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;

import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;


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

        cards.add(newsTextCard.getCard(getActivity(), "杨导", "我在"));
        cards.add(newsTextCard.getCard(getActivity(), "颢神", "我在"));
        cards.add(newsTextCard.getCard(getActivity(), "颢神", "我在"));

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        mCardArrayAdapter.setEnableUndo(true);


        listView = (CardListView) self.findViewById(R.id.newsList);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }

        return self;
    }
}
