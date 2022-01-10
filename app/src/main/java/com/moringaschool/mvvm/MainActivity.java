package com.moringaschool.mvvm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //LayoutManager takes care of the arrangement of items inside the RecyclerView.
        //recyclerView.setHasFixedSize(true); //Not necessary, but improves performance, when you have a fixed number of items to display

        NoteAdapter adapter = new NoteAdapter(); //'Final' because we try to access it through the inner class
        recyclerView.setAdapter(adapter);

        //Ask ViewModelProvider to provide
        //Scope the ViewModel to 'this' context, and then return a 'NoteViewModel' class instance.
        ////noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class); //This method was deprecated. We only want one instance of ViewModel for the lifetime of the app, not a new instance with every new activity.
        // The Android system handled providing new instance, or supplying a pre-existing instance to the Activity/Fragment

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
    }
}