package com.example.firebaseui_firestoreexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewNoteActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_title_)
    EditText editTextTitle;
    @BindView(R.id.edit_text_Description_)
    EditText editTextDescription;
    @BindView(R.id.number_picker_priority)
    NumberPicker numberPickerPriority;
    @BindView(R.id.button)
    Button button;
    FirebaseFirestore db;
    CollectionReference notebookRef;
    DocumentReference doc;
    public String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        ButterKnife.bind(this);
            db=FirebaseFirestore.getInstance();
        notebookRef = db.collection("uinotebook");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("addNote");
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);
        Intent e = getIntent();
        if (e != null) {
            editTextTitle.setText(e.getStringExtra("titlee"));
            editTextDescription.setText(e.getStringExtra("descriptionn"));
            numberPickerPriority.setValue((e.getIntExtra("priorityy", 1)));
             id = e.getStringExtra("id");


        }

    }


    private void upDate() {

        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        int priority = numberPickerPriority.getValue();

        WriteBatch batch=db.batch();
        DocumentReference doc1= notebookRef.document(id);
        batch.update(doc1,"title",title);
        DocumentReference doc2 =notebookRef.document(id);
        batch.update(doc2,"description",description);
        DocumentReference doc3= notebookRef.document(id);
        batch.update(doc3,"priority",priority);
        batch.commit();






//        doc.update("titel", FieldValue.arrayUnion(title) );
//        doc.update("description", FieldValue.arrayUnion(description) );
//        doc.update("priority", FieldValue.arrayUnion(priority) );
        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater u = getMenuInflater();
        u.inflate(R.menu.new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.note_save:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        int priority = numberPickerPriority.getValue();
        if (title.isEmpty() || description.isEmpty())
            return;

        notebookRef.add(new Note(title, description, priority));
        Toast.makeText(this, "note saved", Toast.LENGTH_LONG).show();
        finish();
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        upDate();
    }
}
