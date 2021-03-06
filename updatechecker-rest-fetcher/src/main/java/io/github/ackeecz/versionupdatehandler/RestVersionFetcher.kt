package io.github.ackeecz.versionupdatehandler

import io.github.ackeecz.versionupdatehandler.model.BasicVersionsConfiguration
import io.github.ackeecz.versionupdatehandler.model.DefaultVersionsConfiguration
import io.github.ackeecz.versionupdatehandler.model.VersionsConfiguration
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Class that fetches version configuration from rest api
 *
 * Api is specified by base url and is expected to be at address /app_version
 */
class RestVersionFetcher(
    private val baseUrl: String,
    private val currentAttributeName: String = CURRENT_VERSION,
    private val minimalAttributeName: String = MINIMAL_VERSION
) : VersionFetcher {

    companion object {
        const val MINIMAL_VERSION = "minimal_version_android"
        const val CURRENT_VERSION = "current_version_android"
    }

    internal interface ApiDescription {
        @GET("app_version")
        suspend fun versions(): JsonObject?
    }

    private val api: ApiDescription by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()))
            .client(OkHttpClient.Builder().build())
            .build()
            .create(ApiDescription::class.java)
    }

    override suspend fun fetch(): VersionsConfiguration {
        return api.versions()?.map() ?: DefaultVersionsConfiguration
    }

    private fun JsonObject.map(): BasicVersionsConfiguration {
        return with(this) {
            val minimalVersion = if (has(minimalAttributeName)) get(minimalAttributeName).asLong else -1
            val currentVersion = if (has(currentAttributeName)) get(currentAttributeName).asLong else -1
            BasicVersionsConfiguration(minimalVersion, currentVersion)
        }
    }
}

