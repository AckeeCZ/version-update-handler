package com.ackee.versioupdatehandler

import com.ackee.versioupdatehandler.model.VersionsConfiguration
import rx.Single

/**
 * Base interface for classes that will handle version fetching.
 *
 * @author Georgiy Shur (georgiy.shur@ackee.cz)
 * @since 2/5/2017
 */
interface VersionFetcher {
    fun fetch(): Single<VersionsConfiguration>
}