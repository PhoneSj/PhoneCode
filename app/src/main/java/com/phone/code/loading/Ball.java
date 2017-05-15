package com.phone.code.loading;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class Ball {

    private Point center;
    private int radius;
    private int color= Color.BLUE;

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(color);
        canvas.drawCircle(center.x,center.y,radius,paint);
    }
}
