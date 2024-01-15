package com.example.dio1.data.local

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.dio1.Dominio.Carro
import com.example.dio1.data.local.CarContract.carEntry.COLUMN_NAME_BATERIA
import com.example.dio1.data.local.CarContract.carEntry.COLUMN_NAME_POTENCIA
import com.example.dio1.data.local.CarContract.carEntry.COLUMN_NAME_PRECO
import com.example.dio1.data.local.CarContract.carEntry.COLUMN_NAME_RECARGA
import com.example.dio1.data.local.CarContract.carEntry.COLUMN_NAME_URL_PHOTO

class CarRepository (private val context: Context) {

    fun save(carro: Carro) : Boolean{
        var isSaved = false
       try {
           val dbHelper = CarsDbHelper(context)
           val db = dbHelper.writableDatabase

           val values = ContentValues().apply {
               put(CarContract.carEntry.COLUMN_NAME_PRECO, carro.preco)
               put(CarContract.carEntry.COLUMN_NAME_BATERIA, carro.bateria)
               put(CarContract.carEntry.COLUMN_NAME_POTENCIA, carro.potencia)
               put(CarContract.carEntry.COLUMN_NAME_RECARGA, carro.recarga)
               put(CarContract.carEntry.COLUMN_NAME_URL_PHOTO, carro.urlPhoto)
           }

           val inserted = db?.insert(CarContract.carEntry.TABLE_NAME, null, values)
           if (inserted != null){
               isSaved = true
           }
       }catch (ex: Exception){
          ex.message?.let{
              Log.e("Erro ao inserir -> ", it)
          }
       }
        return isSaved
    }

    fun findCarById(id: Int){
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.writableDatabase

        //Listam as colunas a serrem exibidas no resultado da query
        val columns = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_PRECO,
            COLUMN_NAME_BATERIA,
            COLUMN_NAME_POTENCIA,
            COLUMN_NAME_RECARGA,
            COLUMN_NAME_URL_PHOTO)

        val filter = "${BaseColumns._ID} = ?"
        val filterValues = arrayOf(id.toString())

        val cursor = db.query(
            CarContract.carEntry.TABLE_NAME,
            columns,
            filter,
            filterValues
        )
    }
}