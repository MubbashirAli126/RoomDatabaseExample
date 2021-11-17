package com.mobitising.roomdatabaseexample.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static String DataBaseName="TasksDatabase";
    public abstract TaskDao taskDao();
}
