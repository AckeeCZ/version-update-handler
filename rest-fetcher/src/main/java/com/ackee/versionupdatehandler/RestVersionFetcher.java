package com.ackee.versionupdatehandler;

import com.ackee.versioupdatehandler.VersionFetcher;
import com.ackee.versioupdatehandler.model.BasicVersionsConfiguration;
import com.ackee.versioupdatehandler.model.VersionFetchError;
import com.ackee.versioupdatehandler.model.VersionsConfiguration;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Single;
import rx.functions.Func1;

/**
 * Class that fetches version configuration from rest api
 * <p>
 * Api is specified by base url and is expected to be at address /app_version
 *
 * @author David Bilik [david.bilik@ackee.cz]
 * @since 06/02/2017
 **/
public class RestVersionFetcher implements VersionFetcher {
    public static final String TAG = RestVersionFetcher.class.getName();

    interface ApiDescription {
        @GET("app_version")
        public Single<JsonObject> getVersions();
    }

    public static final String MINIMAL_VERSION = "minimal_version_android";
    public static final String CURRENT_VERSION = "current_version_android";

    String minimalAttributeName;
    String currentAttributeName;
    String baseUrl;

    private ApiDescription api = null;

    public RestVersionFetcher(String baseUrl) {
        this(baseUrl, MINIMAL_VERSION, CURRENT_VERSION);
    }

    public RestVersionFetcher(String baseUrl, String currentAttributeName, String minimalAttributeName) {
        this.baseUrl = baseUrl;
        this.currentAttributeName = currentAttributeName;
        this.minimalAttributeName = minimalAttributeName;
    }

    @Override
    public Single<VersionsConfiguration> fetch() {
        return getApi()
                .getVersions()
                // Single<BasicVersionsConfiguration> is not subtype of Single<VersionsConfiguration> :(
                .map(new Func1<JsonObject, VersionsConfiguration>() {
                    @Override
                    public VersionsConfiguration call(JsonObject json) {
                        return new BasicVersionsConfiguration(json.get(minimalAttributeName).getAsLong(), json.get(currentAttributeName).getAsLong());
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Single<? extends VersionsConfiguration>>() {
                    @Override
                    public Single<? extends VersionsConfiguration> call(Throwable throwable) {
                        return Single.error(new VersionFetchError());
                    }
                });
    }

    private ApiDescription getApi() {
        if (api == null) {
            api = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()))
                    .client(new OkHttpClient.Builder()
                            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build())
                    .build()
                    .create(ApiDescription.class);
        }
        return api;
    }
}
