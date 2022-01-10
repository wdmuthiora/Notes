package com.moringaschool.mvvm;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Note.class}, version = 1) //Room Annotation. If you have more than one Model, you can replace 'Note.class' with an array '{Note.class, ...}'. Version number is used in Db, and is incremented every time there is a change to the database, and provide a migration strategy.
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance; //We create this class because we have to change this Database class in to a Singleton, which we can then access via the static variable, 'instance'.

    public abstract NoteDao noteDao(); //Use this method to access Dao methods/operations on DB. To be accessed by NoteRepository.java


    //Room builder is used instead of normal constructor because it is an abstract class, to create an instance of NoteDatabase.
    public static synchronized NoteDatabase getInstance(Context context){ //Synchronized means only one thread can access this method at a time, to avoid database instance conflicts when different threads try to access db at the same time.
        //create a NoteDatabase instance here.
        //Returns a Singleton NoteDatabase instance, which we create below.

        if(instance==null){
            //'instance=new NoteDatabase()' would be applicable if the NoteDatabase were not 'abstract'. Instead we use a builder to create an instance.
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database")  //name 'note_database' is the NoteDatabase instance file name that we will use to access the Database.
                    .fallbackToDestructiveMigration() //fallbackToDestructiveMigration deletes and recreates db everytime there is a change to the version number. Helps avoid application crash due to version number changes
                    .addCallback(roomCallback) //This line can be removed. It's only here because we need to populate the Db table with initial rows.
                    .build(); //Builder returns a NoteDatabase.class object.

        }
        return instance;
    }

    //To populate Database with some data, when creating it initially. Again, this operation needs to be Asynchronous.
    private static RoomDatabase.Callback roomCallback= new RoomDatabase.Callback(){
        //use Ctrl+O to access the override methods.
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    //Async Task to populate/insert notes to Db after creating it.
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private NoteDao noteDao;

        //Constructor
        public PopulateDbAsyncTask(NoteDatabase db) {
            this.noteDao = db.noteDao(); //Access NoteDatabase's noteDao. This is possible because onCreate() is called after the database has been created.
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Add these rows to table in DB.
            noteDao.insert(new Note("Title 1", "Description 1", 1));
            noteDao.insert(new Note("Title 2", "Description 2", 2));
            noteDao.insert(new Note("Title 3", "Description 3", 3));

            return null;
        }
    }

}
