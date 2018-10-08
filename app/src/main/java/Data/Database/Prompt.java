package Data.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Prompt {
    @PrimaryKey
    private int id;
    private String path;
    private String name;
    private String savedDate;

    public Prompt(int id, String name, String path, String savedDate){
        this.id = id;
        this.name = name;
        this.path = path;
        this.savedDate = savedDate;
    }
    @Ignore
    public Prompt() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(String savedDate) {
        this.savedDate = savedDate;
    }
}
