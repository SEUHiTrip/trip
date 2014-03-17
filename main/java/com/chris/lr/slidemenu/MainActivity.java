package com.chris.lr.slidemenu;

import com.chris.lr.slidemenu.LayoutRelative.OnScrollListener;
import com.seu.hitrip.fragment.FragmentHomepage;
import com.seu.hitrip.fragment.FragmentNews;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class MainActivity extends Activity implements OnGestureListener,
        OnTouchListener {
    //	private String URL = "http://gmaps-samples.googlecode.com/svn/trunk/articles-android-webmap/simple-android-map.html";
    private static final String TAG = "ChrisSlideMenu";
    private RelativeLayout mainLayout;
    private RelativeLayout leftLayout;
    private RelativeLayout titlebar;
    private LayoutRelative layoutSlideMenu;
    private ListView mListMore;
    private TextView mTitleText;

    private ImageView ivMore;
    private ImageView ivBack;
    private GestureDetector mGestureDetector;

    private static final int SPEED = 1;
    private boolean bIsScrolling = false;
    //	private int iLimited = 0;
    private int mScroll = 0;
    private View mClickedView = null;

    private final int FRAGMENT_HOME_PAGE = 0;
    private final int FRAGMENT_NORMAL_MAP = 1;
    private final int FRAGMENT_SCENERY_MAP = 2;
    private final int FRAGMENT_NEARBY = 3;
    private final int FRAGMENT_NEWS = 4;
    private final int FRAGMENT_MINE = 5;


    private final String title[] = {
            "首页", // FragmentHomepage
            "市区地图", // FragmentNormalMap
            "景区地图", // FragmentSceneryMap
            "附近的人", // FragmentNearby
            "动态中心", // FragmentNews
            "我的动态", // FragmentMine
            "设置" // FragmentSettings
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initFragment();
    }

    private void initView(){
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        leftLayout = (RelativeLayout) findViewById(R.id.leftLayout);
        titlebar = (RelativeLayout) findViewById(R.id.titlebar);
        mTitleText = (TextView) findViewById(R.id.title_text);
        mainLayout.setOnTouchListener(this);
        leftLayout.setOnTouchListener(this);

        layoutSlideMenu = (LayoutRelative) findViewById(R.id.layoutSlideMenu);
        layoutSlideMenu.setOnScrollListener(new OnScrollListener() {
            @Override
            public void doOnScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                onScroll(distanceX);
            }

            @Override
            public void doOnRelease(){
                onRelease();
            }
        });

        ivMore = (ImageView) findViewById(R.id.ivMore);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivMore.setOnTouchListener(this);
        ivBack.setOnTouchListener(this);

        mListMore = (ListView) findViewById(R.id.listMore);
        mListMore.setAdapter(new ArrayAdapter<String>(this, R.layout.item, R.id.tv_item, title));
        mListMore.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                changeFragment(i);

                Toast.makeText(getApplicationContext(), title[i] + " : " + i, Toast.LENGTH_SHORT).show();

            }
        });

        mGestureDetector = new GestureDetector(this);
        mGestureDetector.setIsLongpressEnabled(false);

        LayoutParams lp = (LayoutParams) leftLayout.getLayoutParams();
        lp.leftMargin = -lp.width;
        leftLayout.setLayoutParams(lp);

        Bitmap userPic = BitmapFactory.decodeResource(getResources(),R.drawable.pic);
        userPic = getRoundedCornerBitmap(userPic);
        ImageView userP = (ImageView) findViewById(R.id.userPic);
        userP.setImageBitmap(userPic);

    }

    private void changeFragment(int i) {
        // Create fragment and give it an argument specifying the article it should show
        Fragment newFragment = getFragment(i);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private Fragment getFragment(int i) {
        Fragment newFragment = null;
        switch (i){
            case FRAGMENT_NEWS:
                newFragment = new FragmentNews();
                break;
            default:
                newFragment = new FragmentHomepage();
                break;
        }
        return newFragment;
    }

    private void initFragment(){
        Fragment firstFragment = new FragmentHomepage();

        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getFragmentManager().beginTransaction()
                .add(R.id.container, firstFragment).commit();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            LayoutParams lp = (LayoutParams) leftLayout.getLayoutParams();
            if(lp.leftMargin == 0) {
                new SlideMenu().execute(leftLayout.getLayoutParams().width, -SPEED);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void rollLayout(int margin) {
        LayoutParams lp = (LayoutParams) leftLayout.getLayoutParams();
        lp.leftMargin += margin;
        if(lp.leftMargin > 0) lp.leftMargin = 0;
        leftLayout.setLayoutParams(lp);
    }

    private void onScroll(float distanceX){
        bIsScrolling = true;
        mScroll += distanceX;
        if(mScroll != 0) rollLayout(-mScroll);
    }

    private void onRelease(){
        LayoutParams lp = (LayoutParams) leftLayout.getLayoutParams();
        if(Math.abs(lp.leftMargin) > lp.width/2)
            new SlideMenu().execute(lp.width - Math.abs(lp.leftMargin), -SPEED);
        else
            new SlideMenu().execute(Math.abs(lp.leftMargin), SPEED);
    }

    ////////////////////////////// onTouch ///////////////////////////////
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mClickedView = v;

        if(MotionEvent.ACTION_UP == event.getAction() && bIsScrolling){
            onRelease();
        }

        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        bIsScrolling = false;
        mScroll = 0;
        return true;
    }

    @Override
    public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        onScroll(distanceX);
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(mClickedView != null){
            LayoutParams lp = (LayoutParams) mainLayout.getLayoutParams();
            if(mClickedView == ivMore) {
                new SlideMenu().execute(leftLayout.getLayoutParams().width, SPEED);
            } else if (mClickedView == ivBack) {
                new SlideMenu().execute(leftLayout.getLayoutParams().width, -SPEED);
            }
        }
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleText.setText(title);
        // super.setTitle(title);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 100;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    public class SlideMenu extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            if(params.length != 2){
                Log.e(TAG, "error, params must have 2!");
            }

            int times = params[0] / Math.abs(params[1]);
            if(params[0] % Math.abs(params[1]) != 0){
                times ++;
            }

            for(int i = 0; i < times; i++){
                this.publishProgress(params[0], params[1], i+1);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if(values.length != 3){
                Log.e(TAG, "error, values must have 3!");
            }

            int distance = Math.abs(values[1]) * values[2];
            int delta = values[0] - distance;

            int leftMargin = 0;
            if(values[1] < 0){ // 左移
                leftMargin = (delta > 0 ? values[1] :
                        -(Math.abs(values[1]) - Math.abs(delta)));
            }else{
                leftMargin = (delta > 0 ? values[1] :
                        (Math.abs(values[1]) - Math.abs(delta)));
            }

            rollLayout(leftMargin);
        }
    }
}