package com.example.firebaseui_firestoreexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHoler> {
public OnItemClicListener onItemClicListener;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHoler holder, int position, @NonNull Note model) {
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewDescription.setText(model.getDescription());
        holder.textViewPriority.setText(String.valueOf(model.getPriority()));

    }
    public void delete( int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    @NonNull
    @Override
    public NoteHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteHoler(view);
    }

    public class NoteHoler extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_priority)
        TextView textViewPriority;
        @BindView(R.id.text_view_title)
        TextView textViewTitle;
        @BindView(R.id.text_view_description)
        TextView textViewDescription;
        private View view;

        public NoteHoler(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION&&onItemClicListener!=null){
                        onItemClicListener.OnClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClicListener{
        void OnClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setlistener(OnItemClicListener onItemClicListener){
        this.onItemClicListener=onItemClicListener;
    }
}
