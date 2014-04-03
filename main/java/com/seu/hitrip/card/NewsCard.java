package com.seu.hitrip.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seu.hitrip.R;
import com.seu.hitrip.person.PersonalInfo;
import com.seu.hitrip.util.BitmapTools;


import java.util.Date;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by yqf on 3/17/14.
 */
public class NewsCard extends Card {


    NewsInfo info = null;

    private ViewGroup parent;

    public static NewsCard getCard(final Context context,
                               NewsInfo info){

        NewsCard card = new NewsCard(context, info);

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
                     NewsInfo info){
        super(context, R.layout.card_thumbnail_layout);
        this.info = info;
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
        usernameView.setText(info.people.name);
        msgView.setText(info.msg);
        locationView.setText(info.location);
        actionTypeView.setText(NewsInfo.ACTIONS[info.actionType]);
        postTimeView.setText(DateFormat.format("MM.dd HH:mm", info.postTime));
        avatarView.setImageBitmap(BitmapTools.getRoundedCornerBitmap(((BitmapDrawable)info.people.avatar).getBitmap()));
        picView.setImageBitmap(info.pic);

        super.setupInnerViewElements(parent, view);
    }

    static public class NewsInfo implements Comparable<NewsInfo> {

        public static String[] ACTIONS = {
                "拍了照片",
                "说了一句话",
        };

        public static int ACTION_PIC = 0;
        public static int ACTION_TEXT = 1;

        public  PersonalInfo.People people;
        public  String msg;
        public  String location;
        public  int actionType;
        public  Date postTime;
        public  Bitmap pic;

        public NewsInfo(
                PersonalInfo.People people,
                String content,
                String location,
                int actionType,
                Date postTime,
                Bitmap pic
        ){
            this.people = people;
            this.msg = content;
            this.location = location;
            this.actionType = actionType;
            this.postTime = postTime;
            this.pic = pic;
        }


        @Override
        public int compareTo(NewsInfo newsInfo) {
            return (int) (newsInfo.postTime.getTime() - this.postTime.getTime());
        }

        @Override
        public boolean equals(Object obj) {
            if(! (obj instanceof NewsInfo)) return false;
            NewsInfo info = (NewsInfo) obj;
            if(!this.msg.equals(info.msg)) return false;
            if(!(this.people.id == info.people.id)) return false;
            if(!this.postTime.equals(info.postTime)) return false;
            if(!(this.actionType == info.actionType)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return msg.hashCode()+postTime.hashCode();
        }
    }
}
