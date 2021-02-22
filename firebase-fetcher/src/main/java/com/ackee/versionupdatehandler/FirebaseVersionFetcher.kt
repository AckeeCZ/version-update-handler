package com.ackee.versionupdatehandler

import com.ackee.versioupdatehandler.VersionFetcher
import com.ackee.versioupdatehandler.model.BasicVersionsConfiguration
import com.ackee.versioupdatehandler.model.VersionsConfiguration
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await

/**
 * Class that fetches version configuration from Firebase Remote.
 */
class FirebaseVersionFetcher(
    private val cacheExpiration: Int = 3600,
    private val minimalAttributeName: String = MINIMAL_VERSION,
    private val currentAttributeName: String = CURRENT_VERSION
) : VersionFetcher {

    companion object {
        const val MINIMAL_VERSION = "minimal_version_android"
        const val CURRENT_VERSION = "current_version_android"

        val DEFAULTS: Map<String, Any> by lazy {
            mapOf(
                MINIMAL_VERSION to -1,
                CURRENT_VERSION to -1
            )
        }
    }

    override suspend fun fetch(): VersionsConfiguration {
        val remoteConfig = FirebaseRemoteConfig.getInstance().apply {
            setDefaultsAsync(DEFAULTS)
        }
        remoteConfig.fetch(cacheExpiration.toLong()).await()
        remoteConfig.activate().await()

        val minimalVersion = remoteConfig.getLong(minimalAttributeName)
        val currentVersion = remoteConfig.getLong(currentAttributeName)
        return BasicVersionsConfiguration(minimalVersion, currentVersion)
    }
}