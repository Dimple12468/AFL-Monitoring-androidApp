package com.example.afl_monitoringandroidapp.statusTabs;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class StatusViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            String text = null;
            switch (input){
                case 1:
                    text = "This is pending Fragment";
                    break;
                case 2:
                    text = "This is ongoing fragment";
                    break;
                case 3:
                    text = "This is completed fargemnt";
                    break;

            }
            return text;
//            return "Hello world from tab: " + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }

}
