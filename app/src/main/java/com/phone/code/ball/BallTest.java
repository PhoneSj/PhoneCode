package com.phone.code.ball;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.phone.code.R;
import com.phone.code.bezier.BezierView;

/**
 * Created by Phone on 2017/5/11.
 */

public class BallTest extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball);
    }

    public void onClear(View view){
        BezierView bezierView= (BezierView) findViewById(R.id.bezierView);
        bezierView.clear();
    }
}
