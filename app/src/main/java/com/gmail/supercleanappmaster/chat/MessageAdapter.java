package com.gmail.supercleanappmaster.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
/*
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<ChatMessageModel> nMessageList;

    public MessageAdapter(List<ChatMessageModel> nMessageList) {
        //this.nMessageList = nMessageList;
    }


    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout_sender, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessageModel messageModel = nMessageList.get(position);
        //holder.nMessages_text_view.setText(messageModel.getClient_message());
        holder.nMessages_text_view.setText(messageModel.getEmployee_message());

    }

    @Override
    public int getItemCount() {
        return nMessageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView nMessages_text_view;

        public MessageViewHolder(@NonNull View view) {
            super(view);

            nMessages_text_view = view.findViewById(R.id.message_text_view);
        }
    }
}
*/