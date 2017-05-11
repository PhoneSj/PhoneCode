package com.phone.code.wave;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Phone on 2017/1/11.
 */

public class WaveView extends View {

    private int waveLength = 1600;
    private int waveHeight = 100;
    private int originY;
    private Path path;
    private Paint paint;
    private int deltaX;
    private int deltaY;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        //该动画起作用，换成Runnable实现
        startAnim();
        post(new Runnable() {
            @Override
            public void run() {
                deltaX += 5;
                deltaX=deltaX>waveLength?0:deltaX;
                deltaY = (int) (deltaX * 1.0f / waveLength * waveHeight);
                if (deltaX <= waveLength / 2) {
                    deltaY = (int) (50 * deltaY * 2.0f / waveLength);
                } else {
                    deltaY = (int) (50 * (waveLength - deltaY) * 2.0f / waveLength);
                }
                postInvalidate();
                post(this);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();//重置路径，清空路径
        originY = getHeight() / 2;
        int helfWave = waveLength / 2;
        path.moveTo(-waveLength + deltaX, originY);
        for (int x = -waveLength; x <= getWidth() + waveLength; x += waveLength) {
            path.rQuadTo(helfWave / 2, waveHeight + deltaY, helfWave, 0);
            path.rQuadTo(helfWave / 2, -waveHeight - deltaY, helfWave, 0);
        }
        path.lineTo(getWidth(), getHeight());
        path.lineTo(0, getHeight());
        path.close();
        canvas.drawPath(path, paint);
    }

    private void startAnim() {
        Log.i("phoneTest","startAnim...");
        ValueAnimator animator = ValueAnimator.ofInt(0, waveLength);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.i("phoneTest","===...");
                deltaX = (int) valueAnimator.getAnimatedValue();
                deltaY = (int) (deltaX * 1.0f / waveLength * waveHeight);
                if (deltaX <= waveLength / 2) {
                    deltaY = (int) (50 * deltaY * 2.0f / waveLength);
                } else {
                    deltaY = (int) (50 * (waveLength - deltaY) * 2.0f / waveLength);
                }
                postInvalidate();
            }
        });
        animator.setDuration(5000);
        animator.start();
        animator.setRepeatCount(Integer.MAX_VALUE);
    }
}
