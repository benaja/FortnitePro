package data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FavouriteRepository {
    private FavouriteDao favouriteDao;
    private LiveData<List<Favourite>> allFavourites;

    FavouriteRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        favouriteDao = db.favouriteDao();
        allFavourites = favouriteDao.getAll();
    }

    LiveData<List<Favourite>> getAllFavourites() {
        return allFavourites;
    }


    public void insert (Favourite favourite) {
        new insertAsyncTask(favouriteDao).execute(favourite);
    }

    private static class insertAsyncTask extends AsyncTask<Favourite, Void, Void> {

        private FavouriteDao mAsyncTaskDao;

        insertAsyncTask(FavouriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Favourite... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
