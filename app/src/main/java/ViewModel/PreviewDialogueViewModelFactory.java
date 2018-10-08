package ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import Data.PromptlyRepo;

public class PreviewDialogueViewModelFactory extends ViewModelProvider.NewInstanceFactory {
private final PromptlyRepo mRepo;
public PreviewDialogueViewModelFactory(PromptlyRepo repo){
        this.mRepo = repo;
        }

@NonNull
@SuppressWarnings({"Unchecked", "unchecked"})
@Override
public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PreviewDialogueViewModel(mRepo);
        }
}