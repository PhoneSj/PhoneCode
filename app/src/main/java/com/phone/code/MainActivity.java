package com.phone.code;

import com.phone.code.ball.BallTest;
import com.phone.code.bezier.BezierTest;
import com.phone.code.loading.LoadingTest;
import com.phone.code.wave.WaveTest;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

    private String titles[] = {"wave", "bezier", "ball", "loading"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getListView().setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(this, WaveTest.class);
                break;
            case 1:
                intent = new Intent(this, BezierTest.class);
                break;
            case 2:
                intent = new Intent(this, BallTest.class);
                break;
            case 3:
                intent = new Intent(this, LoadingTest.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
