package ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import Data.Database.Prompt;
import Data.PromptlyRepo;

public class SavedPromptsViewModel extends ViewModel {
    private final LiveData<List<Prompt>> mPromptList;
    private MutableLiveData<String[]> mPromptDetails;

    private PromptlyRepo mRepo;


    public SavedPromptsViewModel(PromptlyRepo repo) {
        this.mRepo = repo;
        mPromptList = mRepo.getPromptList();
        mPromptDetails = new MutableLiveData<>();

    }

    public void setPromptData(String[] promptData){
        mPromptDetails.postValue(promptData);
    }
    public MutableLiveData<String[]> getPrompt(){
        return mPromptDetails;
    }
    public LiveData<List<Prompt>> getPromptList() {
        return mPromptList;
    }
}
