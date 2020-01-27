package com.example.apping_x2_p21_ghibliquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    //Generate a list with random value
    private fun arrayWithUniqueValue(list: IntArray) {
        val l = (list.indices).toList()
        Collections.shuffle(l)
        for (i in 0..5) {
            list[i] = l[i]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Intent for secondActivity
        val explicitIntent = Intent(this,Main2Activity::class.java)

        //data to store people
        var peopleList: List<PeopleItem> = arrayListOf()

        //List to store people which will be displayed
        var peopleListToDisplay: MutableList<PeopleItem> = mutableListOf<PeopleItem>()

        val itemPeople =  IntArray(20)
        // generate a list with random value
        arrayWithUniqueValue(itemPeople)

        //random right response
        var goodResponse: Int = (0..4).random()
        Log.d("Info", goodResponse.toString())

        val infoFilm: MutableList<String> = mutableListOf<String>()

        //the base URL where the webservice is located
        val baseURL = "https://ghibliapi.herokuapp.com/"

        // Use GSON library to create our JSON parser
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())

        // Create a Retrofit client object targeting the provided URL
        // and add a JSON converter (because we are expecting json responses)
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()


        // Use the client to create a service:
        // an object implementing the interface to the WebService
        // First request
        val service: WebServiceInterface = retrofit.create(WebServiceInterface::class.java)
        val wsCallback: Callback<List<PeopleItem>> = object : Callback<List<PeopleItem>> {
            override fun onFailure(call: Call<List<PeopleItem>>, t: Throwable) {
                Log.d("Info", "Error When the Api was Call")
            }

            override fun onResponse(
                call: Call<List<PeopleItem>>,
                response: Response<List<PeopleItem>>
            ) {
                if (response.code() == 200) {
                    peopleList = response.body()!!
                    for (i in 0..5)
                        peopleListToDisplay.add(peopleList[itemPeople[i]])

                    val wsCallbackFilmItem: Callback<FilmItem> = object : Callback<FilmItem> {
                        override fun onFailure(call: Call<FilmItem>, t: Throwable) {
                            Log.d("Info", "Error When the Api was Call")
                        }

                        override fun onResponse(call: Call<FilmItem>, response: Response<FilmItem>) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    movie_name.text = "'" + response.body()!!.title + "' " + "?"
                                    infoFilm.add(response.body()!!.director)
                                    infoFilm.add(response.body()!!.release_date.toString())
                                    infoFilm.add(response.body()!!.title)
                                    infoFilm.add(response.body()!!.description)
                                    infoFilm.add(peopleListToDisplay[goodResponse].name)
                                }
                            }
                        }
                    }

                    //Second Request
                    service.filmItem(peopleListToDisplay[goodResponse].films[0]).enqueue(wsCallbackFilmItem)


                    val onItemClickListener : View.OnClickListener = View.OnClickListener { rowView ->
                        if (rowView.tag != goodResponse)
                        {
                            explicitIntent.putExtra("RESPONSE_STATUS", ResponseStatus(false,
                                peopleListToDisplay[rowView.tag as Int].films[0], infoFilm))
                            startActivity(explicitIntent)
                        }
                        else {
                            explicitIntent.putExtra("RESPONSE_STATUS", ResponseStatus(true,
                                peopleListToDisplay[rowView.tag as Int].films[0], infoFilm))
                            startActivity(explicitIntent)
                        }
                    }
                    list_people.setHasFixedSize(true)
                    list_people.layoutManager = LinearLayoutManager(this@MainActivity)
                    list_people.adapter = PeopleAdapter(peopleListToDisplay, this@MainActivity, onItemClickListener)
                    list_people.addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))

                }
            }

        }
        // Finally, use the service to enqueue the callback
        // This will asynchronously call the method
        service.listAllPeople().enqueue(wsCallback)
    }
}
