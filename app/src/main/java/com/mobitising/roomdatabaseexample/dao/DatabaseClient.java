package com.mobitising.roomdatabaseexample.dao;

import static com.mobitising.roomdatabaseexample.dao.AppDatabase.DataBaseName;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private Context mCtx;
    private static DatabaseClient mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        appDatabase = Room.databaseBuilder(mCtx,AppDatabase.class,DataBaseName).build();
    }
    public static synchronized DatabaseClient getInstance(Context mCtx){
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }
    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
