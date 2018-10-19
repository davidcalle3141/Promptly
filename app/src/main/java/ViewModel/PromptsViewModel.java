package ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import Data.Database.Prompt;
import Data.PromptlyRepo;

public class PromptsViewModel extends ViewModel {
    private final LiveData<List<Prompt>> mPromptList;
    private MutableLiveData<String[]> mPromptDetails;
    private MutableLiveData<Prompt> mFocusedPrompt;

    private PromptlyRepo mRepo;


    public PromptsViewModel(PromptlyRepo repo) {
        this.mRepo = repo;
        mPromptList = mRepo.getPromptList();
        mPromptDetails = new MutableLiveData<>();
        mFocusedPrompt = new MutableLiveData<>();

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

    public MutableLiveData<Prompt> getFocusedPrompt() {
        return mFocusedPrompt;
    }

    public void savePrompt(Prompt prompt){
        mRepo.savePrompt(prompt);
    }
    public void setFocusedPrompt(Prompt mFocusedPrompt) {
        this.mFocusedPrompt.postValue(mFocusedPrompt);
    }
}
