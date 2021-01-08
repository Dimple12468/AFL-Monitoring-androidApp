package com.example.afl_monitoringandroidapp.adminTabs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class adminLocationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public adminLocationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is location fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
