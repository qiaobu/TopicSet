package com.raisin.topicset.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.raisin.topicset.R;
import com.raisin.topicset.component.CropImageView;
import com.raisin.topicset.sqldb.CourseDao;
import com.raisin.topicset.sqldb.CourseTbl;
import com.raisin.topicset.utils.PhotoUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.layout.simple_spinner_dropdown_item;
import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment {

    private static final int FROM_CAMERA = 11;
    private static final int FROM_PIC = 12;
    private static final int PHOTO_CUT = 13;
    private static final String[] GRADE_NAME = new String[]{"小一", "小二", "小三", "小四", "小五", "初六", "初七", "初八", "初九", "高一", "高二", "高三"};
    private static final String[] SEMESTER_NAME = new String[]{"上", "下"};
    private static final String TIME = "yyyyMMddhhmmssSSS";
    private static final String JPG = ".jpg";
    private static final String TAG = "AddFragment";

    private View view;
    // 课程下拉框
    private Spinner spCourse;
    // 年纪下拉框
    private Spinner spGrade;
    // 学期下拉框
    private Spinner spSemester;
    // ImageView
    private CropImageView ivProblem;
    // ImageView
    private CropImageView ivAnswer;
    // Button组件
    private Button btnDone;

    //裁剪框
    private CropImageView mCropImageView;
    //裁剪后的图片Problem
    private Bitmap bpProblem;
    //裁剪后的图片Answer
    private Bitmap bpAnswer;

    // 课程下拉框相关
    private ArrayAdapter adpCourse;
    private List<String> lstCourseId = new ArrayList<>();
    private List<String> lstCourseNm = new ArrayList<>();

    // 年纪下拉框相关
    private ArrayAdapter adpGrade;
    private List<String> lstGradeId = new ArrayList<>();
    private List<String> lstGradeNm = new ArrayList<>();

    // 学期下拉框相关
    private ArrayAdapter adpSemester;
    private List<String> lstSemesterId = new ArrayList<>();
    private List<String> lstSemesterNm = new ArrayList<>();

    // 图片路径
    private String mCurrentPhotoPath = "";
    // 课程Id
    private String courseId = "";
    // 年纪Id
    private String gradeId = "";
    // 学期Id
    private String semesterId = "";
    // 种类：problem; answer
    private String kind = "";

    public AddFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        initSpinner();
        initImageView();
    }

    private void initSpinner() {
        // 课程的下拉框
        getDataSource(getContext());
        spCourse = view.findViewById(R.id.spCourse);
        adpCourse = new ArrayAdapter<>(getContext(), simple_spinner_dropdown_item, lstCourseNm);
        spCourse.setAdapter(adpCourse);
        // 注册监听器
        spCourse.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (lstCourseId != null && lstCourseId.size() >= position) {
                    courseId = lstCourseId.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (lstCourseId != null && lstCourseId.size() >= 0) {
                    courseId = lstCourseId.get(0);
                }
            }
        });

        // 年级的下拉框
        getGradeDataSource();
        spGrade = view.findViewById(R.id.spGrade);
        adpGrade = new ArrayAdapter<>(getContext(), simple_spinner_dropdown_item, lstGradeNm);
        spGrade.setAdapter(adpGrade);
        // 注册监听器
        spGrade.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (lstGradeId != null && lstGradeId.size() >= position) {
                    gradeId = lstGradeId.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (lstGradeId != null && lstGradeId.size() >= 0) {
                    gradeId = lstGradeId.get(0);
                }
            }
        });

        // 学期的下拉框
        getSemesterDataSource();
        spSemester = view.findViewById(R.id.spSemester);
        adpSemester = new ArrayAdapter<>(getContext(), simple_spinner_dropdown_item, lstSemesterNm);
        spSemester.setAdapter(adpSemester);
        // 注册监听器
        spSemester.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (lstSemesterId != null && lstSemesterId.size() >= position) {
                    semesterId = lstSemesterId.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (lstSemesterId != null && lstSemesterId.size() >= 0) {
                    semesterId = lstSemesterId.get(0);
                }
            }
        });
    }

    private void initImageView() {
        // 问题
        ivProblem = view.findViewById(R.id.ivProblem);
        ivProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCropImageView = ivProblem;
                kind = "problem";
                initDialog();
            }
        });

        // 答案
        ivAnswer = view.findViewById(R.id.ivAnswer);
        ivAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCropImageView = ivAnswer;
                kind = "answer";
                initDialog();
            }
        });

        // 保存按钮
        btnDone = view.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 保存图片
                if ("problem".equals(kind)) {
                    PhotoUtils.saveImg(getActivity(), bpProblem, mCurrentPhotoPath);
                } else if ("answer".equals(kind)) {
                    PhotoUtils.saveImg(getActivity(), bpAnswer, mCurrentPhotoPath);
                }

                // TODO: DB处理
                Toast.makeText(getActivity(), "图片保存成功", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
        dialog.setTitle("选择图片：");
        dialog.setItems(new String[]{"相机", "相册"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int idx) {
                switch (idx) {
                    case 0:
                        // 跳转到相机
                        pickPicFromCam();
                        break;
                    case 1:
                        // 跳转到相册
                        pickPicFromPic();
                        break;
                    default:
                        break;
                }
            }
        });
        dialog.show();
    }

    private void pickPicFromCam() {
        try {
            // 图片文件
            File mPhotoFile = createImageFile();
            Uri imageUri;

            // 激活相机
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // 判断存储卡是否可以用，可用进行存储
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (Build.VERSION.SDK_INT < 24) { // 系统版本:6.0.1
                    imageUri = Uri.fromFile(mPhotoFile);
                } else {
                    //兼容android7.0 使用共享文件的形式
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, mPhotoFile.getAbsolutePath());
                    imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }

            // 开启一个带有返回值的Activity，请求码为FROM_CAMERA
            startActivityForResult(intent, FROM_CAMERA);
        } catch (IOException e) {
            Log.d(TAG, "no SDCARD!!!!");
            Log.d(TAG, e.getStackTrace().toString());
            e.printStackTrace();
        }
    }

    private void pickPicFromPic() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 开启一个带有返回值的Activity，请求码为FROM_PIC
        startActivityForResult(intent, FROM_PIC);
    }

    // 创建路径
    private File createImageFile() throws IOException {
        //检测内存卡是否存在
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "内存卡不存在！", Toast.LENGTH_LONG).show();
            Log.d(TAG, "no SDCARD!!!!");
            return null;
        }

        // 创建图片保存的位置
        File path = new File(Environment.getExternalStorageDirectory(), "TopicSet/" + (String.valueOf(courseId + gradeId + semesterId)));
        // 如果路径不存在
        if (!path.exists()) {
            //创建路径，需要权限
            path.mkdirs();
        }

        // 图片名称
        String picName = courseId + "_" + gradeId + "_" + semesterId + "_" + kind + "_" + new SimpleDateFormat(TIME).format(new Date()) + JPG;
        // 组合全部的路径
        mCurrentPhotoPath = path.toString() + File.separator + picName;
        Log.d(TAG, mCurrentPhotoPath);

        return new File(mCurrentPhotoPath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 从相机选择返回的结果
        if (requestCode == FROM_CAMERA && resultCode == RESULT_OK) {
            //设置intent的data和Type属性。android 7.0以上改用provider
            Uri uri = PhotoUtils.obtainUri(getContext(), mCurrentPhotoPath);

            // 对图片进行裁剪
            PhotoUtils.cropImageUri(getActivity(), uri, uri, 1, 1, 300, 300, PHOTO_CUT);
        }

        // 从图库选择返回的结果
        else if (requestCode == FROM_PIC && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(getActivity(),"没有获取到返回的图片",Toast.LENGTH_LONG).show();

            } else {
                Uri uri = data.getData();
                Log.d(TAG, uri.toString());
                // 返回图片路径
                Cursor cursor = this.getContext().getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (cursor.moveToFirst()) {
                    String imgPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.d(TAG, imgPath);
                    // 裁剪框显示图片
                    mCropImageView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
                }
            }
        }

        // 裁剪后返回的图片
        else if (requestCode == PHOTO_CUT) {
            //设置intent的data和Type属性。android 7.0以上改用provider
            Uri uri = PhotoUtils.obtainUri(getContext(), mCurrentPhotoPath);
            if (uri == null) {
                uri = data.getData();
                // 返回图片路径
                Cursor cursor = this.getContext().getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (cursor.moveToFirst()) {
                    String imgPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.d(TAG, imgPath);
                    // 裁剪框显示图片
                    mCropImageView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
                }
            } else {
                mCropImageView.setImageBitmap(PhotoUtils.decodeUriAsBitmap(getContext(), uri));
            }
        }
    }

    private void getDataSource(Context context) {
        CourseDao dao = new CourseDao(context);
        List<CourseTbl> lstCourseTbl = dao.select("select id, courseName from course where type = '1'", new String[]{});
        for (CourseTbl courseTbl : lstCourseTbl) {
            lstCourseId.add(padLeftByZero(String.valueOf(courseTbl.getId())));
            lstCourseNm.add(courseTbl.getCourseName());
        }
    }

    private void getGradeDataSource() {
        for (int idx = 1; idx < 13; idx++) {
            lstGradeId.add(padLeftByZero(String.valueOf(idx)));
            lstGradeNm.add(GRADE_NAME[idx - 1]);
        }
    }

    private void getSemesterDataSource() {
        for (int idx = 1; idx < 3; idx++) {
            lstSemesterId.add(padLeftByZero(String.valueOf(idx)));
            lstSemesterNm.add(SEMESTER_NAME[idx - 1]);
        }
    }

    private String padLeftByZero(String value) {
        if (value.length() < 2) {
            return "0" + value;
        }

        return value;
    }
}
