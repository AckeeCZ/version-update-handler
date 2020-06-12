[ ![Download](https://api.bintray.com/packages/ackeecz/version-update-handler/status-resolver/images/download.svg) ](https://bintray.com/ackeecz/version-update-handler/status-resolver/_latestVersion)

# Android Version Status Resolver and Fetcher

## Purpose of library
Many applications have need to tell their users to update current application because they have outdated version. Google Play doesn't support any API for that so we need to use custom solution.  This library will compare current application version number to the versions that are stored in remote locations (Api request/Firebase Remote Config, ...) and show the dialog to the user with prompt to update their app.


This library is based on Kotlin Coroutines
## Usage
The simplest way for use this library is
```kotlin
 val versionFetcher = ...
 val versionResolver = VersionStatusResolver(versionFetcher)
 versionResolver.checkVersionStatusAndOpenDialog(BuildConfig.VERSION_CODE, getSupportFragmentManager())
```

this will check versions and show update dialog with default texts. If you want to modify strings in dialog and maybe package name, you can via DialogSettings.Builder() class.

```kotlin
 val versionFetcher = ...
 val versionResolver = VersionStatusResolver(versionFetcher)
 versionResolver.checkVersionStatusAndOpenDialog(BuildConfig.VERSION_CODE, getSupportFragmentManager(),
                            DialogSettings.Builder()
                            .title("My custom title")
                            .messageRes(R.string.update_dialog_message)
                            .positiveButton("Yaay")
                            .negativeButton("Never")
                            .build())
```


If you want to show UI that built-in dialog cannot handle, you can use different method and based on result show corresponding UI:

```kotlin
val versionFetcher = ...
val versionResolver = VersionStatusResolver(versionFetcher)

versionResolver.checkVersionStatus(BuildConfig.VERSION_CODE)
    .subscribe({ status ->
        when(status) {
            UP_TO_DATE -> // app is up to date
            UPDATE_AVAILABLE -> // update is available but is not mandatory
            else ->// update is mandatory, user should not be able to run app
        }}, {err ->
            // some error happened while fetching version info
        })

```
VersionStatusResolver class takes responsibility for deciding status of update based on fetched version info and current application version. It returns `Single<VersionStatus>`

Interface VersionFetcher should be used for actual fetching of version configurations.

Two basic implementation are provided in separate modules - Firebase Remote Config fetcher and Rest Api Fetcher

### Firebase Remote Config
Class `FirebaseVersionFetcher` takes optional argument for cache expiration of fetched data from Firebase Remote Config. Default names of attributes are `minimal_version_android` and `current_version_android` but you can modify that with constructor attributes.
```groovy
implementation "cz.ackee.updatechecker:firebase-fetcher:x.x.x"
```
Besides that, you need to add a dependency to the Firebase Remote Config since it is not transitive.
```groovy
implementation "com.google.firebase:firebase-config:x.x.x"
```


### Rest Api
Class `RestVersionFetcher` accepts as argument base url of server. On url GET ${baseUrl}/app-version is expected response with attributes  `minimal_version_android` and `current_version_android` (names can be also modified)
```groovy
compile "cz.ackee.updatechecker:rest-fetcher:x.x.x"
```
## Dependencies
For current version check `gradle.properties` file
```groovy
compile "cz.ackee.updatechecker:status-resolver:x.x.x"
```


### Sample

In the `app` module is sample that demonstrates basic usage of library with mocked fetcher.


## License
Copyright 2018 Ackee s.r.o.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
