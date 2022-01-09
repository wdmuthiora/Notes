package com.moringaschool.mvvm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao  // Dao- This annotation tells Room that this interface is a Database Access Object. It's good practice to have one Dao per entity.
public interface NoteDao {

    @Insert //this annotation tells Room to generate code that inserts this Note object to db
    void insert(Note note);
    @Update //this annotation tells Room to generate code that update this Note object in db
    void update(Note note);
    @Delete//this annotation tells Room to generate code that deletes this Note object from db
    void delete(Note note);
    @Query("DELETE FROM `note-table`")//define database annotation manually, in form of a string
    void deleteAll();
    @Query("SELECT * FROM `note-table` ORDER BY priority_column DESC")
    LiveData<List<Note>> getAllNotes(); //Return LiveData  of List of Notes. You can observe LiveData


    //click on annotation, then Ctrl+B to open corresponding Java file for each annotation.
    //Use @Query annotation to define custom SQL operations.
    //LiveData is just a wrapper around any Java type.
}
