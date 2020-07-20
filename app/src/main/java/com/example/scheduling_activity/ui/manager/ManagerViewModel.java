package com.example.scheduling_activity.ui.manager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ManagerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ManagerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is manager fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
