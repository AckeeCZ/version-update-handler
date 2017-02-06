package com.ackee.versioupdatehandler.model

/**
 * Base interface for holding the versions configuration data.
 *
 * @author Georgiy Shur (georgiy.shur@ackee.cz)
 * @since 2/5/2017
 */
interface VersionsConfiguration {
    fun minimalVersion(): Long
    fun currentVersion(): Long
}