package com.seu.hitrip.card;

/**
 * Created by WhiteT on 3/18/14.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.seu.hitrip.R;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;


public class SuggestedCard extends Card {

    private int sign;
    private int imageresource;
    private int suggesturl;

    public SuggestedCard(int s, Context context) {
        this(s, context, R.layout.card_suggested_inner_content);
    }


    public SuggestedCard(int s, Context context, int innerLayout) {
        super(context, innerLayout);
        init(s);
    }

    private void init(int s) {

        sign = s;
        switch (sign) {
            case 2:
                imageresource = R.drawable.sug02;
                suggesturl = R.string.suggest_card_url2;
                break;
            case 3:
                imageresource = R.drawable.sug03;
                suggesturl = R.string.suggest_card_url3;
                break;
            case 4:
                imageresource = R.drawable.sug04;
                suggesturl = R.string.suggest_card_url4;
                break;
            case 5:
                imageresource = R.drawable.sug05;
                suggesturl = R.string.suggest_card_url5;
                break;
            case 6:
                imageresource = R.drawable.sug06;
                suggesturl = R.string.suggest_card_url6;
                break;
            default:
                imageresource = R.drawable.sug01;
                suggesturl = R.string.suggest_card_url1;
        }

        //Add a header
        SuggestedCardHeader header = new SuggestedCardHeader(sign, getContext());
        addCardHeader(header);

        //Set click listener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getContext(), "正在跳转", Toast.LENGTH_LONG).show();
                Uri url = Uri.parse(getContext().getString(suggesturl));
                Intent it = new Intent(Intent.ACTION_VIEW, url);
                getContext().startActivity(it);
            }
        });
        //Set swipe on
        setSwipeable(true);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        if (view != null) {
            ImageView image = (ImageView) view.findViewById(R.id.card_suggested_image);
            if(image != null) {
                image.setImageResource(imageresource);
            }
        }
    }
}

class SuggestedCardHeader extends CardHeader {

    public int s;

    public SuggestedCardHeader(int sign, Context context) {
        this(context, R.layout.card_suggested_header_inner);
        this.s = sign;
    }

    public SuggestedCardHeader(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        if (view != null) {
            TextView textView = (TextView) view.findViewById(R.id.text_suggested_card1);
            if (textView != null) {
                int text, color, textColor = R.color.white;
                switch (s) {
                    case 2:
                        text = R.string.suggest_card_header2;
                        color = R.color.suggest_header_color2;
                        break;
                    case 3:
                        text = R.string.suggest_card_header3;
                        color = R.color.suggest_header_color3;
                        break;
                    case 4:
                        text = R.string.suggest_card_header4;
                        color = R.color.suggest_header_color4;
                        textColor = R.color.black;
                        break;
                    case 5:
                        text = R.string.suggest_card_header5;
                        color = R.color.suggest_header_color5;
                        break;
                    case 6:
                        text = R.string.suggest_card_header6;
                        color = R.color.suggest_header_color6;
                        break;
                    default:
                        text = R.string.suggest_card_header1;
                        color = R.color.suggest_header_color1;
                }
                textView.setText(getContext().getString(text));
                textView.setBackgroundColor(getContext().getResources().getColor(color));
                textView.setTextColor(getContext().getResources().getColor(textColor));
            }
        }
    }
}
