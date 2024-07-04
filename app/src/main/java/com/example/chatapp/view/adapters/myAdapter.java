package com.example.chatapp.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.AndroidViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ItemCardBinding;
import com.example.chatapp.model.ChatGroup;
import com.example.chatapp.view.ChatActivity;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.GroupViewHolder> {
    private ArrayList<ChatGroup> groupArrayList;

    public myAdapter(ArrayList<ChatGroup> groupArrayList) {
        this.groupArrayList = groupArrayList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_card, parent, false);
        return new GroupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        ChatGroup group = groupArrayList.get(position);
        holder.itemCardBinding.setChatGroup(group);

    }

    @Override
    public int getItemCount() {
        return groupArrayList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        private final ItemCardBinding itemCardBinding;
        public GroupViewHolder(ItemCardBinding itemCardBinding) {
            super(itemCardBinding.getRoot());
            this.itemCardBinding = itemCardBinding;
            itemCardBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    ChatGroup currentGroup = groupArrayList.get(pos);
                    Intent i = new Intent(itemView.getContext(), ChatActivity.class);
                    i.putExtra("GROUP_NAME", currentGroup.getGroupName());
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}
