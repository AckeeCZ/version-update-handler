package com.ackee.versioupdatehandler.model

/**
 * Basic class for versions configuration.
 *
 * @author Georgiy Shur (georgiy.shur@ackee.cz)
 * @since 2/5/2017
 */
data class BasicVersionsConfiguration(val minimalVersion: Long,
                                      val currentVersion: Long) : VersionsConfiguration {
    override fun minimalVersion(): Long {
        return minimalVersion
    }

    override fun currentVersion(): Long {
        return currentVersion
    }
}