package com.example.android.myaddressplus;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AddressTableHandler {
    private static final String TAG = AddressTableHandler.class.getSimpleName();
    public static final String AddressTable =  "address";
    public static final String Address_ID = "_id";
    public static final String Degination = "degination";
    public static final String FirstName = "first_name";
    public static final String LastName = "last_name";
    public static final String Address = "address";
    public static final String Province = "province";
    public static final String Country = "country";
    public static final String Postcode = "postcode";

    private static final String Create_table = "create table "
            +AddressTable
            +"("
            +Address_ID + " INTEGER PRIMARY KEY autoincrement ,"
            +Degination + " text not null,"
            +FirstName + " text not null,"
            +LastName + " text not null,"
            +Address + " text not null,"
            +Province + " text not null,"
            +Country + " text not null,"
            +Postcode + " text not null"
            +")";

    public static void onCreate (SQLiteDatabase db){
        Log.i(TAG, "SangaeLee(147948186) onCreate");
        db.execSQL(Create_table);
    }

    public static void onUpgrade(SQLiteDatabase db,int oldV, int newV){
        Log.i(TAG, "SangaeLee(147948186) onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS "+AddressTable);
        onCreate(db);

    }

}
