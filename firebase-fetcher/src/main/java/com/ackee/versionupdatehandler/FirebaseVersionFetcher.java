package com.ackee.versionupdatehandler;

import com.ackee.versioupdatehandler.VersionFetcher;
import com.ackee.versioupdatehandler.model.BasicVersionsConfiguration;
import com.ackee.versioupdatehandler.model.VersionFetchError;
import com.ackee.versioupdatehandler.model.VersionsConfiguration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import androidx.annotation.NonNull;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Class that fetches version configuration from Firebase Remote.
 */
public class FirebaseVersionFetcher implements VersionFetcher {

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
        return Single.create(new SingleOnSubscribe<VersionsConfiguration>() {
            @Override
            public void subscribe(final SingleEmitter<VersionsConfiguration> emitter) throws Exception {
                boolean isDevMode = FirebaseRemoteConfig.getInstance().getInfo().getConfigSettings().isDeveloperModeEnabled();

                FirebaseRemoteConfig.getInstance().fetch(isDevMode ? 0 : cacheExpiration).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseRemoteConfig.getInstance().activateFetched();
                        } else {
                            if (!emitter.isDisposed()) {
                                emitter.onError(new VersionFetchError());
                            }
                        }
                        long minimalVersion = FirebaseRemoteConfig.getInstance().getLong(minimalAttributeName);
                        long currentVersion = FirebaseRemoteConfig.getInstance().getLong(currentAttributeName);
                        if (!emitter.isDisposed()) {
                            emitter.onSuccess(new BasicVersionsConfiguration(minimalVersion, currentVersion));
                        }
                    }
                });
            }
        });
    }
}
