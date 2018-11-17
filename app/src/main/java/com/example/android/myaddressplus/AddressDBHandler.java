package com.example.android.myaddressplus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AddressDBHandler extends SQLiteOpenHelper {
    private static final String TAG = AddressDBHandler.class.getSimpleName();
    private static final String DBName = "address.db";
    private static final int DBVersion = 1;

    public AddressDBHandler(Context context){

        super(context,DBName,null,DBVersion);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        AddressTableHandler.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        AddressTableHandler.onUpgrade(db,oldVersion,newVersion);
    }
}