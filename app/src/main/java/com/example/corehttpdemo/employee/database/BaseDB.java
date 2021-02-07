package com.example.corehttpdemo.employee.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.corehttpdemo.employee.tables.TBL_Employee;

public class BaseDB extends SQLiteOpenHelper {
    public static String DATABASE_NAME="employee.db";
    public static int DATABASE_VERSION=1;

    private String CREATE_EMP_TABLE_QR="CREATE TABLE "+ TBL_Employee.TABLE_NAME
            +" ("+TBL_Employee.EMP_ID +" INTEGER PRIMARY KEY , "
            +TBL_Employee.EMP_DOB +" TEXT , "
            +TBL_Employee.EMP_NAME+" TEXT)";

    private String DROP_TABLE="DROP TABLE IF EXISTS "+TBL_Employee.TABLE_NAME;

    public BaseDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    public BaseDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EMP_TABLE_QR);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("onUpgrade=","onUpgrade");
        Log.d("oldVersion=",""+oldVersion);
        Log.d("newVersion=",""+newVersion);

        db.execSQL(DROP_TABLE);
        db.execSQL(CREATE_EMP_TABLE_QR);
    }


}
