package com.raisin.topicset.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.raisin.topicset.R;
import com.raisin.topicset.activity.PhotoActivity;

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
        return view;
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }


    private void initView() {
        ibProblem = view.findViewById(R.id.ibProblem);
        ibProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 开始传值
                Intent intent = new Intent(getActivity(), PhotoActivity.class);
                intent.putExtra("PhotoId", "Problem");
                // 利用上下文开启跳转
                startActivity(intent);
            }
        });

        ibAnswer = view.findViewById(R.id.ibAnswer);
        ibAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 开始传值
                Intent intent = new Intent(getActivity(), PhotoActivity.class);
                intent.putExtra("PhotoId", "Answer");
                // 利用上下文开启跳转
                startActivity(intent);
            }
        });

        btnDone = view.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:开始传值
                Intent intent = new Intent(getActivity(), PhotoActivity.class);
                intent.putExtra("PhotoId", "Problem");
                // 利用上下文开启跳转
                startActivity(intent);
            }
        });
    }
}
