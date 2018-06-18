package data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Favourite.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavouriteDao favouriteDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "fortnite-pro")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
