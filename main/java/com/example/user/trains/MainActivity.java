package com.example.user.trains;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.user.trains.data.TrainDbHelper;

import com.example.user.trains.data.TrainContract;


public class MainActivity extends AppCompatActivity {

    private TrainDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new TrainDbHelper(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_insert_new_data:
              /*  insertMeasurement();
                displayDatabaseInfo();*/
                return true;
            case R.id.action_show_graph:
                Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }


    private void displayDatabaseInfo() {
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Зададим условие для выборки - список столбцов
        String[] projection = {
                TrainContract.Measurement._ID,
                TrainContract.Measurement.COLUMN_DATE,
                TrainContract.Measurement.COLUMN_DISTANCE,
                TrainContract.Measurement.COLUMN_PULS,
                TrainContract.Measurement.COLUMN_TRAIN_TIME,
                TrainContract.Measurement.COLUMN_FEELING };

       // String selection = TrainContract.Measurement._ID + ">?";
        //String[] selectionArgs = {"1"};

        // Делаем запрос
        Cursor cursor = db.query(
                TrainContract.Measurement.TABLE_NAME,
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        TextView displayTextView = (TextView) findViewById(R.id.text_view_info);

        try{
            displayTextView.setText("  Table contain " + cursor.getCount() + " trains. \n\n");
            displayTextView.append("   " + TrainContract.Measurement._ID + "     " +
                    TrainContract.Measurement.COLUMN_DATE + "    " +
                    TrainContract.Measurement.COLUMN_DISTANCE + "    " +
                    TrainContract.Measurement.COLUMN_PULS + "    " +
                    TrainContract.Measurement.COLUMN_TRAIN_TIME + "    " +
                    TrainContract.Measurement.COLUMN_FEELING + "\n");

            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(TrainContract.Measurement._ID);
            int dateColumnIndex = cursor.getColumnIndex(TrainContract.Measurement.COLUMN_DATE);
            int distanceColumnIndex = cursor.getColumnIndex(TrainContract.Measurement.COLUMN_DISTANCE);
            int pulsColumnIndex = cursor.getColumnIndex(TrainContract.Measurement.COLUMN_PULS);
            int train_timeColumnIndex = cursor.getColumnIndex(TrainContract.Measurement.COLUMN_TRAIN_TIME);
            int feelingColumnIndex = cursor.getColumnIndex(TrainContract.Measurement.COLUMN_FEELING);

            // Проходим через все ряды
            while(cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                int currentDate = cursor.getInt(dateColumnIndex);
                int currentDistance = cursor.getInt(distanceColumnIndex);
                int currentPuls = cursor.getInt(pulsColumnIndex);
                int currentTrain_time = cursor.getInt(train_timeColumnIndex);
                int currentFeeling = cursor.getInt(feelingColumnIndex);

                // Выводим значения каждого столбца
                displayTextView.append(("\n" +  "   " + currentID + "    " +
                        currentDate + "    " +
                        currentDistance + "    " +
                        currentPuls + "    " +
                        currentTrain_time + "    " +
                        currentFeeling));

            }
        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }
    }

}
