package com.moringaschool.mvvm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {

    private List<Note> notes = new ArrayList<>(); //We initialize the List with 'new ArrayList<>()' because when this List is first created, it contains a null value, before the LiveData has been updated, thus causing references to a null object in the Adapter override methods. We tackle this by initializing it, else we would have to handle the null checks.
    private OnItemClickListener listener;

    protected NoteAdapter(@NonNull DiffUtil.ItemCallback<Note> diffCallback) {
        super(diffCallback);
    }


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
        Note currentNote = notes.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));
        holder.textViewDescription.setText(currentNote.getDescription());
    }

    //Adapter override method
    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) { //In our main activity, we observe the LiveData, and in the 'onChanged()' callback, we get passed a list of notes. This method is used to supply the recyclerView a 'List<Note>'
        this.notes = notes;
        notifyDataSetChanged(); //This method notifies Adapter that the dataset in the DB has changed. It is however not recommended to use this. It does not allow animations in the RecyclerView.
    }

    public Note getNotePosition(int position) {
        return notes.get(position);
    }

    //The Adapter needs a ViewHolder, which we can design our custom one.
    //ViewHolder
    class NoteHolder extends RecyclerView.ViewHolder { //Holds the view for each RecyclerView item.

        private TextView textViewTitle;
        private TextView textViewPriority;
        private TextView textViewDescription;


        //Constructor
        public NoteHolder(@NonNull View itemView) { //Passes the individual Card item that we designed, holding one note.
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
            textViewTitle = itemView.findViewById(R.id.text_view_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) { //Check that we call setOnItemClickListener(), otherwise, it will be null.
                        // This prevents a crash
                        //'position != RecyclerView.NO_POSITION' checks that we do not click on an item inside the recycler view with an invalid position, which could be the case if we delete an item, but it's still in it's delete animation, in which case the position could be NO_POSITION, of -1.
                        listener.onItemClick(notes.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) { //'OnItemClickListener' is the one above, (take care of the package name)
        this.listener = listener; //We use the 'listener' variable to call 'onItemClick()' method, and pass a Note object to whatever method implements this interface

    }
}
