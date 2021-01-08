package com.example.afl_monitoringandroidapp.adminTabs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class adminHomeViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public adminHomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
