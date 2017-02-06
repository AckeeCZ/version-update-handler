package com.ackee.versioupdatehandler;


import com.ackee.versioupdatehandler.model.BasicVersionsConfiguration;
import com.ackee.versioupdatehandler.model.VersionFetchError;
import com.ackee.versioupdatehandler.model.VersionStatus;
import com.ackee.versioupdatehandler.model.VersionsConfiguration;

import org.junit.Test;

import rx.Single;
import rx.functions.Func1;
import rx.observers.TestSubscriber;

/**
 * Tests for {@link VersionStatusResolver}
 *
 * @author David Bilik [david.bilik@ackee.cz]
 * @since 06/02/2017
 **/
public class VersionStatusResolverTest {

    public static final BasicVersionsConfiguration BASIC_VERSIONS_CONFIGURATION = new BasicVersionsConfiguration(10, 15);

    @Test
    public void should_fire_error() {
        //any error should map to VersionFetchError
        VersionStatusResolver resolver = new VersionStatusResolver(new VersionFetcher() {
            @Override
            public Single<VersionsConfiguration> fetch() {
                return Single.error(new VersionFetchError());
            }
        });
        TestSubscriber<VersionStatus> testSubscriber = TestSubscriber.create();
        resolver.checkVersionStatus(0)
                .subscribe(testSubscriber);
        testSubscriber.assertError(VersionFetchError.class);
        testSubscriber.assertNoValues();
        testSubscriber.assertTerminalEvent();
    }

    @Test
    public void should_be_up_to_date_greater_than_last() {
        checkSuccess(15, VersionStatus.UP_TO_DATE);
    }

    @Test
    public void should_be_up_to_date_same_as_last() {
        checkSuccess(20, VersionStatus.UP_TO_DATE);
    }

    @Test
    public void should_be_mandatory_update() {
        checkSuccess(8, VersionStatus.UPDATE_REQUIRED);
    }

    @Test
    public void should_be_not_mandatory_update() {
        checkSuccess(13, VersionStatus.UPDATE_AVAILABLE);
    }

    private void checkSuccess(int actualVersion, VersionStatus expectedResult) {
        VersionStatusResolver resolver = new VersionStatusResolver(new VersionFetcher() {
            @Override
            public Single<VersionsConfiguration> fetch() {
                return Single.just(BASIC_VERSIONS_CONFIGURATION).map(new Func1<BasicVersionsConfiguration, VersionsConfiguration>() {
                    @Override
                    public VersionsConfiguration call(BasicVersionsConfiguration basicVersionsConfiguration) {
                        return basicVersionsConfiguration;
                    }
                });
            }
        });
        TestSubscriber<VersionStatus> testSubscriber = TestSubscriber.create();
        resolver.checkVersionStatus(actualVersion)
                .subscribe(testSubscriber);
        testSubscriber.assertValue(expectedResult);
        testSubscriber.assertNoErrors();
    }
}