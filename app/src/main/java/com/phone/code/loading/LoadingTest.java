package com.phone.code.loading;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.phone.code.R;

public class LoadingTest extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    public void click(View view){
        if(view instanceof LoadingView){
            ((LoadingView) view).startAnim();
        }
    }
}
