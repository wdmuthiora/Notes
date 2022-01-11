package com.moringaschool.mvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
    //For Intent-EXTRA, we need a Key to pass the data (value in key-value pair).
    //It's best practise to store keys as constants.
    //Best practice for Intent Extra keys is using the package name to keep them unique

    //Constants.
    public static final String EXTRA_ID = "com.moringaschool.mvvm.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.moringaschool.mvvm.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.moringaschool.mvvm.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.moringaschool.mvvm.EXTRA_PRIORITY";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextDescription = findViewById(R.id.edit_text_description);
        editTextTitle = findViewById(R.id.edit_text_title);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        //Setting the constraint values/range of the NumberPicker.
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close); //place the 'x' button on the top-left corner. Modify the Manifest file to handle using it to navigate back.

        Intent intent = getIntent(); //Intent that has extra means it's up for editing.
        if (intent.hasExtra(EXTRA_ID)) { //Get intent extra values from the clicked Card inside the RecyclerView, to edit.
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));

        } else {
            setTitle("Add note"); //Place this text onto the Action Bar (Top bit of the app)
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu); //Tell the system to use this custom menu for our app.
        return true; //'True' means the menu should be displayed. 'False' means the menu should not be displayed.
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //Handle clicks on menu items
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        //Validation
        if (title.trim().isEmpty() || description.trim().isEmpty()) { //'.trim' removes the empty spaces
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT);
            return; //Exit 'saveNote()' if either title or description is blank. Otherwise, proceed to saving
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra(EXTRA_DESCRIPTION, description);

        int id = getIntent().getIntExtra(EXTRA_ID, -1); //Default value is -1 because no entry in the DB will have this value, this no conflict, and the ID is invalid.

        if (id!=-1){ //Only add intent extra of ID if the value is not -1.
            data.putExtra(EXTRA_ID, id);
        }

        //If validation was successful, set result as value of 'RESULT_OK', and pass the intent Extra data to the next Activity
        setResult(RESULT_OK, data); //'RESULT_OK' - this is simply an integer constant.
        finish(); //To close this activity

    }


}