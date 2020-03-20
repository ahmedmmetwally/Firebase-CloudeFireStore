package com.example.firebaseui_firestoreexample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.button_add_note)
    FloatingActionButton buttonAddNote;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("uinotebook");
    private NoteAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        setUpRcycler();
    }

    private void setUpRcycler() {
        Query query = notebookRef.orderBy("priority", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> option = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        adapter = new NoteAdapter(option);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.delete(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
        adapter.setlistener(new NoteAdapter.OnItemClicListener() {
            @Override
            public void OnClick(DocumentSnapshot documentSnapshot, int position) {
                Note note=documentSnapshot.toObject(Note.class);
                String id=documentSnapshot.getId();
                String title=note.getTitle();
                String description=note.getDescription();
                int priority=note.getPriority();
                Intent intent=new Intent(MainActivity.this,NewNoteActivity.class);
                intent.putExtra("priorityy",priority);
                intent.putExtra("titlee",title);
                intent.putExtra("descriptionn",description);
                intent.putExtra("id",id);
                startActivity(intent);


            }
        });
    }

    @OnClick(R.id.button_add_note)
    public void onViewClicked() {
        startActivity(new Intent(this, NewNoteActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}