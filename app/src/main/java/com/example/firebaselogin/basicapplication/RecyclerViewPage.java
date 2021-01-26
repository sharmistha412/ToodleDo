package com.example.firebaselogin.basicapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class RecyclerViewPage extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    public static final int EDIT_REQUEST_CODE = 2;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_page);
       //this will get to the add_note and return back the data from there
        Toast.makeText(RecyclerViewPage.this, "Click here to add your Note", Toast.LENGTH_LONG).show();
        FloatingActionButton btn = findViewById(R.id.float_btn2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecyclerViewPage.this, Add_Edit_note.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        //connecting the recycler view
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update RecyclerView here
                //adapter.setNotes(notes);//if you donnot use diffutil use this
                adapter.submitList(notes);//diffutil
            }
        });

        //this method is for deleting elements with swiping either right or left
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(RecyclerViewPage.this, "Note Deleted", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);

        //handling the click of cardViews to edit them
        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent=new Intent(RecyclerViewPage.this, Add_Edit_note.class);
                intent.putExtra(Add_Edit_note.EXTRA_ID,note.getId());
                intent.putExtra(Add_Edit_note.EXTRA_TITLE,note.getTitle());
                intent.putExtra(Add_Edit_note.EXTRA_DESCRIPTION,note.getDescription());
                intent.putExtra(Add_Edit_note.EXTRA_PRIOTY,note.getPriority());
                startActivityForResult(intent,EDIT_REQUEST_CODE);
            }
        });
    }
//this is getting all the values from Add_note
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode  == RESULT_OK) {
                String title = data.getStringExtra(Add_Edit_note.EXTRA_TITLE);
                String description = data.getStringExtra(Add_Edit_note.EXTRA_DESCRIPTION);
                int priority = data.getIntExtra(Add_Edit_note.EXTRA_PRIOTY, 1);

                Note note = new Note(title, description, priority);
                noteViewModel.insert(note);
                Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
            }else if(requestCode == EDIT_REQUEST_CODE && resultCode  == EDIT_REQUEST_CODE) {

                int id=data.getIntExtra(Add_Edit_note.EXTRA_ID,-1);
                if(id == -1){
                    Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                    return;
                }
            String title = data.getStringExtra(Add_Edit_note.EXTRA_TITLE);
            String description = data.getStringExtra(Add_Edit_note.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(Add_Edit_note.EXTRA_PRIOTY, 1);

            Note note=new Note(title,description,priority);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        }
           else {
                Toast.makeText(this, "Note not Saved", Toast.LENGTH_SHORT).show();
            }
    }
//this method is for the selecting and clicking delete all option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_all_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}