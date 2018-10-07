package com.raisin.topicset.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.raisin.topicset.R;
import com.raisin.topicset.adapter.AddViewAdapter;
import com.raisin.topicset.adapter.HomeViewAdapter;

public class AddFragment extends Fragment {

    private View view;
    // ImageButton组件
    private ImageButton ibProblem;
    // ImageButton组件
    private ImageButton ibAnswer;
    // Button组件
    private Button btnDone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add, container, false);

        // 初始化组件
        initView(view);

        return view;
    }

    private void initView(View view) {
        ibProblem = view.findViewById(R.id.ibProblem);
        ibAnswer = view.findViewById(R.id.ibAnswer);
        btnDone = view.findViewById(R.id.btnDone);
    }
}
