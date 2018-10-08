package Utils;


import android.content.Context;

import Data.Database.AppDatabase;
import Data.PromptlyRepo;

public class InjectorUtils {

    private static PromptlyRepo promptlyRepo(Context context){
        AppDatabase database = AppDatabase.getsInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return PromptlyRepo.getsInstance(database.promptDao(),executors);
    }


}
