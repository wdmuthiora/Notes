package com.moringaschool.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel { //AndroidViewModel is a subclass of ViewModel. With AndroidViewModel, we can pass the Application in the constructor, which we can use whenever the Application Context is needed.
    // You should never store a context of an Activity, or a View that references an Activity in the ViewModel because ViewModel is designed to outlive an Activity, after it is destroyed, else memory leaks.
    // But we MUST pass a Context to our Repository, because we need it there to instantiate our database instance. Thus, AndroidViewModel is used because we can pass Application Context to Repository, in order to instantiate our Database
    //ViewModel data survives configuration changes. ViewModel is only removed from memory when the lifecycle of the corresponding Activity or Fragment is over.

    private NoteRepository repository;

    private LiveData<List<Note>> allNotes;


   //Constructor
   public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }



   //Wrapper methods for database operations, because Activity/Fragment only have reference to ViewModel, and not the Repository or database itself, but still need to call these methods below on the data.
   public void insert(Note note){
      repository.insert(note);
   }

   public void update(Note note){
      repository.update(note);
   }

   public void delete(Note note){
     repository.delete(note);
   }

   public void deleteAllNotes(){
     repository.deleteAllNotes();
   }

   public LiveData<List<Note>> getAllNotes(){
     return allNotes;
   }

}
