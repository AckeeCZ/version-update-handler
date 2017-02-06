package com.ackee.versioupdatehandler;

import android.util.Log;

import com.ackee.versioupdatehandler.model.VersionStatus;
import com.ackee.versioupdatehandler.model.VersionsConfiguration;

import rx.Single;
import rx.functions.Func1;


/**
 * Class that contains the version comparing and update handling logic.
 *
 * @author Georgiy Shur (georgiy.shur@ackee.cz)
 * @since 2/5/2017
 */
public class VersionStatusResolver {
    public static final String TAG = VersionStatusResolver.class.getName();
    VersionFetcher fetcher;

    public VersionStatusResolver(VersionFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public Single<VersionStatus> checkVersionStatus(final int version) {
        return fetcher.fetch()
                .map(new Func1<VersionsConfiguration, VersionStatus>() {
                    @Override
                    public VersionStatus call(VersionsConfiguration versionsConfiguration) {
                        return resolveStatus(version, versionsConfiguration);
                    }
                });

    }

    private VersionStatus resolveStatus(int version, VersionsConfiguration configuration) {
        VersionStatus status = VersionStatus.UP_TO_DATE;
        if (version < configuration.minimalVersion()) {
            status = VersionStatus.UPDATE_REQUIRED;
        } else if (version < configuration.currentVersion()) {
            status = VersionStatus.UPDATE_AVAILABLE;
        }
        Log.d("VersionStatusResolver", "Resolving version status... App version:" + version +
                " minimal version: " + configuration.minimalVersion() +
                " current version: " + configuration.minimalVersion() + " Status resolved:  " + status);
        return status;
    }
}
