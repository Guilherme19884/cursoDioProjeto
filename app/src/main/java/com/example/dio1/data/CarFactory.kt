package com.example.dio1.data

import com.example.dio1.Dominio.Carro

object CarFactory {

    val list = listOf(
        Carro(
            id = 1,
            preco = "R$ 78.990,00",
            bateria = "Bateria",
            potencia = "13 km/l",
            recarga = "16,5 Km/l",
            urlPhoto = "www.google.com"
        ),
        Carro(
            id = 2,
            preco = "R$ 59.990,00",
            bateria = "bateria",
            potencia= "13 km/l",
            recarga = "16,5 Km/l",
            urlPhoto = "www.google.com"
        )
    )
}