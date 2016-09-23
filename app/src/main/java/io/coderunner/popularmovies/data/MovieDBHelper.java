package io.coderunner.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import io.coderunner.popularmovies.data.MovieContract.MovieEntry;

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
            MovieEntry._ID + " INTEGER PRIMARY KEY," +

            MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
            MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NULL," +
            MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NULL," +
            MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NULL," +
            MovieEntry.COLUMN_PLOT_SYNOPSYS + " TEXT NULL," +
            MovieEntry.COLUMN_RELEASE_DATE + " TEXT NULL," +
            MovieEntry.COLUMN_POSTER_PATH + " TEXT NULL," +
            MovieEntry.COLUMN_POPULARITY + " TEXT NULL," +
            MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NULL," +
            MovieEntry.COLUMN_TITLE + " TEXT NOT NULL" +
         ");";

        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
