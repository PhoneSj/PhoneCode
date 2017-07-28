package com.phone.code.ball;

import com.phone.code.loading.Ball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Phone on 2017/4/28.
 */

public class BallView extends View {

    private Paint mPaint;
    private Paint textPaint;
    //    private PointF centerPoint;
    //    private PointF touchPoint;
    private boolean enableDraw;
    private Path mPath;
    private Ball ball0;
    private Ball ball1;

    public BallView(Context context) {
        this(context, null);
    }

    public BallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLUE);
        textPaint.setStyle(Paint.Style.FILL);
        //        centerPoint = new PointF();
        //        touchPoint = new PointF();
        mPath = new Path();

        ball0 = new Ball();
        ball1 = new Ball();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ball0.setCenter(new Point(w / 2, h / 2));
        ball0.setRadius(150);
        ball0.setColor(Color.RED);

        ball1.setCenter(new Point(w / 2, h / 2));
        ball1.setRadius(100);
        ball1.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        //        centerPoint.set(w / 2, h / 2);
        int radius = Math.min(w, h) / 10;
        //        canvas.drawCircle(centerPoint.x, centerPoint.y, radius, mPaint);
        canvas.drawCircle(ball0.getCenter().x, ball0.getCenter().y, ball0.getRadius(), mPaint);
        canvas.drawCircle(ball1.getCenter().x, ball1.getCenter().y, ball1.getRadius(), mPaint);
        drawBezier(canvas, ball0, ball1);
        //        if (enableDraw) {
        //            Log.i("phoneTest", "touch point x:" + touchPoint.x + " y:" + touchPoint.y);
        //            canvas.drawCircle(touchPoint.x, touchPoint.y, radius, paint);
        //            double angle = Math.atan((touchPoint.y - centerPoint.y) * 1.0d / (touchPoint.x - centerPoint.x));
        //            float sinValue = (float) Math.sin(angle);
        //            float cosValue = (float) Math.cos(angle);
        //            PointF pointF1 = new PointF(touchPoint.x - radius * sinValue, touchPoint.y + radius * cosValue);
        //            PointF pointF2 = new PointF(centerPoint.x - radius * sinValue, centerPoint.y + radius * cosValue);
        //            PointF pointF3 = new PointF(centerPoint.x + radius * sinValue, centerPoint.y - radius * cosValue);
        //            PointF pointF4 = new PointF(touchPoint.x + radius * sinValue, touchPoint.y - radius * cosValue);
        //            path.reset();
        //            path.moveTo(pointF1.x, pointF1.y);
        //            path.quadTo(centerPoint.x, centerPoint.y, pointF4.x, pointF4.y);
        //            path.lineTo(pointF1.x, pointF1.y);
        //
        //            path.moveTo(pointF2.x, pointF2.y);
        //            path.quadTo(touchPoint.x, touchPoint.y, pointF3.x, pointF3.y);
        //            path.lineTo(pointF2.x, pointF2.y);
        //            path.close();
        //            canvas.drawPath(path, paint);
        //
        //            if (getDistance() < radius * 6) {
        //                path.moveTo(pointF1.x, pointF1.y);
        //                path.quadTo((touchPoint.x + centerPoint.x) / 2, (touchPoint.y + centerPoint.y) / 2, pointF2.x, pointF2.y);
        //                path.lineTo(pointF3.x, pointF3.y);
        //                path.quadTo((touchPoint.x + centerPoint.x) / 2, (touchPoint.y + centerPoint.y) / 2, pointF4.x, pointF4.y);
        //                path.lineTo(touchPoint.x, touchPoint.y);
        //                path.close();
        //                canvas.drawPath(path, paint);
        //            }
        //        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                enableDraw = true;
                float x = event.getX();
                float y = event.getY();
                //                touchPoint.set(x, y);
                ball1.setCenter(new Point((int) x, (int) y));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                enableDraw = false;
                //                touchPoint.set(0, 0);
                ball1.setCenter(new Point(0, 0));
                break;
        }
        invalidate();
        return true;
    }

    private void drawBezier(Canvas canvas, Ball startBall, Ball endBall) {
        float x = endBall.getCenter().x;
        float y = endBall.getCenter().y;
        float startX = startBall.getCenter().x;
        float startY = startBall.getCenter().y;

        //两球心间距离
        float distance = (float) Math.sqrt((x - startX) * (x - startX) + (y - startY) * (y - startY));
        float fraction = ball0.getRadius() * 1.0f / (ball0.getRadius() + ball1.getRadius());
        float controlX = startX + (x - startX) * fraction;
        float controlY = startY + (y - startY) * fraction;

        //起始球的球心到控制点的距离
        float startDistance = startBall.getRadius() * 1.0f / (startBall.getRadius() + endBall.getRadius()) * distance;
        //终止球的球心到控制点的距离
        float endDistance = distance - startDistance;

        //计算起始球与贝塞尔曲线的相切点
        double startAngle = Math.acos(startBall.getRadius() / startDistance);
        //计算终止球与贝塞尔曲线的相切点
        double endAngle = Math.acos(endBall.getRadius() / startDistance);

        int dx = ball1.getCenter().x - ball0.getCenter().x;
        int dy = ball1.getCenter().y - ball0.getCenter().y;
        if (dx * dy >= 0) {

            double b = Math.acos(Math.abs(controlX - startX) / startDistance);
            b = getHH(dy, b);
            float offsetX1 = (float) (startBall.getRadius() * Math.cos(startAngle - b));
            float offsetY1 = (float) (startBall.getRadius() * Math.sin(startAngle - b));
            float tanX1 = startX + offsetX1;
            float tanY1 = startY - offsetY1;

            double c = Math.acos(Math.abs(controlY - startY) / startDistance);
            c = getHH(dy, c);
            float offsetX2 = (float) (startBall.getRadius() * Math.sin(startAngle - c));
            float offsetY2 = (float) (startBall.getRadius() * Math.cos(startAngle - c));
            float tanX2 = startX - offsetX2;
            float tanY2 = startY + offsetY2;

            double d = Math.acos(Math.abs(y - controlY) / endDistance);
            d = getHH(dy, d);
            float offsetX3 = (float) (endBall.getRadius() * Math.sin(endAngle - d));
            float offsetY3 = (float) (endBall.getRadius() * Math.cos(endAngle - d));
            float tanX3 = x + offsetX3;
            float tanY3 = y - offsetY3;

            double e = Math.acos(Math.abs(x - controlX) / endDistance);
            e = getHH(dy, e);
            float offsetX4 = (float) (endBall.getRadius() * Math.cos(endAngle - e));
            float offsetY4 = (float) (endBall.getRadius() * Math.sin(endAngle - e));
            float tanX4 = x - offsetX4;
            float tanY4 = y + offsetY4;

            mPath.reset();
            mPath.moveTo(tanX1, tanY1);
            mPath.quadTo(controlX, controlY, tanX3, tanY3);
            mPath.lineTo(tanX4, tanY4);
            mPath.quadTo(controlX, controlY, tanX2, tanY2);
            mPath.close();
            canvas.drawPath(mPath, mPaint);

            // 辅助线
            canvas.drawCircle(tanX1, tanY1, 5, textPaint);
            canvas.drawCircle(tanX2, tanY2, 5, textPaint);
            canvas.drawCircle(tanX3, tanY3, 5, textPaint);
            canvas.drawCircle(tanX4, tanY4, 5, textPaint);
            canvas.drawCircle(controlX, controlY, 5, textPaint);
            canvas.drawLine(ball0.getCenter().x, ball0.getCenter().y, ball1.getCenter().x, ball1.getCenter().y, textPaint);
            canvas.drawLine(ball0.getCenter().x, ball0.getCenter().y, tanX1, tanY1, textPaint);
            canvas.drawLine(ball0.getCenter().x, ball0.getCenter().y, tanX2, tanY2, textPaint);
            canvas.drawLine(ball1.getCenter().x, ball1.getCenter().y, tanX3, tanY3, textPaint);
            canvas.drawLine(ball1.getCenter().x, ball1.getCenter().y, tanX4, tanY4, textPaint);
            canvas.drawLine(controlX, controlY, tanX1, tanY1, textPaint);
            canvas.drawLine(controlX, controlY, tanX2, tanY2, textPaint);
            canvas.drawLine(controlX, controlY, tanX3, tanY3, textPaint);
            canvas.drawLine(controlX, controlY, tanX4, tanY4, textPaint);

        } else {
            double b = Math.acos(Math.abs(controlX - startX) / startDistance);
            b = getHH(dy, b);
            float offsetX1 = (float) (startBall.getRadius() * Math.cos(startAngle - b));
            float offsetY1 = (float) (startBall.getRadius() * Math.sin(startAngle - b));
            float tanX1 = startX - offsetX1;
            float tanY1 = startY - offsetY1;

            double c = Math.acos(Math.abs(controlY - startY) / startDistance);
            c = getHH(dy, c);
            float offsetX2 = (float) (startBall.getRadius() * Math.sin(startAngle - c));
            float offsetY2 = (float) (startBall.getRadius() * Math.cos(startAngle - c));
            float tanX2 = startX + offsetX2;
            float tanY2 = startY + offsetY2;

            double d = Math.acos(Math.abs(y - controlY) / endDistance);
            d = getHH(dy, d);
            float offsetX3 = (float) (endBall.getRadius() * Math.sin(endAngle - d));
            float offsetY3 = (float) (endBall.getRadius() * Math.cos(endAngle - d));
            float tanX3 = x - offsetX3;
            float tanY3 = y - offsetY3;

            double e = Math.acos(Math.abs(x - controlX) / endDistance);
            e = getHH(dy, e);
            float offsetX4 = (float) (endBall.getRadius() * Math.cos(endAngle - e));
            float offsetY4 = (float) (endBall.getRadius() * Math.sin(endAngle - e));
            float tanX4 = x + offsetX4;
            float tanY4 = y + offsetY4;

            mPath.reset();
            mPath.moveTo(tanX1, tanY1);
            mPath.quadTo(controlX, controlY, tanX3, tanY3);
            mPath.lineTo(tanX4, tanY4);
            mPath.quadTo(controlX, controlY, tanX2, tanY2);
            mPath.close();
            canvas.drawPath(mPath, mPaint);

            // 辅助线
            canvas.drawCircle(tanX1, tanY1, 5, textPaint);
            canvas.drawCircle(tanX2, tanY2, 5, textPaint);
            canvas.drawCircle(tanX3, tanY3, 5, textPaint);
            canvas.drawCircle(tanX4, tanY4, 5, textPaint);
            canvas.drawCircle(controlX, controlY, 5, textPaint);
            canvas.drawLine(ball0.getCenter().x, ball0.getCenter().y, ball1.getCenter().x, ball1.getCenter().y, textPaint);
            canvas.drawLine(ball0.getCenter().x, ball0.getCenter().y, tanX1, tanY1, textPaint);
            canvas.drawLine(ball0.getCenter().x, ball0.getCenter().y, tanX2, tanY2, textPaint);
            canvas.drawLine(ball1.getCenter().x, ball1.getCenter().y, tanX3, tanY3, textPaint);
            canvas.drawLine(ball1.getCenter().x, ball1.getCenter().y, tanX4, tanY4, textPaint);
            canvas.drawLine(controlX, controlY, tanX1, tanY1, textPaint);
            canvas.drawLine(controlX, controlY, tanX2, tanY2, textPaint);
            canvas.drawLine(controlX, controlY, tanX3, tanY3, textPaint);
            canvas.drawLine(controlX, controlY, tanX4, tanY4, textPaint);
        }
    }

    private double getHH(int dy, double angle) {
        if (dy < 0) {
            return angle + Math.PI;
        } else {
            return angle;
        }
    }

    //    private void drawBezier(Canvas canvas, Ball startBall, Ball endBall) {
    //        float x = endBall.getCenter().x;
    //        float y = endBall.getCenter().y;
    //        float startX = startBall.getCenter().x;
    //        float startY = startBall.getCenter().y;
    //        //        float controlX = (startX + x) / 2;
    //        //        float controlY = (startY + y) / 2;
    //
    //        //两球心间距离
    //        //        float distance = (float) Math.sqrt((controlX - startX) * (controlX - startX) + (controlY - startY) * (controlY - startY));
    //        float distance = (float) Math.sqrt((x - startX) * (x - startX) + (y - startY) * (y - startY));
    //        float fraction = ball0.getRadius() * 1.0f / (ball0.getRadius() + ball1.getRadius());
    //        float controlX = startX + (x - startX) * fraction;
    //        float controlY = startY + (y - startY) * fraction;
    //
    //        //起始球的球心到控制点的距离
    //        float startDistance = startBall.getRadius() * 1.0f / (startBall.getRadius() + endBall.getRadius()) * distance;
    //        //终止球的球心到控制点的距离
    //        float endDistance = distance - startDistance;
    //
    //        double offset = 0;
    //        if (x - startX > 0 && y - startY > 0) {
    //            offset = 0;
    //        } else if (x - startX < 0 && y - startY > 0) {
    //            offset = Math.PI / 2;
    //        } else if (x - startX < 0 && y - startY < 0) {
    //            offset = Math.PI;
    //        } else if (x - startX > 0 && y - startY < 0) {
    //            offset = Math.PI / 2 * 3;
    //        }
    //
    //        //计算起始球与贝塞尔曲线的相切点
    //        double startAngle = Math.acos(startBall.getRadius() / startDistance);
    //
    //        double b = Math.acos((controlX - startX) / startDistance) + offset;
    //        //        b = (b + Math.PI * 2 + offset) % (Math.PI / 2);
    //        float offsetX1 = (float) (startBall.getRadius() * Math.cos(startAngle - b));
    //        float offsetY1 = (float) (startBall.getRadius() * Math.sin(startAngle - b));
    //        float tanX1 = startX + offsetX1;
    //        float tanY1 = startY - offsetY1;
    //
    //        double c = Math.acos((controlY - startY) / startDistance) + offset;
    //        //        c = (c + Math.PI * 2 + offset) % (Math.PI / 2);
    //        float offsetX2 = (float) (startBall.getRadius() * Math.sin(startAngle - c));
    //        float offsetY2 = (float) (startBall.getRadius() * Math.cos(startAngle - c));
    //        float tanX2 = startX - offsetX2;
    //        float tanY2 = startY + offsetY2;
    //
    //        //计算终止球与贝塞尔曲线的相切点
    //        double endAngle = Math.acos(endBall.getRadius() / startDistance);
    //
    //        double d = Math.acos((y - controlY) / endDistance) + offset;
    //        //        d = (d + Math.PI * 2 + offset) % (Math.PI / 2);
    //        float offsetX3 = (float) (endBall.getRadius() * Math.sin(endAngle - d));
    //        float offsetY3 = (float) (endBall.getRadius() * Math.cos(endAngle - d));
    //        float tanX3 = x + offsetX3;
    //        float tanY3 = y - offsetY3;
    //
    //        double e = Math.acos((x - controlX) / endDistance) + offset;
    //        //        e = (e + Math.PI * 2 + offset) % (Math.PI / 2);
    //        float offsetX4 = (float) (endBall.getRadius() * Math.cos(endAngle - e));
    //        float offsetY4 = (float) (endBall.getRadius() * Math.sin(endAngle - e));
    //        float tanX4 = x - offsetX4;
    //        float tanY4 = y + offsetY4;
    //
    //        mPath.reset();
    //        mPath.moveTo(tanX1, tanY1);
    //        mPath.quadTo(controlX, controlY, tanX3, tanY3);
    //        mPath.lineTo(tanX4, tanY4);
    //        mPath.quadTo(controlX, controlY, tanX2, tanY2);
    //        mPath.close();
    //        canvas.drawPath(mPath, mPaint);
    //
    //        //        // 辅助线
    //        canvas.drawCircle(tanX1, tanY1, 5, textPaint);
    //        canvas.drawCircle(tanX2, tanY2, 10, textPaint);
    //        canvas.drawCircle(tanX3, tanY3, 15, textPaint);
    //        canvas.drawCircle(tanX4, tanY4, 20, textPaint);
    //        canvas.drawLine(ball0.getCenter().x, ball0.getCenter().y, ball1.getCenter().x, ball1.getCenter().y, textPaint);
    //        //        canvas.drawLine(0, mCircleOneY, mCircleOneX + mRadiusNormal + 400, mCircleOneY, mPaint);
    //        //        canvas.drawLine(mCircleOneX, 0, mCircleOneX, mCircleOneY + mRadiusNormal + 50, mPaint);
    //        //        canvas.drawLine(mCircleTwoX, mCircleTwoY, mCircleTwoX, 0, mPaint);
    //        canvas.drawCircle(controlX, controlY, 5, textPaint);
    //        canvas.drawLine(startX, startY, tanX1, tanY1, textPaint);
    //        canvas.drawLine(tanX1, tanY1, controlX, controlY, textPaint);
    //    }
}
