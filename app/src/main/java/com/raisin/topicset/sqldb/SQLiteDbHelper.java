package com.raisin.topicset.sqldb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ts_database.db";
    private static final int DB_VERSION = 1;

    public SQLiteDbHelper(Context context) {
        // 传递数据库名与版本号给父类
        super(context, DB_NAME, null, DB_VERSION);
    }

    public SQLiteDbHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 在这里通过 db.execSQL 函数执行 SQL 语句创建所需要的表
        // 创建表
        db.execSQL(CourseDao.CREATE_TABLE_COURSE_SQL);
        db.execSQL(TopicSetDao.CREATE_TABLE_TOPICSET_SQL);

        // 原始数据
        db.execSQL("INSERT INTO " + CourseDao.TABLE_COURSE + " (type, courseName, gradeId, gradeName, semesterId, semesterName) values ('1', '语文', '', '', '', '')");
        db.execSQL("INSERT INTO " + CourseDao.TABLE_COURSE + " (type, courseName, gradeId, gradeName, semesterId, semesterName) values ('1', '数学', '', '', '', '')");
        db.execSQL("INSERT INTO " + CourseDao.TABLE_COURSE + " (type, courseName, gradeId, gradeName, semesterId, semesterName) values ('1', '英语', '', '', '', '')");
        db.execSQL("INSERT INTO " + CourseDao.TABLE_COURSE + " (type, courseName, gradeId, gradeName, semesterId, semesterName) values ('1', '物理', '', '', '', '')");
        db.execSQL("INSERT INTO " + CourseDao.TABLE_COURSE + " (type, courseName, gradeId, gradeName, semesterId, semesterName) values ('1', '化学', '', '', '', '')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
