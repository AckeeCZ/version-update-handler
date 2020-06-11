package com.ackee.versioupdatehandler

import com.ackee.versioupdatehandler.model.VersionsConfiguration

/**
 * Base interface for classes that will handle version fetching.
 */
interface VersionFetcher {

    suspend fun fetch(): VersionsConfiguration
}