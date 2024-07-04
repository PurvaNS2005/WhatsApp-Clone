package com.example.chatapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapp.R;
import com.example.chatapp.Repository.Repository;
import com.example.chatapp.databinding.ActivityChatBinding;
import com.example.chatapp.model.ChatGroup;
import com.example.chatapp.model.ChatMessage;

import java.util.List;

public class MyViewModel extends AndroidViewModel {

    Repository repo;
    public MyViewModel(@NonNull Application application) {
        super(application);
        repo = new Repository();
    }
    public void signUpAnonymousUser(){
        repo.firebaseAnonympusAuth(this.getApplication());
    }
    public void getCurrentUSerId(){
        repo.getUserId();
    }
    public void SignOut(){
        repo.signOUT();
    }

    public MutableLiveData<List<ChatGroup>> getGroupList(){
        return repo.getChatGroupList();
    }
    public void createNewGroup(String groupname){
        repo.createNewChatsGroup(groupname);

    }
    public MutableLiveData<List<ChatMessage>> getMessagesLiveData(String groupName){
        return repo.getMessagesLiveData(groupName);
    }
    public void sendMsg(String msg, String chatGroupName){
        repo.sendMessage(msg,chatGroupName);
    }


}
