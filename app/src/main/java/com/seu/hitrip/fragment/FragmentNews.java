package com.seu.hitrip.fragment;

import com.chris.lr.slidemenu.R;

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
public class FragmentNews extends Fragment {

    View self = null;
    CardListView listView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        self = inflater.inflate(R.layout.fragment_news, container, false);

        ArrayList<Card> cards = new ArrayList<Card>();

        cards.add(newsTextCard.getCard(getActivity(), "颢神", "我在药科大"));
        cards.add(newsTextCard.getCard(getActivity(), "颢神", "我在北大"));
        cards.add(newsTextCard.getCard(getActivity(), "颢神", "我在清华"));

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        mCardArrayAdapter.setEnableUndo(true);

        listView = (CardListView) self.findViewById(R.id.newsList);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }

        return self;
    }


    public static class newsTextCard extends Card {


        private final String people;
        private final String content;

        public static Card getCard(final Context context, String people, String content){
            newsTextCard card = new newsTextCard(context, people, content);

            //Create a CardHeader
            CardHeader header = new CardHeader(context);

            header.setButtonExpandVisible(true);

            header.setPopupMenu(R.menu.main, new CardHeader.OnClickCardHeaderPopupMenuListener(){
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
                    Toast.makeText(context, "Click on " + item.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });

            //Add Header to card
            card.addCardHeader(header);

            //Set onClick listener
            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Toast.makeText(context,"Clickable card", Toast.LENGTH_LONG).show();

                    card.setTitle("New Title");

                    // CardView cardView = (CardView) self.findViewById(R.id.carddemo);
                    ((CardView)view).refreshCard(card);
                }
            });

            card.setInnerLayout(R.layout.card_inner_news_text);

            //This provide a simple (and useless) expand area
            CardExpand expand = new CardExpand(context);

            //Set inner title in Expand Area
            expand.setTitle("expend");

            //Add expand to a card
            card.addCardExpand(expand);

            card.setExpanded(true);

            card.setShadow(true);

            return card;
        }


        private newsTextCard(Context context, String people, String content) {
            super(context, R.layout.card_thumbnail_layout);
            this.people = people;
            this.content = content;
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {

            TextView newsTextView = (TextView) parent.findViewById(R.id.newsText);

            newsTextView.setText(people);
            newsTextView.setText(content);

            super.setupInnerViewElements(parent, view);
        }

    }
}
