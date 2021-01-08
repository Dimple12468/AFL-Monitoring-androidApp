package com.example.afl_monitoringandroidapp.adminTabs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class adminAdoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public adminAdoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ADO fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
