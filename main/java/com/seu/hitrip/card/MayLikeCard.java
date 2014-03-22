package com.seu.hitrip.card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seu.hitrip.cose.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardView;
import it.gmariotti.cardslib.library.view.component.CardThumbnailView;

/**
 * Created by WhiteT on 3/18/14.
 */
public class MayLikeCard extends Card {

    private int sign;
    private CardThumbnail thumbnail;

    public MayLikeCard(Context context) {
        this(context, R.layout.card_maylike_inner_content);
    }

    public MayLikeCard(int sign, Context context) {
        this(context, R.layout.card_maylike_inner_content);
        this.sign = sign;
    }

    public MayLikeCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    private void init() {
        //Add Header
        CardHeader header = new MayLikeCardHeader(getContext(), R.layout.card_maylike_inner_header);
        addCardHeader(header);
        setShadow(false);

        //Add Thumbnail
        thumbnail = new CardThumbnail(getContext());
        addCardThumbnail(thumbnail);

        OnCardClickListener clickListener = new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                //Do something
            }
        };

        addPartialOnClickListener(Card.CLICK_LISTENER_CONTENT_VIEW,clickListener);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        TextView title = (TextView) view.findViewById(R.id.card_maylike_main_inner_title);
        TextView subtitle = (TextView) view.findViewById(R.id.card_maylike_main_inner_subtitle);
        TextView add = (TextView) view.findViewById(R.id.card_maylike_main_inner_button);
        add.setClickable(true);

        int titleText, subtitleText, drawableResource;
        switch(sign) {
            case 1:
                titleText = R.string.may_like_card_inner_title_1;
                subtitleText = R.string.may_like_card_inner_subtitle_1;
                drawableResource = R.drawable.ml02;
                break;
            case 2:
                titleText = R.string.may_like_card_inner_title_2;
                subtitleText = R.string.may_like_card_inner_subtitle_2;
                drawableResource = R.drawable.ml03;
                break;
            default:
                titleText = R.string.may_like_card_inner_title_0;
                subtitleText = R.string.may_like_card_inner_subtitle_0;
                drawableResource = R.drawable.ml01;
        }
        title.setText(getContext().getString(titleText));
        subtitle.setText(getContext().getString(subtitleText));
        thumbnail.setDrawableResource(drawableResource);
        thumbnail.setErrorResource(R.drawable.ic_error_loadingsmall);

        CardView cardView = getCardView();
        CardThumbnailView thumb = cardView.getInternalThumbnailLayout();
        if (thumb != null) {
            ViewGroup.LayoutParams lp = thumb.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) lp).setMargins(25, 0, 0, 5);
            }
        }


    }

    class MayLikeCardHeader extends CardHeader {

        public MayLikeCardHeader(Context context, int innerLayout) {
            super(context, innerLayout);
        }

        @Override
        public void setupInnerViewElements(ViewGroup parent, View view) {
            super.setupInnerViewElements(parent, view);
            TextView headertitle = (TextView) view.findViewById(R.id.card_maylike_header_inner_title);
            headertitle.setText(getContext().getString(R.string.may_like_card_title));

            TextView headersubtitle = (TextView) view.findViewById(R.id.card_maylike_header_inner_subtitle);
            headersubtitle.setText(getContext().getString(R.string.may_like_card_subtitle));
        }
    }

}
