package com.seu.hitrip.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.seu.hitrip.until.R;
import com.seu.hitrip.card.MayLikeCard;

import it.gmariotti.cardslib.library.view.CardView;


public class FragmentHomepage extends BaseFragment {

    protected ScrollView mScrollView;

    public int getTitleResourceId() {
        return R.string.fragment_recommend_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mScrollView = (ScrollView) getActivity().findViewById(R.id.homepage_scrollview);

        initCard();
    }

    public void initCard() {

        //Create a Card
        MayLikeCard card = new MayLikeCard(getActivity());
        card.setShadow(false);

        //Set card in the cardView
        CardView cardView = (CardView) getActivity().findViewById(R.id.homepage_card_Maylike1);
        cardView.setCard(card);

        MayLikeCard card2 = new MayLikeCard(1, getActivity());
        card2.addCardHeader(null);
        card2.setShadow(false);

        CardView cardView1 = (CardView) getActivity().findViewById(R.id.homepage_card_Maylike2);
        cardView1.setCard(card2);

        MayLikeCard card3 = new MayLikeCard(2, getActivity());
        card2.addCardHeader(null);
        card2.setShadow(true);

        CardView cardView2 = (CardView) getActivity().findViewById(R.id.homepage_card_Maylike3);
        cardView2.setCard(card3);

    }

    private void initCardSuggested() {

    }

}