package com.moringaschool.mvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //LayoutManager takes care of the arrangement of items inside the RecyclerView.
        recyclerView.setHasFixedSize(true); //Not necessary, but improves performance, when you have a fixed number of items to display

        NoteAdapter adapter = new NoteAdapter(); //'Final' because we try to access it through the inner class
        recyclerView.setAdapter(adapter);


        //Ask ViewModelProvider to provide
        //Scope the ViewModel to 'this' context, and then return a 'NoteViewModel' class instance.
        ////noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class); //This method was deprecated. We only want one instance of ViewModel for the lifetime of the app, not a new instance with every new activity.
        // The Android system handled providing new instance, or supplying a pre-existing instance to the Activity/Fragment



        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);

        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        //Android system will destroy this 'noteViewModel after this Activity is complete, because it has been scoped to 'this' context.
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);


        //Because this method returns LiveData, we can observe it. Remember, LiveData updates when our activity/fragment is in the foreground
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update RecyclerView
                adapter.setNotes(notes);
                Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_SHORT).show();
            }
        });


        //Swipe & Drag
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNotePosition(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //This function will still be triggered if we left the AddNoteActivity using the back button, meaning abortion of creation of a new note, but now, the result will not be set to RESULT_OK, but will be set by the system to RESULT_CANCELLED.

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode==RESULT_OK){

            String title=data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description=data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            int priority=data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1); //Integer values are not nullable, so we pass a default value, in this cae, '1'. This can also serve as a default value.

            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT);

        } else{
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT);

        }
    }

}