package com.phone.code.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Phone on 2017/5/11.
 */

public class LoadingView extends View {

    private int ballColor= 0xff6666ff;
    private Paint ballPaint;
    private Point center;
    private Point current;
    private int radius;
    private int ballRadius=25;
    private int ballCount=5;
    private int duration=5000;
    private float fraction=0f;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnim();
            }
        });
    }

    private void init() {
        ballPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        ballPaint.setStyle(Paint.Style.FILL);
        ballPaint.setColor(ballColor);
        center=new Point();
        current=new Point();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        center.x=(getMeasuredWidth()-getPaddingLeft()-getPaddingRight())/2;
        center.y=(getMeasuredHeight()-getPaddingTop()-getPaddingBottom())/2;
        radius=Math.min(getMeasuredWidth(),getMeasuredHeight())/3;
        current.x=center.x;
        current.y=center.y-radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i=0;i<ballCount;i++){
            Point curCenter=getBallCenter(i);
            canvas.drawCircle(curCenter.x,curCenter.y,getRadius(i),ballPaint);
        }
    }

    /**
     * 计算各个圆的中心
     * @param index
     * @return
     */
    private Point getBallCenter(int index) {
        Point point=new Point();
        double currentAngle=calculateCurrentAngle();
        double deltaAngle=calculateDeltaAngle(index);
        double angle=currentAngle-deltaAngle;
        //小球与大球之间的夹角也随fraction变化
        point.x= (int) (center.x+radius*Math.cos(angle));
        point.y= (int) (center.y+radius*Math.sin(angle));
        return point;
    }

    /**
     * 计算小圆与大圆之间的夹角
     * @param index
     * @return
     */
    private double calculateDeltaAngle(int index) {
        return index*Math.PI/5*Math.sin(fraction*Math.PI);
    }

    /**
     * 计算大圆相对控件中心点的角度
     * @return
     */
    private double calculateCurrentAngle() {
        return 2*Math.PI*(fraction-0.25f);
    }

    private float getRadius(int index) {
        return ballRadius*(ballCount-index)/ballCount;
    }

    public void startAnim(){
        ValueAnimator animator=ValueAnimator.ofFloat(0f,1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fraction=valueAnimator.getAnimatedFraction();
                invalidate();
                Log.i("phoneTest","fraction:"+fraction);
            }
        });
        animator.setDuration(duration);
        animator.start();
    }

}
