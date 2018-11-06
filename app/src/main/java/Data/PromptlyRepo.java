package Data;

import android.arch.lifecycle.LiveData;

import java.util.List;

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
    public LiveData<Prompt> getPrompt(int id ){return mPromptDao.selectPrompt(id);}
    public void savePrompt(Prompt prompt){

        mExecutors.diskIO().execute(()-> mPromptDao.Insert(prompt));
    }

    public void deletePrompt(int promptID) {
        mExecutors.diskIO().execute(() -> mPromptDao.deletePrompt(promptID));
    }




}
