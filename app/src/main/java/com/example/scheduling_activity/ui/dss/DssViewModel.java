package com.example.scheduling_activity.ui.dss;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DssViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DssViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is DSS fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
