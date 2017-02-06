package com.ackee.versioupdatehandler.model;

/**
 * Basic class for versions configuration.
 *
 * @author Georgiy Shur (georgiy.shur@ackee.cz)
 * @since 2/5/2017
 */
public class BasicVersionsConfiguration implements VersionsConfiguration {
    public static final String TAG = BasicVersionsConfiguration.class.getName();

    long minimalVersion;
    long currentVersion;

    public BasicVersionsConfiguration(long minimalVersion, long currentVersion) {
        this.minimalVersion = minimalVersion;
        this.currentVersion = currentVersion;
    }

    @Override
    public long minimalVersion() {
        return minimalVersion;
    }

    @Override
    public long currentVersion() {
        return currentVersion;
    }
}
