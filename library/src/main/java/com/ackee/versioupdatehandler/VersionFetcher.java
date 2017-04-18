package com.ackee.versioupdatehandler;

import com.ackee.versioupdatehandler.model.VersionsConfiguration;

import io.reactivex.Single;


/**
 * Base interface for classes that will handle version fetching.
 *
 * @author Georgiy Shur (georgiy.shur@ackee.cz)
 * @since 2/5/2017
 */
public interface VersionFetcher {
    public static final String TAG = VersionFetcher.class.getName();

    Single<VersionsConfiguration> fetch();

}
