package com.tv.ui.metro.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;



import java.util.ArrayList;
import java.util.List;


public class VerticalMarqueeView extends View {
    public static final int DURATION_SCROLL = 8000;
    public static final int DURATION_ANIMATOR = 1000;
    private int color = Color.WHITE;
    private int textSize = 40;

    private String[] datas =new String[]{
            "南海又开始动荡了","菲律宾到处都在肇事","这次为了一张审判废纸，菲律宾投入了多少成本呢","测试数据4","测试数据5为了长度不一样","就把这条当做测试数据吧"  };
    private int sp2px(Context context, int sp){
        float density = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * density + 0.5f);  }

    private int width;
    private int height;
    private int centerX;
    private int centerY;

    private List<TextBlock> blocks = new ArrayList<TextBlock>(2);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Handler handler = new Handler();
    private boolean isStopScroll = false;

    public VerticalMarqueeView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    public VerticalMarqueeView(Context context, AttributeSet attrs){

        super(context, attrs);
        startScroll();
        commit();
    }

    public VerticalMarqueeView(Context context){

        super(context);
        }
public VerticalMarqueeView color(int color){
        this.color = color;
        return this;
        }
public VerticalMarqueeView textSize(int textSize){
        this.textSize = textSize;
        return this;
        }
public VerticalMarqueeView datas(String[] datas){
        this.datas = datas;
        return this;
        }
public void commit(){
        if(this.datas == null || datas.length == 0){
        Log.e("VerticalMarqueeView", "the datas's length is illegal");
        throw new IllegalStateException("may be not invoke the method named datas(String[])");
        }
        paint.setColor(getResources().getColor(android.R.color.black));
        paint.setTextSize(sp2px(getContext(), 35));
        }
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(this.datas == null || this.datas.length == 0){
        Log.e("VerticalMarqueeView", "the datas's length is illegal");
        return;
        }
        width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();   centerX = width / 2;   centerY = height / 2;   blocks.clear();
//添加显示区域的文字块
        TextBlock block1 = new TextBlock(width, height, paint);
        block1.reset(datas[0], centerX, centerY, 0);
        blocks.add(block1);
        if(datas.length > 1){
             TextBlock block2 = new TextBlock(width, height, paint);
             block2.reset(datas[1], centerX, centerY + height, 1);
              blocks.add(block2);
        }
        }
@Override
protected void onDraw(Canvas canvas){
        for(int i = 0; i < blocks.size(); i++){
        blocks.get(i).draw(canvas);
        }
        }
public void startScroll(){
        isStopScroll = false;
        if(datas == null || datas.length == 0 || datas.length == 1){
        Log.e("VerticalMarqueeView", "the datas's length is illegal");
        return;
        }
        if(!isStopScroll){    handler.postDelayed(new Runnable(){
        @Override
        public void run(){
        scroll();
        if(!isStopScroll){
        handler.postDelayed(this, DURATION_SCROLL);
        }
        }
        }, DURATION_SCROLL);
                }
                   }
      public void stopScroll(){
        this.isStopScroll = true;
        }
      private void scroll(){
        ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofInt("scrollY", centerY, centerY - height));
        animator.setDuration(DURATION_ANIMATOR);
        animator.addUpdateListener(new AnimatorUpdateListener(){
          @Override
          public void onAnimationUpdate(ValueAnimator animation){
        int scrollY = (Integer)animation.getAnimatedValue("scrollY");
        blocks.get(0).reset(scrollY);
        blocks.get(1).reset(scrollY + height);
        invalidate();
        }
        });
        animator.addListener(new AnimatorListener(){
         @Override
         public void onAnimationStart(Animator animation){

        }
        @Override
        public void onAnimationRepeat(Animator animation){

        }
       @Override   public void onAnimationEnd(Animator animation){
      //移除第一块
       int position = blocks.get(1).getPosition();
        TextBlock textBlock = blocks.remove(0);
//最后一个
         if(position == datas.length - 1){
          position = 0;
        }else{
        position ++;
        }
         textBlock.reset(datas[position], centerY + height, position);
        blocks.add(textBlock);     invalidate();
        }
        @Override
        public void onAnimationCancel(Animator animation){

        }
        });
        animator.start();  }
public int getCurrentPosition(){
        if(datas == null || datas.length == 0){    return -1;
        }
        if(datas.length == 1 && blocks.size() == 1){
        return 0;
        }
        return blocks.get(0).getPosition();
        }
      public class TextBlock {
          private int width;
          private int height;
          private String text;
          private int drawX;
          private int drawY;
          private Paint paint;
          private int position;
          public TextBlock(int width, int height, Paint paint){
              this.width = width;
              this.height = height;
              this.paint = paint;
          }
          public void reset(int centerY){
              reset(text, centerX, centerY, position);
          }
          public void reset(String text, int centerY){
              reset(text, centerX, centerY, position);
          }
          public void reset(String text, int centerY, int position){
              reset(text, centerX, centerY, position);
          }
          public void reset(String text, int centerX, int centerY, int position){
              this.text = text;
              this.position = position;
              int measureWidth = (int)paint.measureText(text);
              drawX = (width - measureWidth) / 2;
              FontMetrics metrics = paint.getFontMetrics();
              drawY = (int)(centerY + (metrics.bottom - metrics.top) / 2 - metrics.bottom);
               }
          public int getPosition(){
              return position;
          }
          public void draw(Canvas canvas){
              canvas.drawText(text, drawX, drawY, paint);
          }
      }



}
