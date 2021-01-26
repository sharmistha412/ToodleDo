package com.example.firebaselogin.basicapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ListAdapter<Note,NoteAdapter.NoteHolder> {

    private onItemClickListener listener;

    protected NoteAdapter() {
        super(DIFF_CALLBACK);
    }
    //this is for the animation
    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK= new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId()==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getDescription().equals(newItem.getDescription()) && oldItem.getPriority()
                    ==newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item_view,parent,false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {

        Note currentNote = getItem(position);
        holder.textviewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentNote.getPriority()));

    }

//    @Override
//    public int getItemCount() {
//        return notes.size();
//    }
//
//    public void setNotes(List<Note> notes){
//        this.notes =notes;
//        notifyDataSetChanged();
//    }
    public Note getNoteAt(int position){
        return getItem(position);
    }

    //here we get all the elements from the card View first we have to fill this method and connect everything
    class NoteHolder extends RecyclerView.ViewHolder{
        private TextView textviewTitle;
        private TextView textViewDescription;
        private  TextView textViewPriority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textviewTitle=itemView.findViewById(R.id.text_view_title);
            textViewDescription=itemView.findViewById(R.id.text_view_description);
            textViewPriority=itemView.findViewById(R.id.text_view_priority);

            //setting onclick LIstener on each card view
            //same as button
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    //if we click somewhere else it will crash so be safe
                    if(listener!=null && position!=RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }
   //this method is to update the elements by clicking on them
    public interface onItemClickListener{
        void onItemClick(Note note);
    }
    public void setOnItemClickListener(onItemClickListener listener){
        this.listener=listener;
    }
}
