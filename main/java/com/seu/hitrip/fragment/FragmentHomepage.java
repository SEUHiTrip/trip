package com.seu.hitrip.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.seu.hitrip.card.SuggestedCard;
import com.seu.hitrip.R;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mScrollView = (ScrollView) getActivity().findViewById(R.id.homepage_scrollview);

        initCard();
        initCardSuggested();
        initCircleCard();
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
        card3.addCardHeader(null);
        card3.setShadow(true);

        CardView cardView2 = (CardView) getActivity().findViewById(R.id.homepage_card_Maylike3);
        cardView2.setCard(card3);

    }

    private void initCardSuggested() {
        SuggestedCard card = new SuggestedCard(1, getActivity());
        CardView cardView = (CardView) getActivity().findViewById(R.id.card_suggested1);
        cardView.setCard(card);
        SuggestedCard card2 = new SuggestedCard(2, getActivity());
        CardView cardView2 = (CardView) getActivity().findViewById(R.id.card_suggested2);
        cardView2.setCard(card2);
        SuggestedCard card3 = new SuggestedCard(3, getActivity());
        CardView cardView3 = (CardView) getActivity().findViewById(R.id.card_suggested3);
        cardView3.setCard(card3);
        SuggestedCard card4 = new SuggestedCard(4, getActivity());
        CardView cardView4 = (CardView) getActivity().findViewById(R.id.card_suggested4);
        cardView4.setCard(card4);
        SuggestedCard card5 = new SuggestedCard(5, getActivity());
        CardView cardView5 = (CardView) getActivity().findViewById(R.id.card_suggested5);
        cardView5.setCard(card5);
        SuggestedCard card6 = new SuggestedCard(6, getActivity());
        CardView cardView6 = (CardView) getActivity().findViewById(R.id.card_suggested6);
        cardView6.setCard(card6);
    }

    private void initCircleCard(){

//        Card card = new Card(getActivity());
//        card.setTitle("Title");
//        card.setBackgroundResourceId(R.color.card_background_color1);
//        CardThumbnailCircle thumb = new CardThumbnailCircle(getActivity());
//        card.addCardThumbnail(thumb);
//
//        CardView cardView = (CardView) getActivity().findViewById(R.id.carddemo_circleleft);
//        cardView.setCard(card);

    }

//    public class CardThumbnailCircle extends CardThumbnail {
//
//        public CardThumbnailCircle(Context context) {
//            super(context);
//
//            float density = getContext().getResources().getDisplayMetrics().density;
//            int size = (int) (70*density);
//            setUrlResource("https://plus.google.com/s2/photos/profile/114432517923423045208?sz="+size);
//            setErrorResource(R.drawable.ic_ic_error_loading);
//        }
//
//        @Override
//        public boolean applyBitmap(View imageView, Bitmap bitmap) {
//
//            CircleDrawable circle = new CircleDrawable(bitmap);
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
//                imageView.setBackground(circle);
//            else
//                imageView.setBackgroundDrawable(circle);
//            return true;
//
//        }
//    }
}