package com.example.licenseapp.Messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.licenseapp.R;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    Context context;
    ArrayList<MessagesModel> messagesModels;

    public MessagesAdapter(Context context, ArrayList<MessagesModel> messagesModels){
        this.context = context;
        this.messagesModels = messagesModels;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.message_row, parent,false);
        return new MessagesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {
        //assigning values to the view we created in message_row layout file
        //based on the position of the recyler view
        holder.tvMessageID.setText(messagesModels.get(position).getMessageID());
        holder.tvMessageText.setText(messagesModels.get(position).getMessageText());
        holder.tvMessageDescription.setText(messagesModels.get(position).getMessageDescription());
    }

    @Override
    public int getItemCount() {
        //getting total
        return messagesModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //grabbing veiws from message_row layotu file (similar to oncreate method)

        TextView tvMessageID, tvMessageText, tvMessageDescription;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageID = itemView.findViewById(R.id.messageID);
            tvMessageText = itemView.findViewById(R.id.messageText);
            tvMessageDescription = itemView.findViewById(R.id.messageDescription);
        }
    }
}
