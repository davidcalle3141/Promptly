package Utils;


import android.content.Context;

import Data.Database.AppDatabase;
import Data.PromptlyRepo;
import ViewModel.PromptsViewModelFactory;

public class InjectorUtils {

    private static PromptlyRepo provideRepo(Context context){
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return PromptlyRepo.getsInstance(database.promptDao(),executors);
    }

    public static PromptsViewModelFactory provideSavedPromptsFactory(Context context){
        PromptlyRepo repo = provideRepo(context.getApplicationContext());
        return new PromptsViewModelFactory(repo);
    }


}
