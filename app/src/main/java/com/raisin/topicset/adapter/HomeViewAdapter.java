package com.raisin.topicset.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.raisin.topicset.R;
import com.raisin.topicset.activity.TopicSetDetail;
import com.raisin.topicset.sqldb.CourseDao;
import com.raisin.topicset.sqldb.CourseTbl;

public class HomeViewAdapter extends BaseAdapter {
    // 上下文对象
    private Context context;
    // ListView显示的数据
    private List<String> lstCourse;

    public HomeViewAdapter(Context context, List<String> lstCourse) {
        this.context = context;
        this.lstCourse = lstCourse;
    }

    @Override
    public int getCount() {
        return lstCourse == null ? 0 : lstCourse.size();
    }

    @Override
    public Object getItem(int position) {
        return lstCourse.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // 判断是否有缓存
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            // 得到缓存的布局
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvCourse.setText(lstCourse.get(position));

        setbackColor(position, convertView);

        //点击事件并传值
        viewHolder.tvCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == getCount() - 1) {
                    final EditText courseNm = new EditText(context.getApplicationContext());
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("增加课程：");
                    builder.setView(courseNm);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String courseName = courseNm.getText().toString().trim();
                            CourseDao courseDao = new CourseDao(context);
                            CourseTbl course = new CourseTbl("1", courseName, "", "", "", "");
                            courseDao.insert(course);
                            lstCourse.remove(lstCourse.size() - 1);
                            lstCourse.add(courseName);
                            lstCourse.add("+");
                            notifyDataSetInvalidated();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                        }
                    });
                    builder.show();
                } else {
                    // TODO:开始传值
                    Intent intent = new Intent(context, TopicSetDetail.class);
                    intent.putExtra("CourseName", lstCourse.get(position));
                    //利用上下文开启跳转
                    context.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    private void setbackColor(int position, View convertView) {
        if (position == lstCourse.size() - 1) {
            convertView.setBackgroundResource(R.color.addSubject);
        } else {
            switch ((position + 1) % 5) {
                case 0:
                    convertView.setBackgroundResource(R.color.subject1);
                    break;
                case 1:
                    convertView.setBackgroundResource(R.color.subject2);
                    break;
                case 2:
                    convertView.setBackgroundResource(R.color.subject3);
                    break;
                case 3:
                    convertView.setBackgroundResource(R.color.subject4);
                    break;
                case 4:
                    convertView.setBackgroundResource(R.color.subject5);
                    break;
            }
        }
    }


    private final class ViewHolder {
        TextView tvCourse;

        ViewHolder(View view) {
            tvCourse = view.findViewById(R.id.tvCourse);
        }
    }

}
