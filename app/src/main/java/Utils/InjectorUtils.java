package Utils;


import android.content.Context;

import Data.Database.AppDatabase;
import Data.PromptlyRepo;
import ViewModel.PreviewDialogueViewModelFactory;
import ViewModel.SavedPromptsViewModelFactory;

public class InjectorUtils {

    private static PromptlyRepo provideRepo(Context context){
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return PromptlyRepo.getsInstance(database.promptDao(),executors);
    }

    public static SavedPromptsViewModelFactory provideSavedPromptsFactory(Context context){
        PromptlyRepo repo = provideRepo(context.getApplicationContext());
        return new SavedPromptsViewModelFactory(repo);
    }

    public static PreviewDialogueViewModelFactory providePreviewDialogueFactory(Context context){
        PromptlyRepo repo = provideRepo(context.getApplicationContext());
        return new PreviewDialogueViewModelFactory(repo);
    }

}
