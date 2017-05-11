package com.phone.code.ball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Phone on 2017/4/28.
 */

public class BallView extends View {

    private Paint paint;
    private PointF centerPoint;
    private PointF touchPoint;
    private boolean enableDraw;
    private Path path;
    private int distance;

    public BallView(Context context) {
        this(context,null);
    }

    public BallView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        centerPoint=new PointF();
        touchPoint=new PointF();
        path=new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        centerPoint.set(w / 2, h / 2);
        int radius = Math.min(w, h) / 10;
        canvas.drawCircle(centerPoint.x, centerPoint.y, radius, paint);
        if (enableDraw) {
            Log.i("phoneTest", "touch point x:" + touchPoint.x + " y:" + touchPoint.y);
            canvas.drawCircle(touchPoint.x, touchPoint.y, radius, paint);
            double angle = Math.atan((touchPoint.y - centerPoint.y) * 1.0d / (touchPoint.x - centerPoint.x));
            float sinValue = (float) Math.sin(angle);
            float cosValue = (float) Math.cos(angle);
            PointF pointF1 = new PointF(touchPoint.x - radius * sinValue, touchPoint.y + radius *
                    cosValue);
            PointF pointF2 = new PointF(centerPoint.x - radius * sinValue, centerPoint.y + radius *
                    cosValue);
            PointF pointF3 = new PointF(centerPoint.x + radius * sinValue, centerPoint.y - radius *
                    cosValue);
            PointF pointF4 = new PointF(touchPoint.x + radius * sinValue, touchPoint.y - radius *
                    cosValue);
            path.reset();
            path.moveTo(pointF1.x, pointF1.y);
            path.quadTo(centerPoint.x, centerPoint.y, pointF4.x, pointF4.y);
            path.lineTo(pointF1.x, pointF1.y);

            path.moveTo(pointF2.x, pointF2.y);
            path.quadTo(touchPoint.x, touchPoint.y, pointF3.x, pointF3.y);
            path.lineTo(pointF2.x, pointF2.y);
            path.close();
            canvas.drawPath(path, paint);


            if (getDistance() < radius * 6) {
                path.moveTo(pointF1.x, pointF1.y);
                path.quadTo((touchPoint.x + centerPoint.x) / 2, (touchPoint.y + centerPoint.y) / 2, pointF2.x,
                        pointF2.y);
                path.lineTo(pointF3.x, pointF3.y);
                path.quadTo((touchPoint.x + centerPoint.x) / 2, (touchPoint.y + centerPoint.y) / 2, pointF4.x,
                        pointF4.y);
                path.lineTo(touchPoint.x, touchPoint.y);
                path.close();
                canvas.drawPath(path, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                enableDraw=true;
                float x=  event.getX();
                float y=  event.getY();
                touchPoint.set(x,y);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                enableDraw=false;
                touchPoint.set(0,0);
                break;
        }
        invalidate();
        return true;
    }

    public double getDistance() {
        float dx=Math.abs(touchPoint.x-centerPoint.x);
        float dy=Math.abs(touchPoint.y-centerPoint.y);
        return Math.sqrt(dx*dx+dy*dy);
    }
}
