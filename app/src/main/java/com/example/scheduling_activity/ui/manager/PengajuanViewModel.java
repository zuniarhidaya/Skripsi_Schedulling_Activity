package com.example.scheduling_activity.ui.manager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PengajuanViewModel extends ViewModel {


    private MutableLiveData<String> mText;

    public PengajuanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is pengajuan fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
