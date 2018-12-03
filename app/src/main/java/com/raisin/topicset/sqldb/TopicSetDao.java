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

public class TopicSetDao {

    private Context context;
    private SQLiteDbHelper dbHelper;

    public static final String TABLE_TOPICSET = "topicset";
    public static final String[] TOPICSET_COLUMNS = new String[]{"id", "courseId", "gradeId", "semesterId", "topicSetName", "path", "cdate", "udate"};
    //创建 topicset 表的 sql 语句 "id", "courseId", "gradeId", "semesterId", "problemNm", "answerNm", "path", "cdate", "udate"
    public static final String CREATE_TABLE_TOPICSET_SQL =
            "create table " + TABLE_TOPICSET + "(" +
                    "id integer primary key autoincrement," +
                    "courseId integer not null," +
                    "gradeId varchar(2) not null," +
                    "semesterId varchar(2) not null," +
                    "problemNm varchar(200) not null," +
                    "answerNm varchar(200) not null," +
                    "path varchar(500) not null" + ");";

    public TopicSetDao(Context context) {
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
            cursor = db.query(TABLE_TOPICSET, new String[]{"COUNT(*)"}, selection, selectionArgs, groupBy, having, orderBy);
            count = cursor.getCount();
        } catch (Exception e) {
            Log.e(TABLE_TOPICSET, "", e);
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
    public List<TopicSetTbl> select(String sql, String[] selectionArgs) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<TopicSetTbl> lstTopicSet = new ArrayList<>();

        try {
            db = this.dbHelper.getReadableDatabase();
            cursor = db.rawQuery(sql, selectionArgs);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    lstTopicSet.add(getValue(cursor));
                }
            }
        } catch (Exception e) {
            Log.e(TABLE_TOPICSET, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return lstTopicSet;
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
    public List<TopicSetTbl> select(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        SQLiteDatabase db = null;
        // Cursor:返回值，相当于结果集ResultSet
        Cursor cursor = null;
        List<TopicSetTbl> lstTopicSet = new ArrayList<>();

        try {
            db = this.dbHelper.getReadableDatabase();
            cursor = db.query(TABLE_TOPICSET, columns, selection, selectionArgs, groupBy, having, orderBy);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    lstTopicSet.add(getValue(cursor));
                }
            }
        } catch (Exception e) {
            Log.e(TABLE_TOPICSET, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return lstTopicSet;
    }

    /**
     * 新增一条数据
     */
    public boolean insert(TopicSetTbl topicSet) {
        SQLiteDatabase db = null;

        try {
            db = this.dbHelper.getWritableDatabase();
            db.beginTransaction();
            db.insertOrThrow(TABLE_TOPICSET, null, getContent(topicSet));
            db.setTransactionSuccessful();
            return true;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TABLE_TOPICSET, "", e);
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
    public boolean insert(List<TopicSetTbl> lstTopicSet) {
        SQLiteDatabase db = null;

        try {
            db = this.dbHelper.getWritableDatabase();
            db.beginTransaction();
            for (TopicSetTbl topicSet : lstTopicSet) {
                db.insertOrThrow(TABLE_TOPICSET, null, getContent(topicSet));
            }
            db.setTransactionSuccessful();
            return true;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TABLE_TOPICSET, "", e);
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
            db.delete(TABLE_TOPICSET, where, args);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TABLE_TOPICSET, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }

    /**
     * 修改数据
     */
    public boolean update(ContentValues content, String where, String[] args) {
        SQLiteDatabase db = null;
        try {
            db = this.dbHelper.getWritableDatabase();
            db.beginTransaction();
            db.update(TABLE_TOPICSET, content, where, args);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TABLE_TOPICSET, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }

    private TopicSetTbl getValue(Cursor cursor) {
        TopicSetTbl topicSet = new TopicSetTbl();
        if (cursor.getColumnIndex("id") >= 0) {
            topicSet.setId(cursor.getInt(cursor.getColumnIndex("id")));
        }
        if (cursor.getColumnIndex("courseId") >= 0) {
            topicSet.setCourseId(cursor.getString(cursor.getColumnIndex("courseId")));
        }
        if (cursor.getColumnIndex("gradeId") >= 0) {
            topicSet.setGradeId(cursor.getString(cursor.getColumnIndex("gradeId")));
        }
        if (cursor.getColumnIndex("semesterId") >= 0) {
            topicSet.setSemesterId(cursor.getString(cursor.getColumnIndex("semesterId")));
        }
        if (cursor.getColumnIndex("problemNm") >= 0) {
            topicSet.setProblemNm(cursor.getString(cursor.getColumnIndex("problemNm")));
        }
        if (cursor.getColumnIndex("answerNm") >= 0) {
            topicSet.setAnswerNm(cursor.getString(cursor.getColumnIndex("answerNm")));
        }
        if (cursor.getColumnIndex("path") >= 0) {
            topicSet.setPath(cursor.getString(cursor.getColumnIndex("path")));
        }

        return topicSet;
    }

    private ContentValues getContent(TopicSetTbl topicSet) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("courseId", topicSet.getCourseId());
        contentValues.put("gradeId", topicSet.getGradeId());
        contentValues.put("semesterId", topicSet.getSemesterId());
        contentValues.put("problemNm", topicSet.getProblemNm());
        contentValues.put("answerNm", topicSet.getAnswerNm());
        contentValues.put("path", topicSet.getPath());
        return contentValues;
    }

}
