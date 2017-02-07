package com.ackee.versioupdatehandler;

import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.ackee.versioupdatehandler.model.DialogSettings;
import com.ackee.versioupdatehandler.model.VersionStatus;
import com.ackee.versioupdatehandler.model.VersionsConfiguration;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


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

    /**
     * Check for version and show dialog with default settings
     *
     * @param version         current version
     * @param fragmentManager fragment manager used for showing dialog fragment
     */
    public void checkVersionStatusAndOpenDefault(final int version, final FragmentManager fragmentManager) {
        checkVersionStatusAndOpenDefault(version, fragmentManager, new DialogSettings.Builder().build());
    }

    /**
     * Check for version and show dialog with customized visual settings via {@link DialogSettings}
     *
     * @param version         current version
     * @param fragmentManager fragment manager used for showing dialog fragment
     */
    public void checkVersionStatusAndOpenDefault(final int version, final FragmentManager fragmentManager, final DialogSettings settings) {
        checkVersionStatus(version)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<VersionStatus>() {
                    @Override
                    public void call(VersionStatus versionStatus) {
                        if (versionStatus != VersionStatus.UP_TO_DATE) {
                            showDialog(versionStatus == VersionStatus.UPDATE_REQUIRED, fragmentManager, settings);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * Show dialog with update info
     *
     * @param forceUpdate indicator if update is mandatory
     */
    private void showDialog(boolean forceUpdate, FragmentManager fragmentManager, DialogSettings dialogSettings) {
        UpdateDialog.newInstance(forceUpdate, dialogSettings).show(fragmentManager, UpdateDialog.class.getName());
    }

}


