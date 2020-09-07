package com.example.kotlincollection

import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType

object Api {
    const val url = "https://raw.githubusercontent.com/VityaJulin/Kotlin_collections/master/app/src/main/java/com/example/firstapplication/posts.json"

    val client = HttpClient {
        install(JsonFeature) {
            acceptContentTypes = listOf(
                ContentType.Text.Plain,
                ContentType.Application.Json
            )
            serializer = GsonSerializer()
        }
    }
}