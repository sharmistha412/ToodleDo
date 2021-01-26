package com.example.firebaselogin.basicapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class Add_Edit_note extends AppCompatActivity {

    public static final String EXTRA_ID="com.example.firebaselogin.basicapplication.EXTRA_ID";
    public static final String EXTRA_TITLE="com.example.firebaselogin.basicapplication.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION="com.example.firebaselogin.basicapplication.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIOTY="com.example.firebaselogin.basicapplication.EXTRA_PRIOTY";

    private EditText editTextTitle,editTextDescription;
    private NumberPicker numberPickerPriority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle=findViewById(R.id.edit_text_title);
        editTextDescription=findViewById(R.id.edit_text_description);
        numberPickerPriority=findViewById(R.id.numberPicker);
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.cross);


        //it will work only if the db has an id so that it will send the data back ,so that we can edit
        Intent intent=getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIOTY,1));
        }else {
            setTitle("Add Note");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.save_note,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
  //AFTER WE CLICK SAVE ON ADD_NOTE
    private void saveNote() {
        String title=editTextTitle.getText().toString();
        String description=editTextDescription.getText().toString();
        int priority=numberPickerPriority.getValue();
        if(title.trim().isEmpty() || description.trim().isEmpty() ){
            Toast.makeText(this,"Please enter a title and description",Toast.LENGTH_SHORT).show();
            Intent newIntent=new Intent(Add_Edit_note.this,RecyclerViewPage.class);
            startActivity(newIntent);
            finish();
        }
        else if(title.trim().isEmpty()  ){
            Toast.makeText(this,"Please enter a title",Toast.LENGTH_SHORT).show();
        }
        else if(description.trim().isEmpty() ){
            Toast.makeText(this,"Please enter description",Toast.LENGTH_SHORT).show();
        }
        //returning back the data TO RECYCLE VIEW
        Intent returndata=getIntent();
        returndata.putExtra(EXTRA_TITLE,title);
        returndata.putExtra(EXTRA_DESCRIPTION,description);
        returndata.putExtra(EXTRA_PRIOTY,priority);

        int id=getIntent().getIntExtra(EXTRA_ID,-1);
        if(id!=-1){
            returndata.putExtra(EXTRA_ID,id);
        }
        setResult(RESULT_OK,returndata);
        finish();


    }
}