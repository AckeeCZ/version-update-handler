package com.ackee.versioupdatehandler;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

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

    public void checkVersionStatusAndOpenDefault(final int version, final FragmentManager fragmentManager) {
        checkVersionStatus(version)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<VersionStatus>() {
                    @Override
                    public void call(VersionStatus versionStatus) {
                        if (versionStatus != VersionStatus.UP_TO_DATE) {
                            showDialog(versionStatus == VersionStatus.UPDATE_REQUIRED, fragmentManager);
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
    private void showDialog(boolean forceUpdate, FragmentManager fragmentManager) {
        UpdateDialog.newInstance(forceUpdate).show(fragmentManager, UpdateDialog.class.getName());
    }

    public static class UpdateDialog extends DialogFragment {

        private static final String FORCE_UPDATE_KEY = "force_update";

        public static UpdateDialog newInstance(boolean forceUpdate) {
            Bundle args = new Bundle();
            args.putBoolean(FORCE_UPDATE_KEY, forceUpdate);
            UpdateDialog updateDialog = new UpdateDialog();
            updateDialog.setArguments(args);
            return updateDialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Update app");
            builder.setMessage("Your application is outdated. Please update to the newest version");
            final boolean isForceUpdate = getArguments().getBoolean(FORCE_UPDATE_KEY, false);
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (isForceUpdate) {
                        getActivity().finish();
                    }
                    final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (isForceUpdate) {
                        getActivity().moveTaskToBack(true);
                    }
                }
            });
            return builder.create();
        }
    }

}


