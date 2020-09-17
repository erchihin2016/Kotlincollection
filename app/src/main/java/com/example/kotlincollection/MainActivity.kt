package com.example.kotlincollection

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import io.ktor.client.request.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import net.danlew.android.joda.JodaTimeAndroid
import java.io.File

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        JodaTimeAndroid.init(this)

        val gson = Gson()
        var jsonString: String = gson.toJson(posts)

        fetchData()
    }

    private fun fetchData(): Job = launch {
        try {
            val list = withContext(Dispatchers.IO) {
                Api.client.get<MutableList<PostCard>>(Api.url)
            }
            with(recycle_main) {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = Adapter(list)
            }
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Нет интернета", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    var posts = mutableListOf<PostCard>(
        PostCard(
            username = "Username1",
            post = "First post in our network",
            postType = PostType.EVENT
        ),
        PostCard(
            username = "Username2",
            post = "Second post in our network",
            postType = PostType.YOUTUBE_VIDEO
        ),
        PostCard(username = "Username3", post = "Third post in our network")
    )

}
