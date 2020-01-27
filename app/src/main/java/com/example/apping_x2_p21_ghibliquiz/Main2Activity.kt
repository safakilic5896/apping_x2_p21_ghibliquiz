package com.example.apping_x2_p21_ghibliquiz

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        //the base URL where the webservice is located
        val baseURL = "https://ghibliapi.herokuapp.com/"

        // Use GSON library to create our JSON parser
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())

        //Open a URL/website
        val implicitIntent = Intent(Intent.ACTION_VIEW)

        // Create a Retrofit client object targeting the provided URL
        // and add a JSON converter (because we are expecting json responses)
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()

        val service: WebServiceInterface = retrofit.create(WebServiceInterface::class.java)

        val responseStatus = intent.getSerializableExtra("RESPONSE_STATUS") as? ResponseStatus

        if (responseStatus != null) {
            director.text= responseStatus.infoFilm[Info.DIRECTOR.value]
            year.text = responseStatus.infoFilm[Info.YEAR.value]
            titleMovie.text = responseStatus.infoFilm[Info.TITLEMOVIE.value]
            synopsis.text = responseStatus.infoFilm[Info.SYNOPSIS.value]
            character.text = responseStatus.infoFilm[Info.CHARACTER.value]

            val uri: Uri = Uri.parse("http://www.google.com/#q=" + responseStatus.infoFilm[Info.TITLEMOVIE.value])
            implicitIntent.data = uri

            if (!responseStatus.isGoodResponse) {
                response.text = "WRONG !"
                response.setTextColor(Color.RED)

                val wsCallback: Callback<FilmItem> = object : Callback<FilmItem> {
                    override fun onFailure(call: Call<FilmItem>, t: Throwable) {
                        Log.d("Info", "Error When the Api was Call")
                    }

                    override fun onResponse(call: Call<FilmItem>, response: Response<FilmItem>) {
                        if (response.code() == 200) {
                            character_plays.text = "This character can be seen in '" + response.body()!!.title + "'"
                        }
                    }

                }

                service.filmItem(responseStatus.filmUrl).enqueue(wsCallback)

            } else {
                response.text = "RIGHT !"
                response.setTextColor(Color.GREEN)

            }
            button.setOnClickListener {
                startActivity(implicitIntent)
            }
        }
    }
}
