package com.example.trucksharingtask;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {
    public DB(@Nullable Context context) {
        super(context, "truck_app_db1", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USERS_TABLE = "CREATE TABLE USERS (UID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, password TEXT, image BLOB, name TEXT, phone_number TEXT);";
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
        String CREATE_DELIVERIES_TABLE = "CREATE TABLE DELIVERIES (DID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " receiver TEXT, sender TEXT, goods_type TEXT, veh_type TEXT," +
                "weight INTEGER, length INTEGER, width INTEGER, height INTEGER" +
                ", time TEXT, location TEXT, dropOff TEXT, date TEXT);";
        sqLiteDatabase.execSQL(CREATE_DELIVERIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS DELIVERIES;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS USERS;");
        onCreate(sqLiteDatabase);
    }

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user.getUsername());
        contentValues.put("password", user.getPassword());
        byte[] accountImageByte = convertImage(user.getImage());
        contentValues.put("image", accountImageByte);
        contentValues.put("name", user.getName());
        long row = db.insert("USERS", null, contentValues);
        db.close();
        return row;
    }

    public static byte[] convertImage(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public int getUser(String uname, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        String rawQuery = "SELECT * FROM USERS WHERE username LIKE '%" + uname + "%' AND password LIKE '%" + pass + "%'";
        Cursor cursor = db.rawQuery(rawQuery, null);
        if(cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("UID"));
                cursor.close();
                return id;
            }
        }
        cursor.close();
        return 0;
    }

    public class MyResult {
        public int id;
        public String name;
        public String password;
        public String phone;
        public String username;
        public byte[] image;
    }

    public MyResult GUS(int UID) {
        MyResult result = new MyResult();
        SQLiteDatabase db = this.getReadableDatabase();
        String rawQuery = "SELECT * FROM USERS WHERE UID=" + UID;
        Cursor cursor = db.rawQuery(rawQuery, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = result.id = cursor.getInt(cursor.getColumnIndex("UID"));
                @SuppressLint("Range") String name = result.name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String username = result.username = cursor.getString(cursor.getColumnIndex("username"));
                @SuppressLint("Range") String passs = result.password = cursor.getString(cursor.getColumnIndex("password"));
                @SuppressLint("Range") String phone = result.phone =  cursor.getString(cursor.getColumnIndex("phone_number"));
                @SuppressLint("Range") byte[] byteArray = result.image = cursor.getBlob(cursor.getColumnIndex("image"));
            }
            cursor.close();
            return result;
        }
        return result;
    }

    public List<orderModel> getOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM DELIVERIES";
        Cursor cursor = db.rawQuery(sql, null);

        List<orderModel> orderList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") orderModel order = new orderModel(
                        cursor.getString(cursor.getColumnIndex("sender")),
                        cursor.getString(cursor.getColumnIndex("receiver")),
                        cursor.getString(cursor.getColumnIndex("goods_type")),
                        cursor.getString(cursor.getColumnIndex("veh_type")),
                        cursor.getString(cursor.getColumnIndex("time")),
                        cursor.getString(cursor.getColumnIndex("location")),
                        cursor.getString(cursor.getColumnIndex("dropOff")),
                        Long.parseLong(cursor.getString(cursor.getColumnIndex("date"))),
                        cursor.getInt(cursor.getColumnIndex("weight")),
                        cursor.getInt(cursor.getColumnIndex("length")),
                        cursor.getInt(cursor.getColumnIndex("width")),
                        cursor.getInt(cursor.getColumnIndex("height")));
                orderList.add(order);
                cursor.moveToNext();
            } while(!cursor.isAfterLast());
        }
        cursor.close();
        return orderList;
    }

    public long addOrder(orderModel order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sender", order.getSender());
        contentValues.put("receiver", order.getReceiver());
        contentValues.put("date", order.getDate().toString());
        contentValues.put("time", order.getTime());
        contentValues.put("location", order.getLocation());
        contentValues.put("dropOff",order.getDropOff());
        contentValues.put("goods_type", order.getGoodType());
        contentValues.put("veh_type", order.getVehicleType());
        contentValues.put("weight", order.getWeight());
        contentValues.put("length", order.getLength());
        contentValues.put("width", order.getWidth());
        contentValues.put("height", order.getHeight());
        long row = db.insert("DELIVERIES", null, contentValues);
        db.close();
        return row;
    }


}
