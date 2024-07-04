package com.example.chatapp.Repository;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapp.model.ChatGroup;
import com.example.chatapp.model.ChatMessage;
import com.example.chatapp.view.GroupsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    MutableLiveData<List<ChatGroup>> chatGroupList;
    MutableLiveData<List<ChatMessage>> messagesLiveData;

    FirebaseDatabase db;
    DatabaseReference ref;
    DatabaseReference groupReference;
    public Repository(){
        this.chatGroupList = new MutableLiveData<>();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();
        messagesLiveData = new MutableLiveData<>();
    }

    public void firebaseAnonympusAuth(Context context){
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent i = new Intent(context, GroupsActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }
        });
    }
    public String getUserId(){
        return FirebaseAuth.getInstance().getUid();
    }
    public void signOUT(){
        FirebaseAuth.getInstance().signOut();
    }

    public MutableLiveData<List<ChatGroup>>  getChatGroupList(){
        List<ChatGroup> groupList = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatGroup group = new ChatGroup(dataSnapshot.getKey());
                    groupList.add(group);
                }
                chatGroupList.postValue(groupList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return chatGroupList;
    }
    public void createNewChatsGroup(String groupName){
       // ChatGroup group = new ChatGroup(groupName);
        ref.child(groupName).setValue(groupName);
    }


    public MutableLiveData<List<ChatMessage>> getMessagesLiveData(String groupName) {
        groupReference = db.getReference().child(groupName);
        List<ChatMessage> messageList = new ArrayList<>();
        groupReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                    messageList.add(message);
                }
                messagesLiveData.postValue(messageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return messagesLiveData;
    }
    public void sendMessage(String messageText, String chatGroup){
        DatabaseReference ref = db.getReference(chatGroup);
        if(!messageText.trim().equals("")){
            ChatMessage msg = new ChatMessage(FirebaseAuth.getInstance().getUid(), messageText, System.currentTimeMillis());
            String randomKey = ref.push().getKey();
            ref.child(randomKey).setValue(msg);
        }

    }
}
