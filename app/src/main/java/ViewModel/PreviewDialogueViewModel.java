package ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import Data.Database.Prompt;
import Data.PromptlyRepo;

public class PreviewDialogueViewModel extends ViewModel {
    private final PromptlyRepo mRepo;
    private MutableLiveData<String[]> mPromptDetails;



    public PreviewDialogueViewModel(PromptlyRepo repo) {
        this.mRepo = repo;
        mPromptDetails = new MutableLiveData<>();
    }
    public void savePrompt(Prompt prompt){
        mRepo.savePrompt(prompt);
    }
    public MutableLiveData<String[]> getPrompt(){
        return mPromptDetails;
    }
    public void setPromptData(String[] promptData){
        mPromptDetails.postValue(promptData);
    }

}
