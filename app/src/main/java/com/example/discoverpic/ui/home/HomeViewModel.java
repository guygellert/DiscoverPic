package com.example.discoverpic.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.discoverpic.model.Model;
import com.example.discoverpic.model.Post;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }
    private LiveData<List<Post>> questionList = Model.instance().getAllPosts();
    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<List<Post>> getAllPosts(){
        return questionList;
    }

}