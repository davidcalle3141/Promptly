package Data.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PromptDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void Insert(Prompt... prompt);

    @Query("SELECT * FROM Prompt WHERE id = :id")
    LiveData<Prompt> selectPrompt(int id);

    @Query("Select * FROM Prompt")
    LiveData<List<Prompt>> getAllPrompts();

    @Query("DELETE FROM Prompt WHERE id = :id")
    void deletePrompt(int id);

}

