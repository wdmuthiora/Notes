package com.moringaschool.mvvm;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note-table") //Entity-This is a Room annotation, and at compile-time creates necessary code to create an SQLite table for this object.
public class Note {

    @PrimaryKey(autoGenerate = true) //PrimaryKey-Room annotation to create unique note ids in db.
    private  int id;

    private String title;
    private String description;

    @ColumnInfo(name = "priority_column") //ColumnInfo-Room annotation to define db column name of table. Default is the variable name, ie, priority.
    private int priority;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    //constructor
    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }


}
