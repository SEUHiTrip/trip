package com.seu.hitrip.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chris.lr.slidemenu.R;

import java.util.ArrayList;


import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.CardView;


public class FragmentHomepage extends BaseFragment {

    View self = null;

    @Override
    public int getTitleResourceId() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
        self = inflater.inflate(R.layout.fragment_homepage, container, false);

        ArrayList<Card> cards = new ArrayList<Card>();

        for(int i = 0; i < 3; i++)
            cards.add(getCard());

        //CardView cardView = (CardView) self.findViewById(R.id.carddemo);

        //cardView.setCard(getCard());

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        mCardArrayAdapter.setEnableUndo(true);
        CardListView listView = (CardListView) self.findViewById(R.id.myList);

        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }

        return self;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public Card getCard() {
        //Create a Card
        Card card = new Card(getActivity(), R.layout.card_inner_content);

        //Create a CardHeader
        CardHeader header = new CardHeader(getActivity());

        header.setTitle("title");

        header.setButtonExpandVisible(true);

        header.setPopupMenu(R.menu.main, new CardHeader.OnClickCardHeaderPopupMenuListener(){
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                Toast.makeText(getActivity(), "Click on "+item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        //Add Header to card
        card.addCardHeader(header);

        card.setTitle("My Title");

        //Enable a swipe action
        card.setSwipeable(true);

        //Set onClick listener
        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getActivity(),"Clickable card", Toast.LENGTH_LONG).show();

                card.setTitle("New Title");

                // CardView cardView = (CardView) self.findViewById(R.id.carddemo);
                ((CardView)view).refreshCard(card);
            }
        });

        //This provide a simple (and useless) expand area
        CardExpand expand = new CardExpand(getActivity());

        //Set inner title in Expand Area
        expand.setTitle("expend");

        //Add expand to a card
        card.addCardExpand(expand);

        card.setExpanded(true);

        card.setShadow(true);

        return card;
    }


}