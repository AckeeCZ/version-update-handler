package com.ackee.versionupdatehandler

import android.util.Log
import com.ackee.versioupdatehandler.VersionFetcher
import com.ackee.versioupdatehandler.model.BasicVersionsConfiguration
import com.ackee.versioupdatehandler.model.VersionFetchError
import com.ackee.versioupdatehandler.model.VersionStatus
import com.ackee.versioupdatehandler.model.VersionsConfiguration
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rx.Single

/**
 * Class that fetches version configuration from Firebase Remote.
 *
 * @author Georgiy Shur (georgiy.shur@ackee.cz)
 * @since 2/5/2017
 */
class FirebaseVersionFetcher(val cacheExpiration: Long = 3600): VersionFetcher {

    companion object {
        const val MINIMAL_VERSION = "minimal_version_android"
        const val CURRENT_VERSION = "current_version_android"
    }

    override fun fetch(): Single<VersionsConfiguration> {
        return Single.create({
            val isDevMode = FirebaseRemoteConfig.getInstance().info.configSettings.isDeveloperModeEnabled

            FirebaseRemoteConfig.getInstance().fetch(if (isDevMode) 0 else cacheExpiration).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseVersionFetcher", "onComplete: success")
                    FirebaseRemoteConfig.getInstance().activateFetched()
                } else {
                    Log.d("FirebaseVersionFetcher", "onComplete: failed")
                    it.onError(VersionFetchError())
                }
                val minimalVersion = FirebaseRemoteConfig.getInstance().getLong(MINIMAL_VERSION)
                val currentVersion = FirebaseRemoteConfig.getInstance().getLong(CURRENT_VERSION)

                it.onSuccess(BasicVersionsConfiguration(minimalVersion, currentVersion))
            }
        })
    }
}