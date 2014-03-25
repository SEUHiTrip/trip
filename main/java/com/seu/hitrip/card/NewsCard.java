package com.seu.hitrip.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.MenuItem;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seu.hitrip.cose.R;
import com.seu.hitrip.util.BitmapTools;


import java.util.Date;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by yqf on 3/17/14.
 */
public class NewsCard extends Card {

    public static String[] ACTIONS = {
        "拍了照片",
        "说了一句话",
    };

    public static int ACTION_PIC = 0;
    public static int ACTION_TEXT = 1;

    private  String people;
    private  String msg;
    private  String location;
    private  int actionType;
    private  Date postTime;
    private  Bitmap avatar;
    private  Bitmap pic;
    private ViewGroup parent;

    public static NewsCard getCard(final Context context,
                               String people,
                               String content,
                               String location,
                               int action_type,
                               Date post_time,
                               Bitmap avater,
                               Bitmap pic){

        NewsCard card = new NewsCard(context, people, content,location,action_type,post_time,avater,pic);

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

        card.setInnerLayout(R.layout.card_news_inner_layout);

        card.setShadow(true);

        return card;
    }


    private NewsCard(final Context context,
                     String people,
                     String content,
                     String location,
                     int actionType,
                     Date postTime,
                     Bitmap avatar,
                     Bitmap pic){
        super(context, R.layout.card_thumbnail_layout);
        this.people = people;
        this.msg = content;
        this.location = location;
        this.actionType = actionType;
        this.postTime = postTime;
        this.avatar = avatar;
        this.pic = pic;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        this.parent = parent;
        TextView usernameView = (TextView) parent.findViewById(R.id.card_news_inner_user_name);
        TextView msgView = (TextView) parent.findViewById(R.id.card_news_inner_msg);
        TextView locationView = (TextView) parent.findViewById(R.id.card_news_inner_location);
        TextView actionTypeView = (TextView) parent.findViewById(R.id.card_news_inner_action_type);
        TextView postTimeView = (TextView) parent.findViewById(R.id.card_news_inner_post_time);
        ImageView avatarView = (ImageView) parent.findViewById(R.id.card_news_inner_avatar);
        ImageView picView = (ImageView) parent.findViewById(R.id.card_news_inner_pic);
        usernameView.setText(people);
        msgView.setText(msg);
        locationView.setText(location);
        actionTypeView.setText(ACTIONS[actionType]);
        postTimeView.setText(DateFormat.format("MM.dd HH:mm", postTime));
        avatarView.setImageBitmap(BitmapTools.getRoundedCornerBitmap(avatar));
        picView.setImageBitmap(pic);

        super.setupInnerViewElements(parent, view);
    }

}
