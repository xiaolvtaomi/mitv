package com.tv.ui.metro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yealink.MainApplication;
import com.yealink.lib.common.wrapper.AccountConstant;
import com.yealink.lib.common.wrapper.SettingsManager;
import com.yealink.lib.common.wrapper.SipProfile;

public class LoginStep1Activity extends Activity  implements SettingsManager.AccountStateListener {
    Button button;

    private EditText tv_username, tv_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_step1);

        tv_username = (EditText) findViewById(R.id.login_yanzhen);
        tv_password = (EditText) findViewById(R.id.id_login_et_pwd);

        SettingsManager.getInstance().registerAccountListener(LoginStep1Activity.this);
        setDialog();
        button= (Button) findViewById(R.id.bt_go);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNullOrEmpty(tv_username.getText().toString())&&
                        !isNullOrEmpty(tv_password.getText().toString())) {
                    if (ad != null && ad.isShowing()) {
                        ad.dismiss();
                    }
                    try {
                        ad.show();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    //登录视频会议的时候  先注销。
                    hide();
                    save(tv_username.getText().toString(), tv_password.getText().toString());
                    isCall = true;
                    //为空代表是视频会议 挂断后不需要注销
                    MainApplication.code = "";
                } else {
                    Toast.makeText(LoginStep1Activity.this,"请输入完整的账号密码",Toast.LENGTH_SHORT).show();
                }

            }
        });
        button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Animation animIn = AnimationUtils.loadAnimation(LoginStep1Activity.this, R.anim.anim_in) ;
                Animation animOut = AnimationUtils.loadAnimation(LoginStep1Activity.this, R.anim.anim_out) ;
                if(hasFocus){
                    //Bring the view to ViewContainer`s front . such as ViewGroup`s bringChildToFront(View) ;
                   button.setBackgroundColor(Color.parseColor("#d1d1d1"));
                    v.bringToFront() ;
                    v.setAnimation(animIn);
                    v.startAnimation(animIn);
                }else{
                    button.setBackgroundColor(Color.parseColor("#FFCCCC"));
                    v.setAnimation(animOut);
                    v.startAnimation(animOut);
                }
            }
        });

    }

    private void save(String username, String password) {
        SipProfile sp = SettingsManager.getInstance().getSipProfile(0);
        sp.userName = username;
        sp.registerName = username;
        sp.password = password;
        sp.server = "pds01.com";
        sp.port = 5061;
        sp.outboundPort=5060;
        sp.outboundServer="202.110.98.2";
        sp.isBFCPEnabled = false;
        sp.isEnableOutbound=true;
        sp.isEnabled = true;
        sp.transPort = SipProfile.TRANSPORT_TCP;
        SettingsManager.getInstance().setSipProfile(0, sp);
    }


    private static final int MSG_ACCOUNT_CHANGE = 200;
    private boolean isCall = false;
    private String callId = "";// 被叫电话
    private AlertDialog ad;
    private AlertDialog.Builder builder;
    private LinearLayout ll_status;
    private TextView tv_status;
    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ACCOUNT_CHANGE:
                    setStatus(SettingsManager.getInstance().getSipProfile(0));
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void onAccountStateChanged(int arg0, int arg1, int arg2) {
        handler.sendEmptyMessage(MSG_ACCOUNT_CHANGE);
    }

    private void setStatus(SipProfile sp) {
        if (sp.state == AccountConstant.STATE_UNKNOWN) {
            tv_status.setText("未知");
        } else if (sp.state == AccountConstant.STATE_DISABLED) {
            tv_status.setText("网络繁忙，请稍后在拨……");
        } else if (sp.state == AccountConstant.STATE_REGING) {
            tv_status.setText("正在注册...");
        } else if (sp.state == AccountConstant.STATE_REGED) {
            tv_status.setText("已注册");

            if (isCall) {
                if (ad != null && ad.isShowing()) {
                    ad.dismiss();
                }
                isCall = false;
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Intent intent=new Intent(LoginStep1Activity.this,LoginStep2Activity.class);
                        startActivity(intent);
                    }
                },1000);
            }

        } else if (sp.state == AccountConstant.STATE_REG_FAILED) {
            tv_status.setText("注册失败");
        } else if (sp.state == AccountConstant.STATE_UNREGING) {
            tv_status.setText("正在注销...");
        } else if (sp.state == AccountConstant.STATE_UNREGED) {
            tv_status.setText("已注销");
        } else if (sp.state == AccountConstant.STATE_REG_ON_BOOT) {
            tv_status.setText("启动时注册");
        }
    }

    private void setDialog() {
        // TODO Auto-generated method stub
        ll_status = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ad_threetree_status, null);
        tv_status = (TextView) ll_status.findViewById(R.id.tv_status);
        builder = new AlertDialog.Builder(this, R.style.Dialog);
        builder.setView(ll_status);
        builder.setCancelable(false);
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                hide();
                isCall = false;
            }
        });
        ad = builder.create();
    }


    public void hide() {
        // TODO Auto-generated method stub
        SipProfile sp = SettingsManager.getInstance().getSipProfile(0);
        sp.isEnabled = false;
        SettingsManager.getInstance().setSipProfile(0, sp);
        isCall = false;
    }
    /**
     * 判断是否为null或空�?
     *
     * @param str
     *            String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == keyEvent.KEYCODE_BACK) {
            Intent intent=new Intent(LoginStep1Activity.this,MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onKeyDown(keyCode, keyEvent);
    }
}
