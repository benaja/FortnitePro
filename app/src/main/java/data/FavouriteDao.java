package data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavouriteDao {

    @Query("SELECT * FROM favourites")
    LiveData<List<Favourite>> getAll();

    @Query("SELECT * from favourites where id = :id")
    Favourite loadById(int id);

    @Insert
    void insert(Favourite favourite);

    //@Insert
    //void insertAll(Favourite... favourites);

    @Delete
    void delete(Favourite favourite);
}
