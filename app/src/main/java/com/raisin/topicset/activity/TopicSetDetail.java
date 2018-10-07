package com.raisin.topicset.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.raisin.topicset.R;

public class TopicSetDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
    }

    private void initView() {
        TextView textView = findViewById(R.id.tvDetail);
        String courseNm = getIntent().getStringExtra("CourseName");
        if (courseNm != null && !courseNm.isEmpty()){
            textView.setText(courseNm);
        }

    }

}
