package com.example.projectuts

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

    class DBOpenHelper(context: Context) : SQLiteOpenHelper(context, DB_Name, null, DB_Ver) {
        override fun onCreate(db: SQLiteDatabase?) {
            val tUser =
                "create table user(id_user integer primary key autoincrement,username text not null,password text not null)"
            val tReservasi =
                "create table reservasi(id_reservasi integer primary key autoincrement, name text not null, gender text not null, dt text not null, address text not null, phone text not null)"
            val tTreatment =
                "create table treatment(id_treatment integer primary key autoincrement, treatment text not null, customer text not null)"
            db?.execSQL(tUser)
            db?.execSQL(tReservasi)
            db?.execSQL(tTreatment)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }

        companion object {
            val DB_Name = "Pasien1"
            val DB_Ver = 1
        }
    }