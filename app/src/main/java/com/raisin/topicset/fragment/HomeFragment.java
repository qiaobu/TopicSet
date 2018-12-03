package com.raisin.topicset.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.raisin.topicset.R;
import com.raisin.topicset.adapter.HomeViewAdapter;
import com.raisin.topicset.sqldb.CourseDao;
import com.raisin.topicset.sqldb.CourseTbl;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
    // ListView组件
    private ListView lvCourse;
    // 存储数据
    private List<String> lstCourse = new ArrayList<>();
    // ListView的数据适配器
    private HomeViewAdapter homeViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // 初始化组件
        initView(view);
        // 初始化数据
        initData();

        initAdapter();

        return view;
    }

    private void initView(View view) {
        lvCourse = (ListView) view.findViewById(R.id.lvCourse);
    }

    private void initData() {
        // DB
        CourseDao courseDao = new CourseDao(getContext());
        List<CourseTbl> lstCourseTbl = courseDao.select(new String[]{"courseName"}, "type = ?", new String[]{"1"}, null, null, "id");
        for (CourseTbl courseTbl : lstCourseTbl) {
            lstCourse.add(courseTbl.getCourseName());
        }
        lstCourse.add("+");
    }

    private void initAdapter() {
        homeViewAdapter = new HomeViewAdapter(this.getContext(), lstCourse);
        lvCourse.setAdapter(homeViewAdapter);
    }
}
