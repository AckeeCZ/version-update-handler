package com.ackee.versioupdatehandler.model

/**
 * Base interface for holding the versions configuration data.
 */
interface VersionsConfiguration {

    fun minimalVersion(): Long
    fun currentVersion(): Long
}