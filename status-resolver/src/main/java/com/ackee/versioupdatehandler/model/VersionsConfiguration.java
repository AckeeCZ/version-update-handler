package com.ackee.versioupdatehandler.model;

/**
 * Base interface for holding the versions configuration data.
 */
public interface VersionsConfiguration {
    long minimalVersion();

    long currentVersion();
}
