package com.example.dio1.data.local

import android.provider.BaseColumns

object CarContract {

    const val DATABASE_NAME = "db_car"
    object carEntry : BaseColumns{
        const val TABLE_NAME = "Car"
        const val COLUMN_NAME_PRECO = "preco"
        const val COLUMN_NAME_BATERIA = "bateria"
        const val COLUMN_NAME_POTENCIA = "potencia"
        const val COLUMN_NAME_RECARGA = "recarga"
        const val COLUMN_NAME_URL_PHOTO = "url_photo"
    }

    const val TABLE_CAR =
        "CREATE TABLE ${carEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${carEntry.COLUMN_NAME_PRECO} TEXT," +
                "${carEntry.COLUMN_NAME_BATERIA} TEXT," +
                "${carEntry.COLUMN_NAME_POTENCIA} TEXT," +
                "${carEntry.COLUMN_NAME_URL_PHOTO} TEXT," +
                "${carEntry.COLUMN_NAME_RECARGA} TEXT," +
                "${carEntry.COLUMN_NAME_URL_PHOTO} TEXT,)"

    const val SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS ${carEntry.TABLE_NAME}"
}