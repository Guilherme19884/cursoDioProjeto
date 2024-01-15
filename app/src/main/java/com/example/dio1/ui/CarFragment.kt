package com.example.dio1.ui

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.health.connect.datatypes.units.Length
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dio1.Dominio.Carro
import com.example.dio1.R
import com.example.dio1.data.CarsApi
import com.example.dio1.data.local.CarContract
import com.example.dio1.data.local.CarContract.carEntry.COLUMN_NAME_BATERIA
import com.example.dio1.data.local.CarContract.carEntry.COLUMN_NAME_POTENCIA
import com.example.dio1.data.local.CarContract.carEntry.COLUMN_NAME_PRECO
import com.example.dio1.data.local.CarContract.carEntry.COLUMN_NAME_RECARGA
import com.example.dio1.data.local.CarContract.carEntry.COLUMN_NAME_URL_PHOTO
import com.example.dio1.data.local.CarContract.carEntry.TABLE_NAME
import com.example.dio1.data.local.CarRepository
import com.example.dio1.data.local.CarsDbHelper
import com.example.dio1.ui.adapter.CarAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class CarFragment : Fragment() {

    lateinit var fabCalcular : FloatingActionButton;
    lateinit var listaCarros : RecyclerView;
    lateinit var progress : ProgressBar;
    lateinit var noInternetImage : ImageView;
    lateinit var noInternetText : TextView;
    lateinit var carsApi : CarsApi;

    var carrosArray : ArrayList<Carro> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_fragment, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRetrofit();
        setupView(view);
        setupListeners();
    }

    override fun onResume() {
        super.onResume();
        if (checkForInternet(context)){
           // callService(); Essa é outra forma de chamar serviços
            getAllCars();
        }
        else{
            emptyState()
        }
    }

    fun setupRetrofit(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://igorbag.github.io/cars-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        carsApi = retrofit.create(CarsApi::class.java)
    }

    fun getAllCars(){
        carsApi.getAllcars().enqueue(object : Callback<List<Carro>>{
            override fun onResponse(call: Call<List<Carro>>, response: Response<List<Carro>>) {
                if (response.isSuccessful){
                    progress.isVisible = false
                    noInternetImage.isVisible = false;
                    noInternetText.isVisible = false;
                    response.body()?.let {
                        setupList(it);
                    }
                }else{
                    Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Carro>>, t: Throwable) {
                Toast.makeText(context, R.string.response_error, Toast.LENGTH_LONG).show()
            }

        })
    }

    fun emptyState(){
        progress.isVisible = false;
        listaCarros.isVisible = false;
        noInternetImage.isVisible = true;
        noInternetText.isVisible = true;
    }

    fun setupView(view: View) {
        fabCalcular = view.findViewById(R.id.fab_calcular);
        listaCarros = view.findViewById(R.id.rv_listaCarros);
        progress = view.findViewById(R.id.pb_loader);
        noInternetImage = view.findViewById(R.id.iv_empty_state);
        noInternetText = view.findViewById(R.id.tv_no_wifi);
    }

    fun setupListeners() {
        fabCalcular.setOnClickListener {
            startActivity(Intent(context, CalcularAutonomiaActivity::class.java))
        }
    }

    fun callService(){
        val urlBase = "https://igorbag.github.io/cars-api/cars.json"
        MyTask().execute(urlBase);
        progress.isVisible = true;
    }

    fun setupList(lista : List<Carro>){
        listaCarros.isVisible = true;

        val adapter = CarAdapter(lista);
        listaCarros.layoutManager = LinearLayoutManager(requireContext());
        listaCarros.adapter = adapter;
        adapter.carItemListener = {carro ->
            val isSaved = CarRepository(requireContext()).save(carro)
        }
    }

    fun checkForInternet(context : Context?) : Boolean{
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network = connectivityManager.activeNetwork ?: return false

            val activeNetWork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetWork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetWork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }else{
            @Suppress("DPRECATION")
            val netWorkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DPRECATION")
            return netWorkInfo.isConnected
        }
    }

    // Usar o Retrofit como abstração do AsynTask!!!
    inner class MyTask : AsyncTask<String, String, String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            Log.d("MyTask", "Iniciando...")
        }

            override fun doInBackground(vararg url: String?): String {
                var urlConnection: HttpURLConnection? = null;

                try {
                    val urlBase = URL(url[0]);

                    urlConnection = urlBase.openConnection() as HttpURLConnection
                    urlConnection.connectTimeout = 60000;
                    urlConnection.readTimeout = 60000;
                    urlConnection.setRequestProperty(
                        "Accept",
                        "application/json"
                    );

                    val responseCode = urlConnection.responseCode

                    if(responseCode == HttpURLConnection.HTTP_OK){
                        var response = urlConnection.inputStream.bufferedReader().use { it.readText() }
                        publishProgress(response)
                    }else{
                        Log.e("Erro", "Serviço indisponível..", )
                    }

                } catch (ex: Exception) {
                    Log.e("Erro", "Erro ao realizar o processamento...", ex)
                } finally {
                    urlConnection?.disconnect();
                }
                return " ";
            }


            override fun onProgressUpdate(vararg values: String?) {
                try {

                    val jsonArray = JSONTokener(values[0]).nextValue() as JSONArray

                    for (i in 0 until jsonArray.length()) {
                        val id = jsonArray.getJSONObject(i).getString("id")
                        Log.d("Id ->", id)

                        val preco = jsonArray.getJSONObject(i).getString("preco")
                        Log.d("Preço ->", preco)

                        val bateria = jsonArray.getJSONObject(i).getString("bateria")
                        Log.d("Bateria->", bateria)

                        val potencia = jsonArray.getJSONObject(i).getString("potencia")
                        Log.d("Potencia->", potencia)

                        val recarga = jsonArray.getJSONObject(i).getString("recarga")
                        Log.d("Recarga->", recarga)

                        val urlPhoto = jsonArray.getJSONObject(i).getString("urlPhoto")
                        Log.d("urlPhoto->", urlPhoto)

                        val model = Carro(
                            id = id.toInt(),
                            preco = preco,
                            bateria = bateria,
                            potencia = potencia,
                            recarga = recarga,
                            urlPhoto = urlPhoto,
                            isFavorite = false
                        )
                        carrosArray.add(model);
                    }
                    progress.isVisible = false;
                    noInternetImage.isVisible = false;
                    noInternetText.isVisible = false;
                    //setupList();
                } catch (ex: Exception) {
                    Log.e("Erro ->", ex.message.toString());
                }
                fun streamToString(inputStream: InputStream): String {
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                    var line: String
                    var result = ""

                    try {
                        do {
                            line = bufferedReader.readLine()
                            line?.let {
                                result += line
                            }
                        } while (true)
                    } catch (ex: Exception) {
                        Log.e("Erro", "Erro ao parcelar Stream")
                    }
                    return result
                }
            }
    }
}