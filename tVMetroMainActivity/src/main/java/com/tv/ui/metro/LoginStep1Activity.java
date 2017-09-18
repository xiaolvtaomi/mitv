package com.tv.ui.metro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class LoginStep1Activity extends Activity   {
   Button button;
   TextView forgot_your_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_step1);
        button= (Button) findViewById(R.id.bt_go);
        forgot_your_password= (TextView) findViewById(R.id.forgot_your_password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginStep1Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Animation animIn = AnimationUtils.loadAnimation(LoginStep1Activity.this, R.anim.anim_in) ;
                Animation animOut = AnimationUtils.loadAnimation(LoginStep1Activity.this, R.anim.anim_out) ;
                if(hasFocus){
                    //Bring the view to ViewContainer`s front . such as ViewGroup`s bringChildToFront(View) ;
                   button.setBackgroundColor(Color.parseColor("#FFCCCC"));
                    v.bringToFront() ;
                    v.setAnimation(animIn);
                    v.startAnimation(animIn);
                }else{
                    button.setBackgroundColor(Color.parseColor("#d1d1d1"));
                    v.setAnimation(animOut);
                    v.startAnimation(animOut);
                }
            }
        });
        forgot_your_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Animation animIn = AnimationUtils.loadAnimation(LoginStep1Activity.this, R.anim.anim_in) ;
                Animation animOut = AnimationUtils.loadAnimation(LoginStep1Activity.this, R.anim.anim_out) ;
                if(hasFocus){
                    //Bring the view to ViewContainer`s front . such as ViewGroup`s bringChildToFront(View) ;
                    v.bringToFront() ;
                    v.setAnimation(animIn);
                    v.startAnimation(animIn);
                }else{
                    v.setAnimation(animOut);
                    v.startAnimation(animOut);
                }
            }
        });
    }

   
}
