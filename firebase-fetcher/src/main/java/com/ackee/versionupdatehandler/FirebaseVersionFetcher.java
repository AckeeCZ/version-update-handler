package com.ackee.versionupdatehandler;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ackee.versioupdatehandler.VersionFetcher;
import com.ackee.versioupdatehandler.model.BasicVersionsConfiguration;
import com.ackee.versioupdatehandler.model.VersionFetchError;
import com.ackee.versioupdatehandler.model.VersionsConfiguration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import rx.Single;
import rx.SingleSubscriber;

/**
 * Class that fetches version configuration from Firebase Remote.
 *
 * @author Georgiy Shur (georgiy.shur@ackee.cz)
 * @since 2/5/2017
 */
public class FirebaseVersionFetcher implements VersionFetcher {
    public static final String TAG = FirebaseVersionFetcher.class.getName();

    public static final String MINIMAL_VERSION = "minimal_version_android";
    public static final String CURRENT_VERSION = "current_version_android";

    private int cacheExpiration;
    String minimalAttributeName;
    String currentAttributeName;

    public FirebaseVersionFetcher() {
        this(3600);
    }

    public FirebaseVersionFetcher(int cacheExpiration) {
        this(cacheExpiration, MINIMAL_VERSION, CURRENT_VERSION);
    }

    public FirebaseVersionFetcher(int cacheExpiration, String minimalAttributeName, String currentAttributeName) {
        this.cacheExpiration = cacheExpiration;
        this.minimalAttributeName = minimalAttributeName;
        this.currentAttributeName = currentAttributeName;
    }

    @Override
    public Single<VersionsConfiguration> fetch() {
        return Single.create(new Single.OnSubscribe<VersionsConfiguration>() {
            @Override
            public void call(final SingleSubscriber<? super VersionsConfiguration> singleSubscriber) {
                boolean isDevMode = FirebaseRemoteConfig.getInstance().getInfo().getConfigSettings().isDeveloperModeEnabled();

                FirebaseRemoteConfig.getInstance().fetch(isDevMode ? 0 : cacheExpiration).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseVersionFetcher", "onComplete: success");
                            FirebaseRemoteConfig.getInstance().activateFetched();
                        } else {
                            Log.d("FirebaseVersionFetcher", "onComplete: failed");
                            singleSubscriber.onError(new VersionFetchError());
                        }
                        long minimalVersion = FirebaseRemoteConfig.getInstance().getLong(minimalAttributeName);
                        long currentVersion = FirebaseRemoteConfig.getInstance().getLong(currentAttributeName);

                        singleSubscriber.onSuccess(new BasicVersionsConfiguration(minimalVersion, currentVersion));
                    }
                });
            }
        });
    }
}
