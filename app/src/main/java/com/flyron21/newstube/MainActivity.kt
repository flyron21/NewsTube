package com.flyron21.newstube

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchDate()
//        val items = fetchDate()
        mAdapter = NewsListAdapter(this)
//        val adapter = NewsListAdapter(items,this)
//        recyclerView.adapter = adapter
        recyclerView.adapter = mAdapter


        val fabRefresh = findViewById<FloatingActionButton>(R.id.fab_refresh)
        fabRefresh.setOnClickListener {
            // Restart or reload the app's content
            recreate() // This method recreates the current activity
        }

    }
    //        private fun fetchDate(): ArrayList<String> {
//        val list = ArrayList<String>()
//        for(i in 0 until 100) {
//            list.add("Item $i")
//        }
//        return list
//    }
    private fun fetchDate() {
        //volley library
        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=202a349d7cc04694a6e38af1b334442d"
        //making a request
        val jsonObjectRequest = object: JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {

                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    println(news.title + "AZAD")
                    newsArray.add(news)
                }

                mAdapter.updateNews(newsArray)
            },
            {
                Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_LONG).show()
            }

        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }




    override fun onItemClicked(item: News) {
        Toast.makeText(this,"Opening full article.",Toast.LENGTH_SHORT).show()
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }


}