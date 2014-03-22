package com.seu.hitrip.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seu.hitrip.cose.R;


import java.util.Date;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by yqf on 3/17/14.
 */
public class NewsCard extends Card {

    public static String[] ACTIONS = {
        "途中 拍照",
        "途中 说",
    };

    public static int ACTION_PIC = 0;
    public static int ACTION_TEXT = 1;

    private final String people;
    private final String msg;
    private final String location;
    private final int action_type;
    private final Date post_time;
    private final Bitmap avatar;

    public static Card getCard(final Context context,
                               String people,
                               String content,
                               String location,
                               int action_type,
                               Date post_time,
                               Bitmap avater){

        NewsCard card = new NewsCard(context, people, content,location,action_type,post_time,avater);

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

        card.setInnerLayout(R.layout.card_news_inner_content);

        card.setShadow(true);

        return card;
    }


    private NewsCard(final Context context,
                     String people,
                     String content,
                     String location,
                     int action_type,
                     Date post_time,
                     Bitmap avatar){
        super(context, R.layout.card_thumbnail_layout);
        this.people = people;
        this.msg = content;
        this.location = location;
        this.action_type = action_type;
        this.post_time = post_time;
        this.avatar = avatar;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView username_view = (TextView) parent.findViewById(R.id.card_news_inner_user_name);
        TextView msg_view = (TextView) parent.findViewById(R.id.card_news_inner_msg);
        TextView location_view = (TextView) parent.findViewById(R.id.card_news_inner_location);
        TextView action_type_view = (TextView) parent.findViewById(R.id.card_news_inner_action_type);
        TextView post_time_view = (TextView) parent.findViewById(R.id.card_news_inner_post_time);
        ImageView avatar_view = (ImageView) parent.findViewById(R.id.card_news_inner_avatar);

        username_view.setText(people);
        msg_view.setText(msg);
        location_view.setText(location);
        action_type_view.setText(ACTIONS[action_type]);
        post_time_view.setText(post_time.toString());
        avatar_view.setImageBitmap(avatar);

        super.setupInnerViewElements(parent, view);
    }
}
