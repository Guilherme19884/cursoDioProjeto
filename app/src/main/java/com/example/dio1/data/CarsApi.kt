package com.example.dio1.data


import com.example.dio1.Dominio.Carro
import retrofit2.Call
import retrofit2.http.GET


interface CarsApi{

    @GET("cars.json")
    fun getAllcars(): Call<List<Carro>>
}