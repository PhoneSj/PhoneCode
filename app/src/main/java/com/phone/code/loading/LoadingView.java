package com.phone.code.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.phone.code.R;

import java.util.ArrayList;
import java.util.List;

public class LoadingView extends View {

    private static final int DEFAULT_BALL_COUNT=5;
    private static final int DEFAULT_BALL_COLOR=0xff6666ff;
    private static final int DEFAULT_BALL_RADIUS=5;
    private static final int DEFAULT_RADIUS=20;
    private static final int DEFAULT_DURATION=2000;


    //View的中心
    private Point mCenter;
    //主球ball的中心的当前位置
    private Point mCurrent;
    //主球运动轨迹的半径
    private int mRadius=DEFAULT_RADIUS;
    //主球的半径
    private int mBallRadius =dp2px(DEFAULT_BALL_RADIUS);
    //球的数量
    private int mBallCount =dp2px(DEFAULT_BALL_COUNT);
    //ball的颜色
    private int mBallColor = 0xff6666ff;
    private int mDuration =DEFAULT_DURATION;
    //主球当前运动的进度
    private float mFraction;
    //球集合
    private List<Ball> mBalls;
    private Path mPath;
    //第一个球与最后一个球在轨迹中展开的最大角度
    private double mMaxDeltaAngle=Math.PI;
    private Paint mBallPaint;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        mBallCount=a.getInt(R.styleable.LoadingView_ballCount,DEFAULT_BALL_COUNT);
        mBallColor=a.getColor(R.styleable.LoadingView_ballColor,DEFAULT_BALL_COLOR);
        mBallRadius= (int) a.getDimension(R.styleable.LoadingView_ballRadius, DEFAULT_BALL_RADIUS);
        mRadius= (int) a.getDimension(R.styleable.LoadingView_radius,DEFAULT_RADIUS);
        mDuration=a.getInt(R.styleable.LoadingView_duration,DEFAULT_DURATION);
        mMaxDeltaAngle=Math.PI/180*a.getInt(R.styleable.LoadingView_maxDeltaAngle,180);
        if(mDuration<=0){
            mDuration=DEFAULT_DURATION;
        }
        init();
    }

    private void init() {
        mBallPaint =new Paint(Paint.ANTI_ALIAS_FLAG);
        mBallPaint.setStyle(Paint.Style.FILL);
        mBallPaint.setColor(mBallColor);
        mCenter =new Point();
        mCurrent =new Point();
        mBalls =new ArrayList<>();
        mPath =new Path();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.i("phoneTest",".......=============================");
        mCenter.x=(getMeasuredWidth()-getPaddingLeft()-getPaddingRight())/2;
        mCenter.y=(getMeasuredHeight()-getPaddingTop()-getPaddingBottom())/2;
//        if(mRadius==DEFAULT_RADIUS){
//            mRadius =Math.min(getMeasuredWidth()-getPaddingLeft()-getPaddingRight(),
//                    getMeasuredHeight()-getPaddingTop()-getPaddingBottom())/3;
//        }
//        if(mBallRadius==DEFAULT_BALL_RADIUS||mBallRadius>=mRadius/3){
//            mBallRadius=mRadius/8;
//        }
        mCurrent.x= mCenter.x;
        mCurrent.y= mCenter.y- mRadius;
        if(mBalls !=null){
            mBalls.clear();
        }
        for(int i = 0; i< mBallCount; i++){
            Ball ball=new Ball();
            ball.setColor(mBallColor);
            ball.setRadius(getRadius(i));
            ball.setCenter(new Point(mCurrent.x, mCurrent.y));
            mBalls.add(ball);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i = 0; i< mBalls.size(); i++){
            mBalls.get(i).draw(canvas, mBallPaint);

            if(i+1< mBalls.size()){
                drawContactBezier(canvas, mBalls.get(i), mBalls.get(i+1));
                drawUncontacBezier(canvas, mBalls.get(i), mBalls.get(i+1));
            }
        }
    }

    /**
     * 绘制两球不连接时的贝塞尔曲线
     * @param canvas
     * @param ballA
     * @param ballB
     */
    private void drawUncontacBezier(Canvas canvas,Ball ballA,Ball ballB) {
        Point pointA=ballA.getCenter();
        Point pointB=ballB.getCenter();
        int radiusA=ballA.getRadius();
        int radiusB=ballB.getRadius();

        double angle = Math.atan((pointA.y - pointB.y) * 1.0d / (pointA.x - pointB.x));
        float sinValue = (float) Math.sin(angle);
        float cosValue = (float) Math.cos(angle);
        //两个球径向的辅助点
        PointF pointF1 = new PointF(pointA.x - radiusA * sinValue, pointA.y + radiusA *
                cosValue);
        PointF pointF2 = new PointF(pointB.x - radiusB * sinValue, pointB.y + radiusB *
                cosValue);
        PointF pointF3 = new PointF(pointB.x + radiusB * sinValue, pointB.y - radiusB *
                cosValue);
        PointF pointF4 = new PointF(pointA.x + radiusA * sinValue, pointA.y - radiusA *
                cosValue);
        mPath.reset();

        //圆心距离
        int distance= calculateDistance(pointA,pointB);
        if(distance<(radiusA+radiusB)*2){
            float progress=0.8f;//决定控制点的位置，当等于1.0f时，控制点为另一个圆心，当等于0时，控制点为自己的圆心
            PointF controlA=new PointF();
            PointF controlB=new PointF();
            controlA.x=(pointB.x-pointA.x)*progress+pointA.x;
            controlA.y=(pointB.y-pointA.y)*progress+pointA.y;
            controlB.x=(pointA.x-pointB.x)*progress+pointB.x;
            controlB.y=(pointA.y-pointB.y)*progress+pointB.y;

            mPath.moveTo(pointF1.x, pointF1.y);
            mPath.quadTo(controlA.x, controlA.y, pointF4.x, pointF4.y);
            mPath.lineTo(pointF1.x, pointF1.y);

            mPath.moveTo(pointF2.x, pointF2.y);
            mPath.quadTo(controlB.x, controlB.y, pointF3.x, pointF3.y);
            mPath.lineTo(pointF2.x, pointF2.y);
            mPath.close();
            canvas.drawPath(mPath, mBallPaint);
        }
    }

    /**
     * 绘制两球连接时的贝塞尔曲线
     * @param canvas
     * @param ballA
     * @param ballB
     */
    private void drawContactBezier(Canvas canvas,Ball ballA,Ball ballB) {
        Point pointA=ballA.getCenter();
        Point pointB=ballB.getCenter();
        int radiusA=ballA.getRadius();
        int radiusB=ballB.getRadius();

        double angle = Math.atan((pointA.y - pointB.y) * 1.0d / (pointA.x - pointB.x));
        float sinValue = (float) Math.sin(angle);
        float cosValue = (float) Math.cos(angle);
        //两个球在径向的辅助点
        PointF pointF1 = new PointF(pointA.x - radiusA * sinValue, pointA.y + radiusA *
                cosValue);
        PointF pointF2 = new PointF(pointB.x - radiusB * sinValue, pointB.y + radiusB *
                cosValue);
        PointF pointF3 = new PointF(pointB.x + radiusB * sinValue, pointB.y - radiusB *
                cosValue);
        PointF pointF4 = new PointF(pointA.x + radiusA * sinValue, pointA.y - radiusA *
                cosValue);
        mPath.reset();

        //圆心距离
        int distance= calculateDistance(pointA,pointB);
        if (distance>=Math.abs(ballA.getRadius()-ballB.getRadius())&&distance<(radiusA+radiusB)
                *1.5) {
            mPath.moveTo(pointF1.x, pointF1.y);
            mPath.quadTo((pointA.x + pointB.x) / 2, (pointA.y + pointB.y) / 2, pointF2.x,
                    pointF2.y);
            mPath.lineTo(pointF3.x, pointF3.y);
            mPath.quadTo((pointA.x + pointB.x) / 2, (pointA.y + pointB.y) / 2, pointF4.x,
                    pointF4.y);
            mPath.lineTo(pointA.x, pointA.y);
            mPath.close();
            canvas.drawPath(mPath, mBallPaint);
        }
    }

    /**
     * 计算两点之间的距离
     * @param pointA
     * @param pointB
     * @return
     */
    public int calculateDistance(Point pointA, Point pointB) {
        float dx=Math.abs(pointA.x-pointB.x);
        float dy=Math.abs(pointA.y-pointB.y);
        return (int) Math.sqrt(dx*dx+dy*dy);
    }

    /**
     * 计算小圆与大圆之间的夹角
     * @param index
     * @return
     */
    private double calculateDeltaAngle(int index) {
        double originAngle=index*mMaxDeltaAngle/ mBallCount *Math.sin(mFraction *Math.PI);
        return originAngle;
    }

    /**
     * 计算大圆相对控件中心点的角度
     * @return
     */
    private double calculateCurrentAngle() {
        return 2*Math.PI*(mFraction -0.25f);
    }

    private int getRadius(int index) {
        return mBallRadius *(mBallCount -index)/ mBallCount;
    }

    public void startAnim(){
        ValueAnimator animator=ValueAnimator.ofFloat(0f,1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mFraction =valueAnimator.getAnimatedFraction();
                double currentAngle=calculateCurrentAngle();
                for(int i = 0; i< mBallCount; i++){
                    double deltaAngle=calculateDeltaAngle(i);
                    double angle=currentAngle-deltaAngle;
                    angle=angle<-Math.PI/2?-Math.PI/2:angle;
                    //小球与大球之间的夹角也随fraction变化
                    mBalls.get(i).getCenter().x= (int) (mCenter.x+ mRadius *Math.cos(angle));
                    mBalls.get(i).getCenter().y= (int) (mCenter.y+ mRadius *Math.sin(angle));
                }
                invalidate();
                Log.i("phoneTest","mFraction:"+ mFraction);
                Log.i("phoneTest","radius:"+ mRadius);
                Log.i("phoneTest","ballRadius:"+ mBallRadius);
            }
        });
        animator.setDuration(mDuration);
        animator.start();
    }

    private int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
