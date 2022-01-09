package com.moringaschool.mvvm;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository (Application application){ //constructor used to assign the member variables. We need an instance of the Database, and LiveData of the contents of the Database. Application is a subclass of context, and was also used in NoteDatabase instance builder.

        NoteDatabase database = NoteDatabase.getInstance(application); //Get an instance of NoteDatabase
        noteDao = database.noteDao(); //To access the 'noteDao()' inside the NoteDatabase.java class
        allNotes = noteDao.getAllNotes(); //To expose the LiveData
    }


    //From MVVM model, the ViewModel, only needs to call these methods from the Repository, to access the Database. This helps create an abstraction layer between ViewModel and Database, Room.
    //These operations need to be executed in the background, because Room does not allow Database operations on the main thread, else the app will crash.
    public void insert(Note note){
        //Use AsyncTask below.
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note){
        //Use AsyncTask below.

        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note){
        //Use AsyncTask below.

        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes(Note note){
        //Use AsyncTask below.

        new DeleteAllNoteAsyncTask(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes(){//Room automatically execute database operations that return LiveData in the background thread, thus we don't have to do anything else to return LiveData.
        return allNotes;
    }


    //Make inserting a note to db an asynchronous task
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void>{ //'Static' so that it does NOT contain reference to the Repository itself, else a memory leak could occur.
        //AsyncTask<'pass Note object', 'no progress update', 'return nothing'>

        private NoteDao noteDao; //Used to make NoteDatabase operations.
        //constructor
        private InsertNoteAsyncTask(NoteDao noteDao){ // Since 'InsertNoteAsyncTask' is static, we cannot access the Repository NoteDao directly, thus we use the constructor.
            this.noteDao=noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) { //'Note... note' the parameters act like array.
            noteDao.insert(notes[0]);
            return null;
        }

    }

    //Make Updating a note to db an asynchronous task
    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void>{ //'Static' so that it does NOT contain reference to the Repository itself, else a memory leak could occur.
        //AsyncTask<'pass Note object', 'no progress update', 'return nothing'>

        private NoteDao noteDao; //Used to make NoteDatabase operations.
        //constructor
        private UpdateNoteAsyncTask(NoteDao noteDao){ // Since 'InsertNoteAsyncTask' is static, we cannot access the Repository NoteDao directly, thus we use the constructor.
            this.noteDao=noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) { //'Note... note' the parameters act like array.
            noteDao.update(notes[0]);
            return null;
        }

    }

    //Make Deleting a note to db an asynchronous task
    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void>{ //'Static' so that it does NOT contain reference to the Repository itself, else a memory leak could occur.
        //AsyncTask<'pass Note object', 'no progress update', 'return nothing'>

        private NoteDao noteDao; //Used to make NoteDatabase operations.

        //constructor
        private DeleteNoteAsyncTask(NoteDao noteDao){ // Since 'DeleteNoteAsyncTask' is static, we cannot access the Repository NoteDao directly, thus we use the constructor.
            this.noteDao=noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) { //'Note... note' the parameters act like array.
            noteDao.delete(notes[0]);
            return null;
        }

    }

    //Make Deleting all notes to db an asynchronous task
    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void>{ //'Static' so that it does NOT contain reference to the Repository itself, else a memory leak could occur.
        //AsyncTask<'don't pass Note object', 'no progress update', 'return nothing'>

        private NoteDao noteDao; //Used to make NoteDatabase operations.

        //constructor
        private DeleteAllNoteAsyncTask(NoteDao noteDao){ // Since 'DeleteAllNoteAsyncTask' is static, we cannot access the Repository NoteDao directly, thus we use the constructor.
            this.noteDao=noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAll();
            return null;
        }

    }

}
