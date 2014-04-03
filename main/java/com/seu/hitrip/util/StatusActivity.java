package com.seu.hitrip.util;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.seu.hitrip.R;


/**
 * Created by WhiteT on 3/23/2014.
 */
public class StatusActivity extends Activity {

    private EditText news_edittext;
    private String news_text;
    private String news_img_url;
    private Button add_img_button;
    private Button submit_button;
    private TextView url_img_text;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_status);
        initView();
    }

    public void initView() {
        news_edittext = (EditText) findViewById(R.id.fragment_status_news_text);
        add_img_button = (Button) findViewById(R.id.frgament_status_addimg_button);
        submit_button = (Button) findViewById(R.id.frgament_status_submit);
        url_img_text = (TextView) findViewById(R.id.fragment_status_img_url);

        add_img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                news_text = news_edittext.getText().toString().trim();
                Intent in = new Intent();
                in.putExtra("text", news_text);
                in.putExtra("img",news_img_url);
                StatusActivity.this.setResult(RESULT_OK, in);
                StatusActivity.this.finish();
                //
                //在动态界面添加一张卡片
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            news_img_url = cursor.getString(columnIndex);
            cursor.close();

            url_img_text.setText("已选择图片:"+news_img_url);
            add_img_button.setText("更改图片");
        }
    }
}