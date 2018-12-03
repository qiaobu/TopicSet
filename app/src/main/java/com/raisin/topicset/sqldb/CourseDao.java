package com.raisin.topicset.sqldb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CourseDao {

    private Context context;
    private SQLiteDbHelper dbHelper;

    public static final String TABLE_COURSE = "course";
    public static final String[] COURSE_COLUMNS = new String[]{"id", "type", "courseName", "gradeId", "gradeName", "semesterId", "semesterName"};
    //创建 course 表的 sql 语句
    public static final String CREATE_TABLE_COURSE_SQL =
            "create table " + TABLE_COURSE + "(" +
                    "id integer primary key autoincrement," +
                    "type varchar(1) not null," +
                    "courseName varchar(100) not null," +
                    "gradeId varchar(2) not null," +
                    "gradeName varchar(50) not null," +
                    "semesterId varchar(2) not null," +
                    "semesterName varchar(50) not null" + ");";

    public CourseDao(Context context) {
        this.context = context;
        this.dbHelper = new SQLiteDbHelper(context);
    }

    /**
     * 获取数据件数
     *
     * @param selection:条件字句，相当于where
     * @param selectionArgs:条件字句，参数数组
     * @param groupBy:分组列
     * @param having:分组条件
     * @param orderBy:排序列
     */
    public int count(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        int count = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.dbHelper.getReadableDatabase();
            cursor = db.query(TABLE_COURSE, new String[]{"COUNT(*)"}, selection, selectionArgs, groupBy, having, orderBy);
            count = cursor.getCount();
        } catch (Exception e) {
            Log.e(TABLE_COURSE, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return count;
    }

    /**
     * 查询数据
     */
    public List<CourseTbl> select(String sql, String[] selectionArgs) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<CourseTbl> courseList = new ArrayList<>();

        try {
            db = this.dbHelper.getReadableDatabase();
            cursor = db.rawQuery(sql, selectionArgs);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    courseList.add(getValue(cursor));
                }
            }
        } catch (Exception e) {
            Log.e(TABLE_COURSE, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return courseList;
    }

    /**
     * 查询数据
     *
     * @param columns:列名称数组
     * @param selection:条件字句，相当于where
     * @param selectionArgs:条件字句，参数数组
     * @param groupBy:分组列
     * @param having:分组条件
     * @param orderBy:排序列
     */
    public List<CourseTbl> select(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        SQLiteDatabase db = null;
        // Cursor:返回值，相当于结果集ResultSet
        Cursor cursor = null;
        List<CourseTbl> courseList = new ArrayList<>();

        try {
            db = this.dbHelper.getReadableDatabase();
            cursor = db.query(TABLE_COURSE, columns, selection, selectionArgs, groupBy, having, orderBy);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    courseList.add(getValue(cursor));
                }
            }
        } catch (Exception e) {
            Log.e(TABLE_COURSE, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return courseList;
    }

    /**
     * 新增一条数据
     */
    public boolean insert(CourseTbl course) {
        SQLiteDatabase db = null;

        try {
            db = this.dbHelper.getWritableDatabase();
            db.beginTransaction();
            db.insertOrThrow(TABLE_COURSE, null, getContent(course));
            db.setTransactionSuccessful();
            return true;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TABLE_COURSE, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 新增多条条数据
     */
    public boolean insert(List<CourseTbl> lstCourse) {
        SQLiteDatabase db = null;

        try {
            db = this.dbHelper.getWritableDatabase();
            db.beginTransaction();
            for (CourseTbl course : lstCourse) {
                db.insertOrThrow(TABLE_COURSE, null, getContent(course));
            }
            db.setTransactionSuccessful();
            return true;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TABLE_COURSE, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 删除数据
     */
    public boolean delete(String where, String[] args) {
        SQLiteDatabase db = null;

        try {
            db = this.dbHelper.getWritableDatabase();
            db.beginTransaction();
            db.delete(TABLE_COURSE, where, args);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TABLE_COURSE, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 修改一条数据
     */
    public boolean update(ContentValues content, String where, String[] args) {
        SQLiteDatabase db = null;
        try {
            db = this.dbHelper.getWritableDatabase();
            db.beginTransaction();
            db.update(TABLE_COURSE, content, where, args);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TABLE_COURSE, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }

    private CourseTbl getValue(Cursor cursor) {
        CourseTbl course = new CourseTbl();
        if (cursor.getColumnIndex("id") >= 0) {
            course.setId(cursor.getInt(cursor.getColumnIndex("id")));
        }
        if (cursor.getColumnIndex("type") >= 0) {
            course.setType(cursor.getString(cursor.getColumnIndex("type")));
        }
        if (cursor.getColumnIndex("courseName") >= 0) {
            course.setCourseName(cursor.getString(cursor.getColumnIndex("courseName")));
        }
        if (cursor.getColumnIndex("gradeId") >= 0) {
            course.setGradeId(cursor.getString(cursor.getColumnIndex("gradeId")));
        }
        if (cursor.getColumnIndex("gradeName") >= 0) {
            course.setGradeName(cursor.getString(cursor.getColumnIndex("gradeName")));
        }
        if (cursor.getColumnIndex("semesterId") >= 0) {
            course.setSemesterId(cursor.getString(cursor.getColumnIndex("semesterId")));
        }
        if (cursor.getColumnIndex("semesterName") >= 0) {
            course.setSemesterName(cursor.getString(cursor.getColumnIndex("semesterName")));
        }

        return course;
    }

    private ContentValues getContent(CourseTbl course) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", course.getType());
        contentValues.put("courseName", course.getCourseName());
        contentValues.put("gradeId", course.getGradeId());
        contentValues.put("gradeName", course.getGradeName());
        contentValues.put("semesterId", course.getSemesterId());
        contentValues.put("semesterName", course.getSemesterName());
        return contentValues;
    }
}
