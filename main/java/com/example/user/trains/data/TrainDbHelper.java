package com.example.user.trains.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.trains.data.TrainContract.Measurement;

public class TrainDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = TrainDbHelper.class.getSimpleName();

    /*
    DB name
     */

    private static final String DATABASE_NAME = "train.db";

    /*
    DB version. При изменении схемы увеличить на единицу
     */

    private static final int DATABASE_VERSION = 1;

    /**
     * Конструктор {@link TrainDbHelper}.
     *
     * @param context Контекст приложения
     */
    public TrainDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Вызывается при создании базы данных
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_TRAIN_TABLE = "CREATE TABLE " + Measurement.TABLE_NAME + " ("
                + Measurement._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Measurement.COLUMN_DATE + " INTEGER NOT NULL, "
                + Measurement.COLUMN_DISTANCE + " INTEGER NOT NULL, "
                + Measurement.COLUMN_PULS + " INTEGER NOT NULL, "
                + Measurement.COLUMN_TRAIN_TIME + " INTEGER NOT NULL, "
                + Measurement.COLUMN_FEELING + " INTEGER NOT NULL DEFAULT 5);";

        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TRAIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
