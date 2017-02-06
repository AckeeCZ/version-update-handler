package com.ackee.versioupdatehandler

import android.util.Log
import com.ackee.versioupdatehandler.model.VersionStatus
import com.ackee.versioupdatehandler.model.VersionsConfiguration
import rx.Single

/**
 * Class that contains the version comparing and update handling logic.
 *
 * @author Georgiy Shur (georgiy.shur@ackee.cz)
 * @since 2/5/2017
 */
class VersionStatusResolver(val fetcher: VersionFetcher) {

    fun checkVersionStatus(version: Int): Single<VersionStatus> {
        return fetcher.fetch()
                .map { resolveStatus(version, it) }

    }

    private fun resolveStatus(version: Int, configuration: VersionsConfiguration): VersionStatus {
        val status = if (version < configuration.minimalVersion()) {
            VersionStatus.UPDATE_REQUIRED
        } else if (version < configuration.currentVersion()) {
            VersionStatus.UPDATE_AVAILABLE
        } else {
            VersionStatus.UP_TO_DATE
        }
        Log.d("VersionStatusResolver", "Resolving version status... App version: $version, " +
                "minimal version: ${configuration.minimalVersion()}, " +
                "current version: ${configuration.minimalVersion()}. Status resolved: $status")
        return status
    }
}