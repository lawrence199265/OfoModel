package com.lawrence.core.lib.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangxu on 17/1/19.
 */

public class DataBaseHelper {


    private static DataBase dataBase;

    private static final DataBaseHelper databaseHelper = new DataBaseHelper();

    public static DataBaseHelper getInstance() {
        return databaseHelper;
    }


    public void createDatabase(Context context, String name, int version) {
        dataBase = new DataBase(context, name, version);
    }

    public void createTable(String name, String... item) {
        StringBuilder sql = new StringBuilder();

        sql.append("create table ")
                .append(name)
                .append(" (");
        for (String s : item) {
            sql.append(s)
                    .append(",");
        }
        sql.replace(sql.length() - 1, sql.length(), ")");
        dataBase.getWrite().execSQL(sql.toString());
    }

    public <T extends Object> T selectAll(String name) {
        String sql = "select * from " + name;
        Cursor cursor = dataBase.getWrite().rawQuery(sql, null);

        return null;
    }

    public int selectCount(String name) {
        String sql = "select count(1)  from " + name;

        Cursor cursor = dataBase.db.rawQuery(sql, null);

        int count = cursor.getCount();


        closeCursor(cursor);
        return count;
    }


    private void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }


    public void insert(String name,  ContentValues data) {
        dataBase.getWrite().insert(name, "", data);
    }

    public void insert(String name,  List<ContentValues> data) {
        dataBase.getWrite().beginTransaction();
        for (ContentValues values : data) {

            StringBuilder sql = new StringBuilder();
            sql.append("insert into or replace ")
                    .append(name)
                    .append(" (");

            Iterator iterator = values.keySet().iterator();

            while (iterator.hasNext()) {
                sql.append(iterator.next())
                        .append(",");
            }

            sql.replace(sql.length() - 1, sql.length(), ")");

            sql.append(" values (");

            iterator = values.valueSet().iterator();

            while (iterator.hasNext()) {
                sql.append(iterator.next())
                        .append(",");
            }

            sql.replace(sql.length() - 1, sql.length(), ")");

            Log.d("Sql Test", "insert: " + sql.toString());

            dataBase.db.execSQL(sql.toString());
        }

        dataBase.getWrite().endTransaction();

    }

    public static void main(String[] args) {
        DataBaseHelper helper = DataBaseHelper.getInstance();
//        helper.createDatabase();
        helper.createTable("user", "user_id", "user_name", "password", "email");

        List<ContentValues> values = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            ContentValues value = new ContentValues();
            value.put("user_id", i);
            value.put("user_name", "测试用户" + i);
            value.put("password", "12345" + i);
            value.put("email", "abc" + i + "@gmail.com");
            values.add(value);
        }

        helper.insert("user", values);

    }
}