package com.example.user.trains;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.widget.Toast;


import com.example.user.trains.data.TrainContract;
import com.example.user.trains.data.TrainContract.Measurement;
import com.example.user.trains.data.TrainDbHelper;

public class AddActivity extends AppCompatActivity {

    private EditText dateET;
    private EditText distanceET;
    private EditText pulsET;
    private EditText train_timeET;
    private Spinner feelSpinner;

    private int feelingScal = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dateET = (EditText) findViewById(R.id.date);
        distanceET = (EditText) findViewById(R.id.distance);
        pulsET = (EditText) findViewById(R.id.puls);
        train_timeET = (EditText) findViewById(R.id.train_time);

        feelSpinner = (Spinner) findViewById(R.id.spinner);
        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter feelSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.feeling, android.R.layout.simple_spinner_item);

        feelSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        feelSpinner.setAdapter(feelSpinnerAdapter);
        feelSpinner.setSelection(2);

        feelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.bad))) {
                        feelingScal = TrainContract.Measurement.BAD;
                    } else if (selection.equals(getString(R.string.good))) {
                        feelingScal = TrainContract.Measurement.GOOD;
                    } else if (selection.equals(getString(R.string.normal))){
                        feelingScal = TrainContract.Measurement.NORMAL;
                    } else if (selection.equals(getString(R.string.discomfort))){
                        feelingScal = TrainContract.Measurement.DISCOMFORT;
                    } else {
                        feelingScal = TrainContract.Measurement.PERFECT;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                feelingScal = 5;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertMeasurement();
                // Закрываем активность
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Сохраняем введенные данные в базе данных.
     */
    private void insertMeasurement() {
        // Считываем данные из текстовых полей
        String dateS  = dateET.getText().toString().trim();
        int date = Integer.parseInt(dateS);

        String distanceS = distanceET.getText().toString().trim();
        int distance = Integer.parseInt(distanceS);

        String pulsS = pulsET.getText().toString().trim();
        int puls = Integer.parseInt(pulsS);

        String train_timeS = train_timeET.getText().toString().trim();
        int train_time = Integer.parseInt(train_timeS);

        TrainDbHelper mDbHelper = new TrainDbHelper(this);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Measurement.COLUMN_DATE, date);
        values.put(Measurement.COLUMN_DISTANCE, distance);
        values.put(Measurement.COLUMN_PULS, puls);
        values.put(Measurement.COLUMN_TRAIN_TIME, train_time);
        values.put(Measurement.COLUMN_FEELING, feelingScal);

        // Вставляем новый ряд в базу данных и запоминаем его идентификатор
        long newRowId = db.insert(Measurement.TABLE_NAME, null, values);

        // Выводим сообщение в успешном случае или при ошибке
        if (newRowId == -1) {
            // Если ID  -1, значит произошла ошибка
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Training number: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }
}