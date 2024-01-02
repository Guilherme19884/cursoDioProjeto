package com.example.dio1.ui

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dio1.R
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class CalcularAutonomiaActivity : AppCompatActivity() {

    lateinit var precoLitro : EditText;
    lateinit var kmPercorrido : EditText;
    lateinit var resultado : TextView;
    lateinit var btnCalcular : Button;
    lateinit var icVoltar : ImageView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcular_autonomia)
        setupView();
        setupListeners();
    }

    fun setupView(){
        btnCalcular = findViewById(R.id.bt_calcular);
        precoLitro = findViewById(R.id.et_precoLitro);
        kmPercorrido = findViewById(R.id.et_kmPercorrido);
        resultado = findViewById(R.id.tv_resultado);
        icVoltar = findViewById(R.id.ic_voltar);
    }

    fun setupListeners(){
        btnCalcular.setOnClickListener {
            calcular();
        }
        icVoltar.setOnClickListener{
            finish();
        }
    }

    fun calcular(){
        val preco = precoLitro.text.toString().toFloat();
        val km = kmPercorrido.text.toString().toFloat();
        if (precoLitro.toString().isNotBlank()) {
            val precoLitroValue = precoLitro.toString().toFloat()
            val result = preco / km;
            resultado.text = result.toString();
            saveSharedPref(result);
        } else {
            // Tratar o caso em que a string está vazia
            resultado.text = "O campo preço por litro, não pode estar vazio."
        }
    }

    fun saveSharedPref(resultado : Float){
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putFloat(getString(R.string.saved_calc), resultado)
            apply() //parei o vídeo em 07:52 minutos do vídeo sharedPreferences com Android
        }
    }

}
