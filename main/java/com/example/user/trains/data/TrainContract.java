package com.example.user.trains.data;

import android.provider.BaseColumns;


public final class TrainContract {

    private TrainContract(){

    };

    public static final class Measurement implements BaseColumns{
        public final static String TABLE_NAME = "measurement";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_DISTANCE = "distance";
        public final static String COLUMN_PULS = "puls";
        public final static String COLUMN_TRAIN_TIME = "train_time";
        public final static String COLUMN_FEELING = "feeling";

        public static final int PERFECT = 5;
        public static final int GOOD = 4;
        public static final int NORMAL = 3;
        public static final int DISCOMFORT = 2;
        public static final int BAD = 1;

    }
}