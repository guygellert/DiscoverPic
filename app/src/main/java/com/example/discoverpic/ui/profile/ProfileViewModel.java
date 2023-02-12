package com.example.discoverpic.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.discoverpic.model.Model;
import com.example.discoverpic.model.Post;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    private LiveData<List<Post>> data = Model.instance().getUserPosts();

    public LiveData<List<Post>> getData(){
        data = Model.instance().getUserPosts();
        return data;
    }
}