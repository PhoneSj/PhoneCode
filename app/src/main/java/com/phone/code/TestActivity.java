package com.phone.code;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Phone on 2017/5/15.
 */

public class TestActivity extends Activity {

    WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        // 获取Service
        WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        // 设置窗口类型，一共有三种Application windows, Sub-windows, System windows
        // API中以TYPE_开头的常量有23个
        mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 设置期望的bitmap格式
        mWindowParams.format = PixelFormat.RGBA_8888;

        // 以下属性在Layout Params中常见重力、坐标，宽高
        mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowParams.x = 100;
        mWindowParams.y = 100;

        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // 自定义图片视图
        MoveImageView imageView = new MoveImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);
        imageView.setFloatViewParamsListener(new FloatViewListener());

        // 添加指定视图
        mWindowManager.addView(imageView, mWindowParams);



    }

    private class FloatViewListener implements MoveImageView.FloatViewParamsListener {
        @Override
        public int getTitleHeight() {
            // 获取状态栏高度。不能在onCreate回调方法中获取
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;

            int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
            int titleBarHeight = contentTop - statusBarHeight;

            return titleBarHeight;
        }

        @Override
        public android.view.WindowManager.LayoutParams getLayoutParams() {
            return mWindowParams;
        }
    }
}
