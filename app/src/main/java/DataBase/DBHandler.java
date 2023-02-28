package DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PlayersInfo.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Players_table"; // Name of the Table...
    public static final String KEY_ID = "ID";// Column1 of the table
    public static final String KEY_NAME = "NAME";// Column2 of the table


    public DBHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String sql = "CREATE TABLE Players (name TEXT PRIMARY KEY)";
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertUser(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert("Players", null, values);
    }

    public boolean insertData(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        long result = db.insert(TABLE_NAME,null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    @SuppressLint("Range")
    public String getPlayerName(int playerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_NAME}, null, null, null, null, null);
        String playerName = null;
        if (cursor.moveToFirst()) {
            playerName = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        }
        cursor.close();
        return playerName;
    }


    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}
