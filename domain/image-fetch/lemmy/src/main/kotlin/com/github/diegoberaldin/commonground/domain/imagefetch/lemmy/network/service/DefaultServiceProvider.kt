package com.github.diegoberaldin.commonground.domain.imagefetch.lemmy.network.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit

@Single
internal class DefaultServiceProvider : ServiceProvider {

    companion object {
        private const val DEFAULT_INSTANCE = "lemmy.world"
        private const val VERSION = "v3"
        private const val ENABLE_LOGGING = false
    }

    override var currentInstance: String = DEFAULT_INSTANCE
        private set

    override lateinit var post: PostService
        private set

    private val baseUrl: String get() = "https://$currentInstance/api/$VERSION/"

    init {
        reinitialize()
    }

    override fun changeInstance(value: String) {
        if (currentInstance != value) {
            currentInstance = value
            reinitialize()
        }
    }

    private fun reinitialize() {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .apply {
                if (ENABLE_LOGGING) {
                    addInterceptor(interceptor)
                }
            }
            .build()
        val json = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
        post = retrofit.create()
    }
}