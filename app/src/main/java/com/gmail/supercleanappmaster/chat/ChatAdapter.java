package com.gmail.supercleanappmaster.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.gmail.supercleanappmaster.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends FirebaseRecyclerAdapter<ChatMessageModel, ChatAdapter.ChatViewHolder>
{
    private static final int RECEIVER_LAYOUT = 2;
    private static final int SENDER_LAYOUT = 1;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatAdapter(@NonNull FirebaseRecyclerOptions<ChatMessageModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int i, @NonNull ChatMessageModel model) {
            holder.nMessage_text_view.setText(model.getMessage());
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType)
        {
            case SENDER_LAYOUT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout_sender, parent, false);
                return new ChatViewHolder(view);
        }
        // RECEIVER_LAYOUT
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout_reciver, parent, false);
        return new ChatViewHolder(view);


    }

    @Override
    public int getItemViewType(int position) {

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = mUser.getUid();
        // checks for the message item and assign to respective user
        if (getItem(position).getSender_id().equals(currentUserId))
        {
            return SENDER_LAYOUT;
        }
        else
        {
            return RECEIVER_LAYOUT;
        }
    }

    class ChatViewHolder extends RecyclerView.ViewHolder
    {
        TextView  nMessage_text_view;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            nMessage_text_view = itemView.findViewById(R.id.receiver_message_text_view);

        }
    }
}
