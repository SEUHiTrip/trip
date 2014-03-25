//package com.seu.hitrip.util;
//
//import android.app.Activity;
//import android.app.Fragment;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.seu.hitrip.cose.R;
//
//
///**
// * Created by WhiteT on 3/23/2014.
// */
//public class StatusActivity extends Activity {
//
//    private EditText news_edittext;
//    private String news_text;
//    private String news_img_url;
//    private Bitmap news_img;
//    private Button add_img_button;
//    private Button submit_button;
//
//    private static int RESULT_LOAD_IMAGE = 1;
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSate) {
//        return inflater.inflate(R.layout.activity_status, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        news_edittext = (EditText) findViewById(R.id.frgament_status_news_text);
//        add_img_button = (Button) findViewById(R.id.frgament_status_addimg_button);
//        submit_button = (Button) findViewById(R.id.frgament_status_submit);
//
//        add_img_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
//            }
//        });
//
//        submit_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                news_text = news_edittext.getText().toString().trim();
//                news_img = BitmapFactory.decodeFile(news_img_url);
//            }
//        });
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            news_img_url = cursor.getString(columnIndex);
//            cursor.close();
//        }
//    }
//
//}
