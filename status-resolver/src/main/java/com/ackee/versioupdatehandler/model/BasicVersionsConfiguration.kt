package com.ackee.versioupdatehandler.model

/**
 * Basic class for versions configuration.
 */
open class BasicVersionsConfiguration(
    private val minimalVersion: Long,
    private val currentVersion: Long
) : VersionsConfiguration {

    companion object {
        val TAG = BasicVersionsConfiguration::class.java.name
    }

    override fun minimalVersion(): Long {
        return minimalVersion
    }

    override fun currentVersion(): Long {
        return currentVersion
    }
}

object DefaultVersionsConfiguration : BasicVersionsConfiguration(-1, -1)