package com.example.dio1.data.local

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.dio1.Dominio.Carro

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
}