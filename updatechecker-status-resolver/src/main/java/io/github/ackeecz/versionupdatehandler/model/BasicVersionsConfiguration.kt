package io.github.ackeecz.versionupdatehandler.model

/**
 * Basic class for versions configuration.
 */
open class BasicVersionsConfiguration(
    private val minimalVersion: Long,
    private val currentVersion: Long
) : VersionsConfiguration {

    override fun minimalVersion(): Long {
        return minimalVersion
    }

    override fun currentVersion(): Long {
        return currentVersion
    }
}

object DefaultVersionsConfiguration : BasicVersionsConfiguration(-1, -1)
