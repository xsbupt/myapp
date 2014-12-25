package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by xs on 14/12/19.
 */
public class LaunchActivity extends Activity {

    private TextView mNest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.launch);

        mNest = (TextView) findViewById(R.id.nest);

        mNest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, NestActivity.class);
                startActivity(intent);
            }
        });
    }
}
