# Android Version Status Resolver and Fetcher

## Purpose of library
Many applications have need to tell users to update current application because they have outdated version. Google Play doesnt support any API for that so we need to use custom solution - even some api request or service like Firebase Remote Configuration.

## Usage
The simplest way for use this library is
```java
 val versionFetcher = ...
 val versionHandler = new VersionHandler(versionFetcher)
 versionHandler.checkVersionStatusAndShowDefault(BuildConfig.VERSION_CODE, getSupportFragmentManager())
```

this will check versions and show update dialog with the right settings.

If you want to show custom UI with update info, you can use different method:

```java
val versionFetcher = ...
val versionHandler = new VersionHandler(versionFetcher)

versionHandler.checkVersionStatus(BuildConfig.VERSION_CODE)
    .subscribe({ status ->
        if(status == UP_TO_DATE) {
            // app is up to date
        } else if(status == UPDATE_AVAILABLE) {
            // update is available but is not mandatory
        } else {
            // update is mandatory, user should not be able to run app
        }, {err ->
            // some error happened while fetching version info
        })

```
VersionHandler class takes responsibility for deciding status of update based on fetched version info and current application version. It returns `Single<VersionStatus>`

Interface VersionFetcher should be used for actual fetching of version configurations.

Two basic implementation are provided in separate modules - Firebase Remote Config fetcher and Rest Api Fetcher

### Firebase Remote Config
Class `FirebaseVersionFetcher` takes optional argument for cache expiration of fetched data from Firebase Remote Config. Default names of attributes are `minimal_version_android` and `current_version_android` but you can modify that with constructor attributes.
```groovy
compile "cz.ackee.versionfetcher:firebase-fetcher:x.x.x"
```


### Rest Api
Class `RestVersionFetcher` accepts as argument base url of server. On url GET ${baseUrl}/app-version is expected response with attributes  `minimal_version_android` and `current_version_android` (names can be also modified)
```groovy
compile "cz.ackee.versionfetcher:rest-fetcher:x.x.x"
```
## Dependencies

```groovy
compile "cz.ackee.versionfetcher:status-resolver:x.x.x"
```
