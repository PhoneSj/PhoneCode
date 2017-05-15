package com.phone.code.ball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Phone on 2017/5/12.
 */

public class FloatView extends View {

    private int color=0x88000000;
    private RectF rectFA;
    private RectF rectFB;
    private Paint textPaint;
    private PointF center;
    private RadialGradient radialGradient;

    public FloatView(Context context) {
        this(context,null);
    }

    public FloatView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(color);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        center=new PointF((getMeasuredWidth()+getPaddingLeft()-getPaddingRight())/2,
                (getMeasuredHeight()+getPaddingTop()-getPaddingBottom())/2);
        rectFA=new RectF(center.x-100,center.y-100,center.x+100,center.y+100);
        rectFB=new RectF(center.x-200,center.y-100,center.x+200,center.y+100);
        radialGradient=new RadialGradient(center.x,center.y,100,0xaa000000,0x33000000, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(rectFA!=null){
            canvas.drawArc(rectFA,90,180,false,textPaint);
            //        canvas.drawArc(rectFB,-90,180,false,textPaint);
            canvas.scale(2.0f,1.0f,center.x,center.y);
            textPaint.setShader(radialGradient);
            canvas.drawArc(rectFA,-90,180,false,textPaint);
        }
    }
}
