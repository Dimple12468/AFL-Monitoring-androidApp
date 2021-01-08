package com.example.afl_monitoringandroidapp.adminTabs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class adminStatsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public adminStatsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is stats fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
