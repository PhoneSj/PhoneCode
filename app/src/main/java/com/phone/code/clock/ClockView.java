package com.phone.code.clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Phone on 2017/4/11.
 */

public class ClockView extends View {

	private int radius;
	private Path path;
	private Paint paint;
	private String words = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	float startAngle = 120;
	float sweepAngle = 300;

	public ClockView(Context context) {
		this(context, null);
	}

	public ClockView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr, 0);
		init();
	}


	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 3;
		path.addArc(getMeasuredWidth() / 2 - radius, getMeasuredHeight() / 2 - radius, getMeasuredWidth() / 2 + radius,
				getMeasuredHeight() / 2 + radius, startAngle, sweepAngle);
		invalidate();
	}

	private void init() {
		path = new Path();
		path.addArc(getMeasuredWidth() / 2 - radius, getMeasuredHeight() / 2 - radius, getMeasuredWidth() / 2 + radius,
				getMeasuredHeight() / 2 + radius, startAngle, sweepAngle);
		paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		paint.setTextSize(40);
		paint.setLetterSpacing(1);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//		for (int i = 0; i < words.length(); i++) {
		//            canvas.drawPath(path,paint);
		//		}
		canvas.drawPath(path,paint);
		canvas.drawTextOnPath(words,path,0,0,paint);
		//		canvas.drawArc(getMeasuredWidth() / 2 - radius, () / 2 - radius,
		//				getMeasuredWidth() / 2 + radius, getMeasuredHeight() / 2 + radius, startAngle, sweepAngle, false,
		//				paint);

	}
}
