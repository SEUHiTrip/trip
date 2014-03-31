package com.seu.hitrip.map;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.ls.widgets.map.MapWidget;
import com.seu.hitrip.cose.R;
import com.seu.hitrip.person.PersonalInfo;


public class Popup extends MapPopupBase implements OnClickListener
{
    private final MapWidget map;
    private Button btnMapDetailAudioPlay;
	private Button btnMapDetailAudioStop;
	private Button tvMapPointDetail;
	private Button map_point_detail;
	private BootstrapButton self_location;
	private View view;
	private Context context;
	private MapObjectModel objectModel;


	private Handler handler = new Handler();

	public void StrartbarUpdate(){
        handler.post(timeUpdate);
    }

	 private Runnable timeUpdate = new Runnable(){
	    public void run(){
	        handler.postDelayed(timeUpdate, 1000);
	    }
	  };

	  private static String formatLongToTimeStr(int l) {
	         int hour = 0;
	         int minute = 0;
	         int second = 0;

	         second = l / 1000;

	         if (second > 60) {
	             minute = second / 60;
	             second = second % 60;
	         }else {

	        	 return getTwoLength(second);
			 }

	         if (minute > 60) {
	             hour = minute / 60;
	             minute = minute % 60;
	         }else {

	        	 return getTwoLength(minute)  + ":"  + getTwoLength(second);
			}

	         return (getTwoLength(hour) + ":" + getTwoLength(minute)  + ":"  + getTwoLength(second));
	     }

	  private static String getTwoLength(final int data) {
	         if(data < 10) {
	             return "0" + data;
	         } else {
	             return "" + data;
	         }
	     }



    public Popup(Context context, View view, ViewGroup parentView, MapWidget map)
    {
        super(context, parentView);
        this.view=view;
        this.context=context;
        this.map = map;
        container.addView(view);
        btnMapDetailAudioPlay = ((Button)view.findViewById(R.id.map_point_play));
	    btnMapDetailAudioStop = ((Button)view.findViewById(R.id.map_point_pause));
	    tvMapPointDetail = ((Button)view.findViewById(R.id.tv_map_point_detail));
	    map_point_detail=(Button) view.findViewById(R.id.map_point_detail);

	    self_location=(BootstrapButton) parentView.findViewById(R.id.map_self_location);
	    self_location.setOnClickListener(this);
	    btnMapDetailAudioPlay.setOnClickListener(this);
	    btnMapDetailAudioStop.setOnClickListener(this);
	    map_point_detail.setOnClickListener(this);

    }


    public void moveBy(int dx, int dy)
    {
        if (lastX != -1 && lastY != -1){
            int paddingBottom = 0;
            int paddingRight = 0;
            if(container.getPaddingTop() > (screenHeight - (view.getHeight() + 3))){
                paddingBottom = (container.getPaddingBottom() - dy);
            }

            if(container.getPaddingLeft() > (screenWidth - (view.getWidth() + 3))){
                paddingRight = container.getPaddingRight() - dx;
            }

            container.setPadding(container.getPaddingLeft() + dx,
                                 container.getPaddingTop() + dy,
                                 paddingRight, paddingBottom);
        }
    }


    public void setInfo(MapObjectModel objectModel,String theText)
    {
    	this.objectModel=objectModel;
    	tvMapPointDetail.setText(theText);
    	btnMapDetailAudioPlay.setVisibility(View.VISIBLE);
	    btnMapDetailAudioStop.setVisibility(View.GONE);


        try {
             handler.removeCallbacks(timeUpdate);
             btnMapDetailAudioPlay.setVisibility(View.VISIBLE);
             btnMapDetailAudioStop.setVisibility(View.GONE);
        } catch (Exception e) {
             e.printStackTrace();
        }


    }

    public void removeIcon()
    {
    	container.removeView(view);
    }


	@Override
	public void onClick(View paramView) {
		switch (paramView.getId()) {

		case R.id.map_point_play:

			btnMapDetailAudioPlay.setVisibility(View.GONE);
			btnMapDetailAudioStop.setVisibility(View.VISIBLE);

			break;
		case R.id.map_point_pause:
			btnMapDetailAudioPlay.setVisibility(View.VISIBLE);
			btnMapDetailAudioStop.setVisibility(View.GONE);

			break;
		case R.id.map_point_detail:
//			Intent intent=new Intent();
//			intent.setClass(context, DetailActivity.class);
//			intent.putExtra("�������", objectModel.getCaption());
//			context.startActivity(intent);
		   break;

		default:
			break;
		}

	}



}
