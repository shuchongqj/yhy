package com.quanyan.yhy.ui.common.city.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
    public static String result;
    private static SQLiteDatabase db;

    public static String query(Context context,String cache_key){
        String path=context.getFilesDir()+ "/quanyan.db";
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("select CONTENT from DB_CACHE where CACHE_KEY=?",
                new String[]{cache_key});
        if(cursor!=null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                result=cursor.getString(0);
            }
            cursor.close();
        }

        return result;
    }
}
