package com.seu.hitrip.map;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seu.hitrip.cose.R;

import java.io.IOException;

public class Popup extends MapPopupBase implements OnClickListener
{
	private Button btnMapDetailAudioPlay;
	private Button btnMapDetailAudioStop;
	private Button tvMapPointDetail;
	private Button map_point_detail;
	private TextView tvAudioTime;
	private TextView vAudioPlay;
	private ProgressBar vAudioBuffering;
	private MediaPlayer mp;
	private ImageButton self_location;
	private View view;
	private Context context;
	private MapObjectModel objectModel;


	private Handler handler = new Handler();

	public void StrartbarUpdate(){
        handler.post(timeUpdate);
    }

	 private Runnable timeUpdate = new Runnable()
	  {
	    public void run()
	    {
	    	tvAudioTime.setText(formatLongToTimeStr(mp.getCurrentPosition()));
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



    public Popup(Context context, View view, ViewGroup parentView)
    {
        super(context, parentView);
        this.view=view;
        this.context=context;
        container.addView(view);
        btnMapDetailAudioPlay = ((Button)view.findViewById(R.id.map_point_play));
	    btnMapDetailAudioStop = ((Button)view.findViewById(R.id.map_point_pause));
	    tvMapPointDetail = ((Button)view.findViewById(R.id.tv_map_point_detail));
	    map_point_detail=(Button) view.findViewById(R.id.map_point_detail);
	    vAudioPlay=(TextView) parentView.findViewById(R.id.map_audio_play);
	    tvAudioTime=(TextView) parentView.findViewById(R.id.timing);
	    vAudioBuffering=(ProgressBar) parentView.findViewById(R.id.buffering);
	    self_location=(ImageButton) parentView.findViewById(R.id.map_self_location);
	    self_location.setOnClickListener(this);
	    btnMapDetailAudioPlay.setOnClickListener(this);
	    btnMapDetailAudioStop.setOnClickListener(this);
	    map_point_detail.setOnClickListener(this);
	    vAudioPlay.setOnClickListener(this);
	    tvAudioTime.setOnClickListener(this);

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
	    vAudioPlay.setVisibility(View.VISIBLE);
		tvAudioTime.setVisibility(View.GONE);
	    if (mp!=null) {
			mp.release();
		}
  	    mp= MediaPlayer.create(context, objectModel.getMp3());
  	    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			 /*�����ļ���������¼�*/
			@Override
			public void onCompletion(MediaPlayer arg0) {

			try {
				 mp.release();
				 handler.removeCallbacks(timeUpdate);
				 btnMapDetailAudioPlay.setVisibility(View.VISIBLE);
				 btnMapDetailAudioStop.setVisibility(View.GONE);
				 vAudioPlay.setVisibility(View.VISIBLE);
				 tvAudioTime.setVisibility(View.GONE);
			} catch (Exception e) {


				 e.printStackTrace();
			}

			}
		});

		 mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			/*���Ǵ������¼�*/
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {

				try {
					   mp.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});


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
			try {
				 if(mp != null)  {
					 mp.stop();
				 }
				mp.prepare();
				mp.start();
				vAudioPlay.setVisibility(View.INVISIBLE);
				vAudioBuffering.setVisibility(View.VISIBLE);
		    	vAudioBuffering.setVisibility(View.GONE);
				tvAudioTime.setVisibility(View.VISIBLE);

		    	StrartbarUpdate();

			} catch (Exception e) {
				 e.printStackTrace();
			}

			break;

		case R.id.map_point_pause:
			vAudioPlay.setVisibility(View.VISIBLE);
			tvAudioTime.setVisibility(View.GONE);
			btnMapDetailAudioPlay.setVisibility(View.VISIBLE);
			btnMapDetailAudioStop.setVisibility(View.GONE);

			try {
				 if(mp !=null)  {
					 mp.stop();
				 }
			} catch (Exception e) {

				  e.printStackTrace();
			}

			break;


		case R.id.map_point_detail:

//			Intent intent=new Intent();
//			intent.setClass(context, DetailActivity.class);
//			intent.putExtra("�������", objectModel.getCaption());
//			context.startActivity(intent);

		   break;

		case R.id. map_audio_play:

			// 0 �ɼ� 4 ���ɼ�ռ�ò��ֿռ䣩 8�ɲ���ģ���ռ�ò��ֿռ䣩

		    	if (mp!=null) {
		    		vAudioPlay.setVisibility(View.INVISIBLE);
					vAudioBuffering.setVisibility(View.VISIBLE);
			    	vAudioBuffering.setVisibility(View.GONE);
			    	tvAudioTime.setVisibility(View.VISIBLE);
			    	btnMapDetailAudioPlay.setVisibility(View.GONE);
			    	btnMapDetailAudioStop.setVisibility(View.VISIBLE);
			    	try {
						mp.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		mp.start();
		    		StrartbarUpdate();
				}

		 break;

		case R.id.timing:
			handler.removeCallbacks(timeUpdate);
			tvAudioTime.setVisibility(View.GONE);
			vAudioBuffering.setVisibility(View.VISIBLE);
	    	vAudioBuffering.setVisibility(View.GONE);
	    	vAudioPlay.setVisibility(View.VISIBLE);
	    	btnMapDetailAudioPlay.setVisibility(View.VISIBLE);
	    	btnMapDetailAudioStop.setVisibility(View.GONE);
	    	if (mp.isPlaying()) {
	    		mp.stop();
			}

		 break;



		case R.id.map_self_location:



		default:


			break;
		}

	}



}
