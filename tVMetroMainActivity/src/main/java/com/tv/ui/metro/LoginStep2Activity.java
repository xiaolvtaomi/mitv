package com.tv.ui.metro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yealink.lib.common.wrapper.CallParams;
import com.yealink.lib.common.wrapper.Calllog;
import com.yealink.lib.common.wrapper.SettingsManager;
import com.yealink.lib.common.wrapper.SipProfile;
import com.yealink.sample.TalkingActivity;

import static com.tv.ui.metro.LoginStep1Activity.isNullOrEmpty;

/**
 * Created by lml on 17/9/17.
 */

public class LoginStep2Activity extends Activity {
    Button button;
    TextView forgot_your_password;
    private EditText tv_username, tv_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_step2);
        button = (Button) findViewById(R.id.bt_go);
        forgot_your_password = (TextView) findViewById(R.id
                .forgot_your_password);
        tv_username = (EditText) findViewById(R.id.login_yanzhen);
        tv_password = (EditText) findViewById(R.id.id_login_et_pwd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNullOrEmpty(tv_username.getText().toString())&&
                        !isNullOrEmpty(tv_password.getText().toString())) {

                    String callId = tv_username.getText().toString()+"**"+tv_password.getText().toString()+"@202.110.98.2";
                    CallParams ps = CallParams.create(callId, 0, Calllog.PROTOCOL_SIP, false, 0, true, true, null);
                    Intent it = new Intent(LoginStep2Activity.this, TalkingActivity.class);
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    it.putExtra(TalkingActivity.EXTRA_CALL_PARAMS, ps);
                    startActivity(it);
                } else {
                    Toast.makeText(LoginStep2Activity.this,"请输入会议室账户和密码",Toast.LENGTH_SHORT).show();

                }

            }
        });
        button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Animation animIn = AnimationUtils.loadAnimation
                        (LoginStep2Activity.this, R.anim.anim_in);
                Animation animOut = AnimationUtils.loadAnimation
                        (LoginStep2Activity.this, R.anim.anim_out);
                if (hasFocus) {
                    //Bring the view to ViewContainer`s front . such as
                    // ViewGroup`s bringChildToFront(View) ;
                    button.setBackgroundColor(Color.parseColor("#FFCCCC"));
                    v.bringToFront();
                    v.setAnimation(animIn);
                    v.startAnimation(animIn);
                } else {
                    button.setBackgroundColor(Color.parseColor("#d1d1d1"));
                    v.setAnimation(animOut);
                    v.startAnimation(animOut);
                }
            }
        });
        forgot_your_password.setOnFocusChangeListener(new View
                .OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Animation animIn = AnimationUtils.loadAnimation
                        (LoginStep2Activity.this, R.anim.anim_in);
                Animation animOut = AnimationUtils.loadAnimation
                        (LoginStep2Activity.this, R.anim.anim_out);
                if (hasFocus) {
                    //Bring the view to ViewContainer`s front . such as
                    // ViewGroup`s bringChildToFront(View) ;
                    v.bringToFront();
                    v.setAnimation(animIn);
                    v.startAnimation(animIn);
                } else {
                    v.setAnimation(animOut);
                    v.startAnimation(animOut);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SipProfile sp = SettingsManager.getInstance().getSipProfile(0);
        sp.isEnabled = false;
        SettingsManager.getInstance().setSipProfile(0, sp);
    }
}
