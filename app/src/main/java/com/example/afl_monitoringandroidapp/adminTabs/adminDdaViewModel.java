package com.example.afl_monitoringandroidapp.adminTabs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class adminDdaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public adminDdaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is DDA fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
