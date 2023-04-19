package DataBase;

import static android.os.FileObserver.MODIFY;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Player_info.db";
    public static final int DATABASE_VERSION = 4;
    public static final String TABLE_NAME = "Players_table"; // Name of the Table...
    public static final String KEY_ID = "ID";// Column1 of the table
    public static final String KEY_NAME = "NAME";// Column2 of the table
    public static final String KEY_SCORE = "SCORE"; // Column 3 of the table
    public static final String KEY_STATUS = "STATUS"; // Column 4 of the table


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, SCORE INTEGER, STATUS BOOLEAN DEFAULT 0)");
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
//    public boolean insertData(Player player, int i) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(KEY_NAME, player.getName());
//        contentValues.put(KEY_SCORE, player.getScore());
//        contentValues.put(KEY_STATUS, player.hasRegainedPoint() ? 1 : 0);
//        long result = db.insert(TABLE_NAME, null, contentValues);
//        if (result == -1)
//            return false;
//        else
//            return true;
//    }

    public boolean insertData(Player player, int i) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, player.getName());
        contentValues.put(KEY_SCORE, player.getScore());
        contentValues.put(KEY_STATUS, player.hasRegainedPoint());

        int rowsAffected = db.update(TABLE_NAME, contentValues, KEY_ID + " = ?", new String[]{String.valueOf(i)});
        return (rowsAffected > 0);
    }


    @SuppressLint("Range")
    public Player getPlayerName(int playerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {KEY_ID, KEY_NAME, KEY_SCORE, KEY_STATUS};
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, KEY_ID + " ASC", "1");
        Player player = null;
        if (cursor.moveToFirst()) {
            playerId = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            String playerName = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            int playerScore = cursor.getInt(cursor.getColumnIndex(KEY_SCORE));
            boolean status = cursor.getInt(cursor.getColumnIndex(KEY_STATUS)) == 0;

            player = new Player(playerId, playerName, playerScore, status);
        }
        cursor.close();
        return player;
    }
//
//    @SuppressLint("Range")
//    public Player getNextPlayerId(int currentPlayerId) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + "> ? LIMIT 1", new String[]{String.valueOf(currentPlayerId)});
//        Player nextPlayer = null;
//        if (cursor.moveToFirst()) {
//            int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
//            String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
//            int score = cursor.getInt(cursor.getColumnIndex(KEY_SCORE));
//            boolean status = cursor.getInt(cursor.getColumnIndex(KEY_STATUS)) == 0;
//
//            nextPlayer = new Player(id, name, score, status);
//        }
//        cursor.close();
//        db.close();
//        return nextPlayer;
//    }

    @SuppressLint("Range")
    public int getPlayerScore(int playerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_SCORE + " FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = ?", new String[]{String.valueOf(playerId)});
        int score = 0;
        if (cursor.moveToFirst()) {
            score = cursor.getInt(cursor.getColumnIndex(KEY_SCORE));
        }
        cursor.close();
        db.close();
        return score;
    }

    @SuppressLint("Range")
    public void updatePlayerScore(int playerId, int newScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + KEY_SCORE + " = ? WHERE " + KEY_ID + " = ?";
        db.execSQL(query, new String[]{String.valueOf(newScore), String.valueOf(playerId)});
        db.close();

    }

    @SuppressLint("Range")
    public Player getPreviousPlayerId(int currentPlayerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + "< ? ORDER BY " + KEY_ID + " DESC LIMIT 1", new String[]{String.valueOf(currentPlayerId)});
        Player previousPlayer = null;
        if (cursor.moveToFirst()) {
             int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            int score = cursor.getInt(cursor.getColumnIndex(KEY_SCORE));
            boolean status = cursor.getInt(cursor.getColumnIndex(KEY_STATUS)) == 0;

            previousPlayer = new Player(id, name, score, status);
        }
        cursor.close();
        db.close();
        return previousPlayer;
    }

    @SuppressLint("Range")
    public String getCurrentPlayerName(int currentPlayerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = ?", new String[]{String.valueOf(currentPlayerId)});
        String playerName = null;
        if (cursor.moveToFirst()) {
            playerName = cursor.getString(cursor.getColumnIndex(KEY_NAME));
        }
        cursor.close();
        db.close();
        return playerName;
    }


public void updatePlayerStatus(int playerId, boolean newStatus) {
    SQLiteDatabase db = this.getWritableDatabase();
    String query = "UPDATE " + TABLE_NAME + " SET " + KEY_STATUS + " = ? WHERE " + KEY_ID + " = ?";
    db.execSQL(query, new Object[] { newStatus, String.valueOf(playerId) });
    db.close();
}
    @SuppressLint("Range")
    public List<Player> getActivePlayers(boolean includeInactive) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Player> players = new ArrayList<>();
        String query;
        if (includeInactive) {
            query = "SELECT * FROM " + TABLE_NAME;
        } else {
            query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_STATUS + " = 1 ";
        }
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                int score = cursor.getInt(cursor.getColumnIndex(KEY_SCORE));
                boolean status = cursor.getInt(cursor.getColumnIndex(KEY_STATUS)) == 1;

                players.add(new Player(id, name, score, status));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return players;
    }



    public Player getNextActivePlayer(int playerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " > " + playerId + " AND " + KEY_STATUS+ " = 1 ORDER BY " +KEY_ID + " LIMIT 1", null);
        Player player = null;
        if (cursor.moveToFirst()) {
            boolean active = cursor.getInt(3) == 1;
            player = new Player(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),active);
        }
        cursor.close();
        db.close();
        return player;
    }

    public Cursor getAllData(int i) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " LIMIT " + i,null);
        return res;
    }
}

