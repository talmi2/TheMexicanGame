package DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Player_info.db";
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

        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(Player player, int i) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_NAME, player.getName());
            contentValues.put(KEY_SCORE, player.getScore());

            int rowsAffected = db.update(TABLE_NAME, contentValues, KEY_ID + " = ?", new String[] { String.valueOf(i) });
            return (rowsAffected > 0);
    }


    @SuppressLint("Range")
    public Player getPlayerName(int playerId) {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] projection = {KEY_ID, KEY_NAME, KEY_SCORE};
            Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, KEY_ID + " ASC", "1");
            Player player = null;
            if (cursor.moveToFirst()) {
                playerId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String playerName = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                int playerScore = cursor.getInt(cursor.getColumnIndex(KEY_SCORE));
                player = new Player(playerId, playerName, playerScore);
            }
            cursor.close();
            return player;
    }

    @SuppressLint("Range")
    public Player getNextPlayerId(int currentPlayerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_NAME + " WHERE " + KEY_ID + "> ? LIMIT 1", new String[]{String.valueOf(currentPlayerId)});
        Player nextPlayer = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            int score = cursor.getInt(cursor.getColumnIndex(KEY_SCORE));
            nextPlayer = new Player(id, name, score);
        }
        cursor.close();
        db.close();
        return nextPlayer;
    }

    public Cursor getAllData(int i) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " LIMIT " + i,null);
        return res;
    }
}

