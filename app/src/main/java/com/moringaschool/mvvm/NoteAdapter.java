package com.moringaschool.mvvm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter <NoteAdapter.NoteHolder> {

    private List<Note> notes = new ArrayList<>(); //We initialize the List with 'new ArrayList<>()' because when this List is first created, it contains a null value, before the LiveData has been updated, thus causing references to a null object in the Adapter override methods. We tackle this by initializing it, else we would have to handle the null checks.

    //Adapter override method
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    //Adapter override method
    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) { //This is where we insert our Note object data into the Views of the View item.
        Note currentNote= notes.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
        holder.textViewDescription.setText(currentNote.getDescription());
    }

    //Adapter override method
    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes){ //In our main activity, we observe the LiveData, and in the 'onChanged()' callback, we get passed a list of notes. This method is used to supply the recyclerView a 'List<Note>'
        this.notes = notes;
        notifyDataSetChanged(); //This method notifies Adapter that the dataset in the DB has changed. It is however not recommended to use this. It does not allow animations in the RecyclerView.
    }

    public Note getNotePosition(int position){
        return notes.get(position);
    }

    //The Adapter needs a ViewHolder, which we can design our custom one.
   //ViewHolder
    class NoteHolder extends RecyclerView.ViewHolder{ //Holds the view for each RecyclerView item.

        private TextView textViewTitle;
        private TextView textViewPriority;
        private TextView textViewDescription;


        //Constructor
        public NoteHolder(@NonNull View itemView) { //Passes the individual Card item that we designed, holding one note.
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
            textViewTitle = itemView.findViewById(R.id.text_view_title);

        }
    }
}
