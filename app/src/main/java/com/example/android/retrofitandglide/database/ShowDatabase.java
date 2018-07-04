package com.example.android.retrofitandglide.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {PopMovie.class},version = 1,exportSchema = false)
public abstract class ShowDatabase extends RoomDatabase {
    private static final String LOG_TAG = ShowDatabase.class.getName();
    private static ShowDatabase mShowDatabase;
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "my_shows";

    public static ShowDatabase getInstance(Context context){
        if (mShowDatabase==null){
            synchronized (LOCK){
                mShowDatabase = Room.databaseBuilder(context,ShowDatabase.class,DATABASE_NAME).build();
            }
        }
        Log.d(LOG_TAG,"create an instance of show database");
        return mShowDatabase;
    }

    public abstract ShowDao getShowDao();
}
