package com.example.myapp;

/**
 * Created by xs on 14/11/25.
 */
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class WheelActivity extends Activity {

    private ListView listView = null;
    private MyViewGroup myViewGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        myViewGroup = (MyViewGroup) findViewById(R.id.myviewGroup);

    }

    public void scroll(View view) {

        myViewGroup.beginScroll();

    }

}

