package com.phone.code.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Phone on 2017/1/11.
 */

public class BezierView extends View {


    private Path path;
    private Paint paint;
    private float preX;
    private float preY;

    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preX = event.getX();
                preY = event.getY();
                path.moveTo(preX, preY);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.quadTo(preX, preY, event.getX(), event.getY());
                preX = event.getX();
                preY = event.getY();
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    /**
     * 清空路径内容
     */
    public void clear(){
        path.reset();
        postInvalidate();
    }
}
