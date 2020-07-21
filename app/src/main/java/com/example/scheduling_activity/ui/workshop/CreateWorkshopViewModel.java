package com.example.scheduling_activity.ui.workshop;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateWorkshopViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CreateWorkshopViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is manager fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
