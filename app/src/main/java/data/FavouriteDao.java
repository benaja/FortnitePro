package data;

import android.arch.persistence.room.Query;

import java.util.List;

public interface FavouriteDao {
    @Query("SELECT * FROM favoirites")
    List<Favourite> getFavourites();
}
