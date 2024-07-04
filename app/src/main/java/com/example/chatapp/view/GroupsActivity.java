package com.example.chatapp.view;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityGroupsBinding;
import com.example.chatapp.model.ChatGroup;
import com.example.chatapp.view.adapters.myAdapter;
import com.example.chatapp.viewModel.MyViewModel;

import java.util.ArrayList;
import java.util.List;

public class GroupsActivity extends AppCompatActivity {

    private ArrayList<ChatGroup> chatGroups = new ArrayList<>();
    private myAdapter adapter;
    private MyViewModel viewModel;
    private ActivityGroupsBinding binding;
    private Dialog chatGroupDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_groups);
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);

        setupRecyclerView();

        observeGroupList();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.recyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new myAdapter(chatGroups); // Pass in the list of chat groups
        recyclerView.setAdapter(adapter);
    }

    private void observeGroupList() {
        viewModel.getGroupList().observe(this, chatGroups -> {
            this.chatGroups.clear(); // Clear existing list
            this.chatGroups.addAll(chatGroups); // Add updated list from ViewModel
            adapter.notifyDataSetChanged(); // Notify adapter of data set change
        });
    }

    private void showDialog() {
        chatGroupDialog = new Dialog(this);
        chatGroupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        chatGroupDialog.setContentView(v);
        chatGroupDialog.show();
        Button submit = v.findViewById(R.id.button);
        EditText edt = v.findViewById(R.id.editText);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = edt.getText().toString();
                viewModel.createNewGroup(groupName); // Call ViewModel method to create new group
                chatGroupDialog.dismiss();
            }
        });
    }
}
