package DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "PlayersInfo.db";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "Players_table"; // Name of the Table...
    public static final String KEY_ID = "ID";// Column1 of the table
    public static final String KEY_NAME = "NAME";// Column2 of the table
    public static final String KEY_SCORE = "SCORE"; // Column 3 of the table


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, SCORE INTEGER)" );
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

    public boolean insertData(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, player.getName());
        contentValues.put(KEY_SCORE, player.getScore());
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


    @SuppressLint("Range")
    public Player getPlayerName(int playerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {KEY_NAME, KEY_SCORE};
        String selection = KEY_ID + "=?";
        String[] selectionArgs = {String.valueOf(playerId)};
        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        Player player = null;
        if (cursor.moveToFirst()) {
            String playerName = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            int playerScore = cursor.getInt(cursor.getColumnIndex(KEY_SCORE));
            player = new Player(playerId, playerName, playerScore);
        }
        cursor.close();
        return player;
    }



    public Integer deleteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null,null);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME,null);
        return res;
    }
}

