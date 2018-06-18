package data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favourites")
public class Favourite {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String name;

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
