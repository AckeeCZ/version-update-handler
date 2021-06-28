package io.github.ackeecz.versionupdatehandler

import io.github.ackeecz.versionupdatehandler.model.VersionsConfiguration

/**
 * Base interface for classes that will handle version fetching.
 */
interface VersionFetcher {

    suspend fun fetch(): VersionsConfiguration
}
