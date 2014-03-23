package com.seu.hitrip.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.seu.hitrip.cose.R;
import com.seu.hitrip.util.BitmapTools;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by yqf on 3/23/14.
 */
public class MyRecordCard extends Card {

    Bitmap background;
    Bitmap avatar;

    public MyRecordCard(Context context,
                        int innerLayout,
                        Bitmap background,
                        Bitmap avatar
                        ) {
        super(context, innerLayout);

        this.background = background;
        this.avatar = avatar;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        ImageView backgroundView = (ImageView) parent.findViewById(R.id.card_my_record_inner_backgroud);
        ImageView avatarView = (ImageView) parent.findViewById(R.id.card_my_record_inner_avatar);


        backgroundView.setImageBitmap(background);
        avatarView.setImageBitmap(BitmapTools.getRoundedCornerBitmap(avatar));
        super.setupInnerViewElements(parent, view);
     }
}
