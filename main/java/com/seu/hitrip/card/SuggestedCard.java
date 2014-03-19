package com.seu.hitrip.card;

/**
 * Created by WhiteT on 3/18/14.
 */

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.seu.hitrip.until.R;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

public class SuggestedCard extends Card {

    public SuggestedCard(Context context) {
        this(context, R.layout.card_suggested_inner_content);
    }

    public SuggestedCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    private void init() {

        //Add a header
        SuggestedCardHeader header = new SuggestedCardHeader(getContext());
        addCardHeader(header);

        //Set click listener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getContext(), "Click listener", Toast.LENGTH_LONG).show();
            }
        });

        //Set swipe on
        setSwipeable(true);

        //Add thumbnail
        CardThumbnail thumb = new SuggestedCardThumb(getContext());
        thumb.setUrlResource("https://lh5.googleusercontent.com/-N8bz9q4Kz0I/AAAAAAAAAAI/AAAAAAAAAAs/Icl2bQMyK7c/s265-c-k-no/photo.jpg");
        thumb.setErrorResource(R.drawable.ic_error_loadingorangesmall);
        addCardThumbnail(thumb);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view != null) {
            TextView title = (TextView) view.findViewById(R.id.carddemo_suggested_title);
            TextView member = (TextView) view.findViewById(R.id.carddemo_suggested_memeber);
            TextView subtitle = (TextView) view.findViewById(R.id.carddemo_suggested_subtitle);
            TextView community = (TextView) view.findViewById(R.id.carddemo_suggested_community);

            if (title != null)
                title.setText(R.string.suggested_card_title);

            if (member != null)
                member.setText(R.string.suggested_card_member);

            if (subtitle != null)
                subtitle.setText(R.string.suggested_card_subtitle);

            if (community != null)
                community.setText(R.string.suggested_card_community);
        }
    }


}

class SuggestedCardHeader extends CardHeader {

    public SuggestedCardHeader(Context context) {
        this(context, R.layout.card_suggested_header_inner);
    }

    public SuggestedCardHeader(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view != null) {
            TextView textView = (TextView) view.findViewById(R.id.text_suggested_card1);

            if (textView != null) {
                textView.setText(R.string.suggested_card_header);
            }
        }
    }
}

class SuggestedCardThumb extends CardThumbnail {

    public SuggestedCardThumb(Context context) {
        super(context);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View viewImage) {
        if (viewImage != null) {

            if (parent!=null && parent.getResources()!=null){
                DisplayMetrics metrics=parent.getResources().getDisplayMetrics();

                int base = 100;

                if (metrics!=null){
                    viewImage.getLayoutParams().width = (int)(base*metrics.density);
                    viewImage.getLayoutParams().height = (int)(base*metrics.density);
                }else{
                    viewImage.getLayoutParams().width = 200;
                    viewImage.getLayoutParams().height = 200;
                }
            }
        }
    }
}
