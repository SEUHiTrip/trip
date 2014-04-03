package com.seu.hitrip.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seu.hitrip.card.MyRecordCard;
import com.seu.hitrip.card.NewsCard;
import com.seu.hitrip.R;
import com.seu.hitrip.person.PersonalInfo;

import java.util.ArrayList;
import java.util.Date;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by yqf on 3/23/14.
 */
public class FragmentMyRecord extends BaseFragment{

    private View selfView = null;
    //private CardView myRecordCardView;
    private CardListView myNewsListView;

    @Override
    public int getTitleResourceId() {
        return R.string.fragment_my_record_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selfView = inflater.inflate(R.layout.fragment_my_record, container, false);

        Bitmap bitmap_avatar = ((BitmapDrawable)PersonalInfo.me.avatar).getBitmap();
        Bitmap bitmap_pic_1 = BitmapFactory.decodeResource(getResources(), R.drawable.news_pic_1);
        Bitmap bitmap_pic_2 = BitmapFactory.decodeResource(getResources(), R.drawable.news_pic_2);

        //myRecordCardView = (CardView) selfView.findViewById(R.id.my_record_card);
        myNewsListView = (CardListView) selfView.findViewById(R.id.my_news_list);

        Card myRecordCard = new MyRecordCard(getActivity(), R.layout.card_layout,bitmap_pic_1,bitmap_avatar);
        myRecordCard.setInnerLayout(R.layout.card_my_record_inner_layout);
        // myRecordCardView.setCard(myRecordCard);

        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(myRecordCard);

        for(NewsCard.NewsInfo info : PersonalInfo.personalNewsSet ){
            if(info.people == PersonalInfo.me)
                cards.add(NewsCard.getCard(getActivity(), info));
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);

        myNewsListView = (CardListView) selfView.findViewById(R.id.my_news_list);
        if (myNewsListView!=null){

            myNewsListView.setAdapter(mCardArrayAdapter);
        }

        return selfView;
    }
}
