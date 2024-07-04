package com.example.chatapp.view;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityChatBinding;
import com.example.chatapp.model.ChatMessage;
import com.example.chatapp.view.adapters.ChatAdapter;
import com.example.chatapp.viewModel.MyViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private List<ChatMessage> messageList;
    private RecyclerView recyclerView;
    private MyViewModel myViewModel;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        String groupName = getIntent().getStringExtra("GROUP_NAME");
        myViewModel.getMessagesLiveData(groupName).observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> chatMessages) {
                messageList = new ArrayList<>();
                messageList.addAll(chatMessages);
                chatAdapter = new ChatAdapter(messageList, getApplicationContext());
                recyclerView.setAdapter(chatAdapter);
                chatAdapter.notifyDataSetChanged();
                int latestPos = chatAdapter.getItemCount()-1;
                if(latestPos>0){
                    recyclerView.smoothScrollToPosition(latestPos);
                }

            }
        });

        binding.sendBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.sendMsg(binding.edittextChatMessage.getText().toString(), groupName);
                binding.edittextChatMessage.setText("");
            }
        });

    }
}