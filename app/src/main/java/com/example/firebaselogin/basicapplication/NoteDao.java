package com.example.firebaselogin.basicapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
//if we make any mistakes here it automatically shows up
@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);
    @Update
    void update(Note note);
    @Delete
    void delete(Note note);
    //QUERY IS FOR USER DEFINED OPERATION WE WANT TO PERFORM
    @Query("DELETE FROM note_table")
    void deleteAllNote();

    @Query("SELECT * FROM note_table ORDER BY table_priority ASC")
    LiveData<List<Note>> getAllNotes();
}
