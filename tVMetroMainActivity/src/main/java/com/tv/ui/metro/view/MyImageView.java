package com.tv.ui.metro.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tv.ui.metro.R;

/**
 * Created by Lenovo on 2017/3/2.
 */
public class MyImageView extends ImageView {

    private int co;
    private int borderwidth;
    public MyImageView(Context context) {
        super(context);
    }
    public MyImageView(Context context, AttributeSet attrs,
                       int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //设置颜色
    public void setColour(int color){
        co = color;
    }
    //设置边框宽度
    public void setBorderWidth(int width){

        borderwidth = width;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画边框
        Rect rec = canvas.getClipBounds();
        rec.bottom--;
        rec.top++;
        rec.left++;
        rec.right--;
        Paint paint = new Paint();
        //设置边框颜色
       paint.setColor(co);
       paint.setStyle(Paint.Style.STROKE);
        //设置边框宽度
        paint.setStrokeWidth(borderwidth);
        canvas.drawRect(rec, paint);
//        Bitmap  bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.tab_highlight);
//        bmp.get
//        canvas.drawBitmap();
  }
}
