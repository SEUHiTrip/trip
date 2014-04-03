package com.seu.hitrip.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.seu.hitrip.R;
import com.seu.hitrip.person.PersonalInfo;


/**
 * Created by WhiteT on 4/1/2014.
 */
public class LoginActivity extends Activity {

    private BootstrapEditText account;
    private BootstrapEditText password;
    private BootstrapButton clearButton;
    private BootstrapButton loginButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        new LoginTask().execute();
        account = (BootstrapEditText) findViewById(R.id.login_account_text);
        password = (BootstrapEditText) findViewById(R.id.login_password_text);
        clearButton = (BootstrapButton) findViewById(R.id.login_clear_button);
        loginButton = (BootstrapButton) findViewById(R.id.login_login_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account.setText("");
                password.setText("");
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ac = account.getText().toString();
                String pw = password.getText().toString();
                if(ac.equals(getResources().getString(R.string.account_id_1))) {
                    if(pw.equals(getResources().getString(R.string.account_pw_1))) {
                        loginSuccess(1);
                    } else {
                        alartDialog(0);
                    }
                } else if (ac.equals(getResources().getString(R.string.account_id_2))) {
                    if(pw.equals(getResources().getString(R.string.account_pw_2))) {
                        loginSuccess(2);
                    } else {
                        alartDialog(0);
                    }
                } else {
                    alartDialog(1);
                }
            }
        });
    }

    private void loginSuccess (int i) {
        Toast.makeText(LoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
        MainActivity.account = i;
        PersonalInfo.account = i;
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void alartDialog(int i) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("错误")
                .setMessage(i == 0 ? "密码错误！" : "用户不存在！")
                .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 1) {
                            account.setText("");
                        }
                        password.setText("");
                    }
                })
                .show();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1) {
                findViewById(R.id.login_relativelayout).setVisibility(View.VISIBLE);
                findViewById(R.id.application_logo).setVisibility(View.INVISIBLE);
            }
            super.handleMessage(msg);
        }
    };

    private class LoginTask extends AsyncTask<Void, String, Void> {

        // 实现抽象方法doInBackground()，代码将在后台线程中执行，由execute()触发，由于这个例子并不需要传递参数，使用Void...，具体书写方式为范式书写

        @Override
        protected Void doInBackground(Void... params) {
            SystemClock.sleep(2000);
            Message msg = mHandler.obtainMessage();
            msg.what = 1;
            msg.sendToTarget();
            return null;
        }
    }
}
