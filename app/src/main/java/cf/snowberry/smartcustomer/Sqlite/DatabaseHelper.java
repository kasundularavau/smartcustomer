package cf.snowberry.smartcustomer.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;

    public static final String DATABASE_NAME = "SC_DB";
    public static final String TABLE_NAME = "USER_TABLE";
    public static final String COL_1 = "UID";
    public static final String COL_2 = "EMAIL";
    public static final String COL_3= "NAME";
    public static final String COL_4 = "PHONE";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, UID TEXT,EMAIL TEXT,NAME TEXT,PHONE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String uid, String email,String name,String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_1, uid);
        values.put(COL_2, email);
        values.put(COL_3, name);
        values.put(COL_4, phone);

        long result = db.insert(TABLE_NAME, null, values);
        
        if (result == -1){
            return false;
        }else{
            return true;
        }

    }

    public Cursor getLocalUserData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }
}
