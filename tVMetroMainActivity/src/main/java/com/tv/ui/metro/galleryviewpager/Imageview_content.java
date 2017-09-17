package com.tv.ui.metro.galleryviewpager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * Created by Lenovo on 2017/4/17.
 */
public class Imageview_content extends ImageView {
// 控件默认长、宽
    private int defaultWidth = 0;
    private int defaultHeight = 0;
// 比例
    private float scale = 0;
    public Imageview_content(Context context) {
        super(context);
    }
    public Imageview_content(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public Imageview_content(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        this.measure(0, 0);
        if (drawable.getClass() == NinePatchDrawable.class)
            return;
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();

        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        if (bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {
            return;
        }
        if (defaultWidth == 0) {
            defaultWidth = getWidth();
        }
        if (defaultHeight == 0) {
            defaultHeight = getHeight();
        }
        scale = (float) defaultHeight / (float) bitmap.getHeight();
        defaultWidth = Math.round(bitmap.getWidth() * scale);
        LayoutParams params = this.getLayoutParams();
        params.width = defaultWidth;
        params.height = defaultHeight;
        this.setLayoutParams(params);
        super.onDraw(canvas);
    }
}

