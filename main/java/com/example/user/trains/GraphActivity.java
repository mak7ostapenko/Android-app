package com.example.user.trains;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.user.trains.data.TrainContract;
import com.example.user.trains.data.TrainDbHelper;





public class GraphActivity extends AppCompatActivity {

    private TrainDbHelper mDbHelper;
    private Spinner spinnerX;
    private Spinner spinnerY;
    private int axisX;
    private int axisY;
    private GraphView graph;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerX = (Spinner) findViewById(R.id.spinnerX);
        setupSpinnerX();
        spinnerY = (Spinner) findViewById(R.id.spinnerY);
        setupSpinnerY();


        mDbHelper = new TrainDbHelper(this);
    }



    public void graphOnClick(View view) {
        graph = (GraphView) findViewById(R.id.graph);

        setupSpinnerX();

        setupSpinnerY();

        int[][] r = showGraph();
        int[] x = r[0];
        int[] y = r[1];

        DataPoint points[] = new DataPoint[x.length];
        for(int i = 0; i < x.length; i++) {
            points[i] = new DataPoint(x[i], y[i]);
        }
        graph.removeAllSeries();

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

        // set manual X bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-150);
        graph.getViewport().setMaxY(150);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(4);
        graph.getViewport().setMaxX(80);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        graph.addSeries(series);
    }






    private void setupSpinnerX() {

        ArrayAdapter spinnerXAdapter = ArrayAdapter.createFromResource(this,
                R.array.axis, android.R.layout.simple_spinner_item);

        spinnerXAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinnerX.setAdapter(spinnerXAdapter);
        spinnerX.setSelection(0);

        spinnerX.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {

                    if (selection.equals(getString(R.string.date))) {
                        axisX = 1;
                    } else if (selection.equals(getString(R.string.distance))) {
                        axisX = 2;
                    } else if (selection.equals(getString(R.string.puls))){
                        axisX = 3;
                    } else if (selection.equals(getString(R.string.training_time))){
                        axisX = 4;
                    } else if(selection.equals(getString(R.string.feeling))){
                        axisX = 5;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                axisX = 1;
            }
        });
    }




    private void setupSpinnerY() {
        ArrayAdapter spinnerYAdapter = ArrayAdapter.createFromResource(this,
                R.array.axis, android.R.layout.simple_spinner_item);

        spinnerYAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinnerY.setAdapter(spinnerYAdapter);
        spinnerY.setSelection(0);

        spinnerY.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {

                    if (selection.equals(getString(R.string.date))) {
                        axisY = 1;
                    } else if (selection.equals(getString(R.string.distance))) {
                        axisY = 2;
                    } else if (selection.equals(getString(R.string.puls))){
                        axisY = 3;
                    } else if (selection.equals(getString(R.string.training_time))){
                        axisY = 4;
                    } else if(selection.equals(getString(R.string.feeling))){
                        axisY = 5;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                axisY = 1;
            }
        });
    }






    private int[][] showGraph() {
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String parameterX;
        String parameterY;


        if (axisX==1) {
            parameterX = TrainContract.Measurement._ID;
        } else if(axisX==2){
            parameterX = TrainContract.Measurement.COLUMN_DISTANCE;
        } else if(axisX==3){
            parameterX = TrainContract.Measurement.COLUMN_PULS;
        } else if(axisX==4){
            parameterX = TrainContract.Measurement.COLUMN_TRAIN_TIME;
        } else if (axisX==5){
            parameterX = TrainContract.Measurement.COLUMN_FEELING;
        } else {
            parameterX = TrainContract.Measurement._ID;
        }

        if (axisY==1) {
            parameterY = TrainContract.Measurement._ID;
        } else if(axisY==2){
            parameterY = TrainContract.Measurement.COLUMN_DISTANCE;
        } else if(axisY==3){
            parameterY = TrainContract.Measurement.COLUMN_PULS;
        } else if(axisY==4){
            parameterY = TrainContract.Measurement.COLUMN_TRAIN_TIME;
        } else if (axisY==5){
            parameterY = TrainContract.Measurement.COLUMN_FEELING;
        } else {
            parameterY = TrainContract.Measurement._ID;
        }

        String[] projection = new String[2];

        // Зададим условие для выборки - список столбцов
        if(parameterX == parameterY) {
            projection[0] = parameterX;
        } else{
            projection[0] = parameterX;
            projection[1] = parameterY;
        }

        // Делаем запрос
        Cursor cursor = db.query(
                TrainContract.Measurement.TABLE_NAME,
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        int ret_x[] = new int[cursor.getCount()];
        int ret_y[] = new int[cursor.getCount()];
        int i = 0;

        try{

            // Узнаем индекс каждого столбца
            int xColumnIndex = cursor.getColumnIndex(parameterX);
            int yColumnIndex = cursor.getColumnIndex(parameterY);

            // Проходим через все ряды
            while(cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentX = cursor.getInt(xColumnIndex);
                int currentY = cursor.getInt(yColumnIndex);

                ret_x[i] = currentX;
                ret_y[i] = currentY;

                ++i;
            }
        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }

        int[][] ret = {ret_x, ret_y};
        return ret;
    }

}
