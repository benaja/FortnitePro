package data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favoirites")
public class Favourite {
    //@Ignore
    Favourite(String name){
        this.name = name;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
