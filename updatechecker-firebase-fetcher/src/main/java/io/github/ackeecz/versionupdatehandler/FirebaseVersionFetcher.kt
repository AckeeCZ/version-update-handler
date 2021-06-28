package io.github.ackeecz.versionupdatehandler

import io.github.ackeecz.versionupdatehandler.model.BasicVersionsConfiguration
import io.github.ackeecz.versionupdatehandler.model.VersionsConfiguration
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
    }

    override suspend fun fetch(): VersionsConfiguration {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.fetch(cacheExpiration.toLong()).await()
        remoteConfig.activate().await()

        val minimalVersion = remoteConfig.getLong(minimalAttributeName)
        val currentVersion = remoteConfig.getLong(currentAttributeName)
        return BasicVersionsConfiguration(minimalVersion, currentVersion)
    }
}
