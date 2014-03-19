package com.seu.hitrip.card;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chris.lr.slidemenu.R;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by yqf on 3/17/14.
 */
public class newsTextCard extends Card {

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

        TextView username = (TextView) parent.findViewById(R.id.inner_news_user_name);
        TextView msg = (TextView) parent.findViewById(R.id.news_inner_msg);

        username.setText(people);
        msg.setText(content);

        super.setupInnerViewElements(parent, view);
    }
}
