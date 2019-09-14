package com.rohindh.babyneeds.data;


import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.rohindh.babyneeds.model.Items;
import com.rohindh.babyneeds.utils.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {
    Context context;
    public DataBaseHandler(Context context) {
        super(context, Constants.DB_NAME, null,Constants.DB_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE "+Constants.TABLE_NAME
                +"("+Constants.KEY_ID+" INTEGER PRIMARY KEY,"
                +Constants.KEY_BABY_ITEM+" TEXT,"
                +Constants.KEY_COLOR+" TEXT,"
                +Constants.KEY_ITEM_SIZE+" INTEGER,"
                +Constants.KEY_QTY_NUMBER+" INTEGER,"
                +Constants.KEY_DATA_NAME+" LONG);";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME);
        onCreate(db);
    }

    public void additem(Items item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_BABY_ITEM,item.getItemName());
//        values.put(Constants.KEY_ID,item.getId());
        values.put(Constants.KEY_ITEM_SIZE,item.getItemSize());
        Log.d("hai","i work fine");
        values.put(Constants.KEY_QTY_NUMBER,item.getItemQuantity());
        values.put(Constants.KEY_COLOR,item.getItemColor());
        values.put(Constants.KEY_DATA_NAME,java.lang.System.currentTimeMillis());

        //insert queries

        db.insert(Constants.TABLE_NAME,null,values);
    }
    public Items getItem(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Constants.TABLE_NAME,new String[]{Constants.KEY_ID,Constants.KEY_BABY_ITEM,Constants.KEY_QTY_NUMBER,Constants.KEY_ITEM_SIZE,Constants.KEY_COLOR},
                Constants.KEY_ID +"=?",new String[]{String.valueOf(id)},null,null,null,null);
        if (cursor!=null) {
            cursor.moveToFirst();
            Items item = new Items();
            item.setItemColor(cursor.getString(cursor.getColumnIndex(Constants.KEY_COLOR)));
            item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_BABY_ITEM)));
            item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

//            to save time first we need to convert the timestamp to something readable
//            item.setDataItemAdded(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATA_NAME)));//so we can't straightly do this without converting
            DateFormat dateFormat = DateFormat.getDateInstance();
            String formattedtime = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATA_NAME))));
            item.setDataItemAdded(formattedtime);
        }
        return null;
    }

    public List<Items> getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Items> itemsList = new ArrayList<>();
        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID, Constants.KEY_BABY_ITEM, Constants.KEY_QTY_NUMBER, Constants.KEY_ITEM_SIZE, Constants.KEY_COLOR
                , Constants.KEY_DATA_NAME}, null, null, null, null, Constants.KEY_DATA_NAME + " DESC");
        if (cursor.moveToFirst()) {
            do {
                Items item = new Items();
                item.setItemColor(cursor.getString(cursor.getColumnIndex(Constants.KEY_COLOR)));
                item.setItemName(cursor.getString(cursor.getColumnIndex(Constants.KEY_BABY_ITEM)));
                item.setItemQuantity(cursor.getInt(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)));

//            to save time first we need to convert the timestamp to something readable
//            item.setDataItemAdded(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATA_NAME)));//so we can't straightly do this without converting
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedtime = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATA_NAME))));
                item.setDataItemAdded(formattedtime);
                itemsList.add(item);
            } while (cursor.moveToNext());
        }
        return itemsList;
    }
    public void deleteitem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.KEY_ID + "=?",new String[]{String.valueOf(id)});
        db.close();
    }
    public int updateItem(Items item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_BABY_ITEM,item.getItemName());
        values.put(Constants.KEY_QTY_NUMBER,item.getItemQuantity());
        values.put(Constants.KEY_COLOR,item.getItemColor());
        values.put(Constants.KEY_DATA_NAME,java.lang.System.currentTimeMillis());
        values.put(Constants.KEY_ID,item.getId());
        values.put(Constants.KEY_ITEM_SIZE,item.getItemSize());
        return db.update(Constants.TABLE_NAME,values,Constants.KEY_ID+"=?",new String[]{String.valueOf(item.getId())});

    }
    public int getItemcount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String querie ="SELECT * FROM "+Constants.TABLE_NAME;
        Cursor cursor = db.rawQuery(querie,null);

        return  cursor.getCount();
    }
}



