package com.yzsn.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yzsn.sample.activity.TestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startTest(View view){
        startActivity(new Intent(MainActivity.this, TestActivity.class));
    }

    public void startScrollViewActivity(View view){
    }

}
