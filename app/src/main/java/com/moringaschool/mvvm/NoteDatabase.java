package com.moringaschool.mvvm;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = Note.class, version = 1) //Room Annotation. If you have more than one Model, you can replace 'Note.class' with an array '{Note.class}'. Version number is used in Db, and is incremented every time there is a change to the database, and provide a migration strategy.
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance; //We create this class because we have to change this Database class in to a Singleton, which we can then access via a static variable.
    public abstract NoteDao noteDao(); //Use this method to access Dao methods/operations on DB.

    public static synchronized NoteDatabase getInstance(Context context){ //Synchronized means only one thread can access this method at a time, to avoid database instance conflicts when different threads try to access db at the same time.
        //create a NoteDatabase instance here.

        if(instance==null){
            //'instance=new NoteDatabase' could be applicable if the NoteDatabase were not 'abstract'. Instead we use a builder to create an instance.
            instance= Room.databaseBuilder(context.getApplicationContext(),NoteDatabase.class, "note_database").fallbackToDestructiveMigration().build(); //fallbackToDestructiveMigration deletes and recreates db everytime there is a change to the version number. Helps avoid application crash due to version number changes
        }
        return instance;
    }

}
