package Data;
import android.arch.lifecycle.LiveData;

import java.util.List;
import java.util.ListIterator;

import Data.Database.Prompt;
import Data.Database.PromptDao;
import Utils.AppExecutors;

public class PromptlyRepo {
    private static final Object LOCK = new Object();
    private static PromptlyRepo sInstance;
    private final PromptDao mPromptDao;
    private final AppExecutors mExecutors;
    private boolean mInitialized = false;

    private PromptlyRepo(PromptDao promptDao, AppExecutors appExecutors){
        this.mPromptDao = promptDao;
        this.mExecutors = appExecutors;

    }

    public synchronized static PromptlyRepo getsInstance(PromptDao promptDao,AppExecutors appExecutors ){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new PromptlyRepo(promptDao,appExecutors);
            }
        }
        return sInstance;
    }

    public LiveData<List<Prompt>> getPromptList(){
        return mPromptDao.getAllPrompts();
    }
    public void savePrompt(Prompt prompt){
        mPromptDao.Insert(prompt);
    }




}
