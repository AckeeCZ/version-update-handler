object Config {
    const val kotlinVersion = "1.3.72"

    const val minSdk = 15
    const val compileSdk = 29
    const val targetSdk = 29
}

object Dependencies {

    // Kotlin
    private const val kotlinCoroutinesVersion = "1.3.7"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Config.kotlinVersion}"
    const val kotlinStdLibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Config.kotlinVersion}"
    const val kotlinCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion"
    const val kotlinCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutinesVersion"
    const val kotlinCoroutinesPlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$kotlinCoroutinesVersion"

    // AndroidX
    private const val androidXVersion = "1.1.0"
    const val androidXAppCompat = "androidx.appcompat:appcompat:$androidXVersion"

    // Core Ktx
    private const val coreKtxVersion = "1.3.0"
    const val coreKtx = "androidx.core:core-ktx:$coreKtxVersion"

    // Firebase
    private const val firebaseConfigVersion = "20.0.3"
    const val firebaseConfig = "com.google.firebase:firebase-config:$firebaseConfigVersion"

    // Retrofit
    private const val retrofitVersion = "2.9.0"
    const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    const val retrofitConverterGson = "com.squareup.retrofit2:converter-gson:$retrofitVersion"

    // Anko
    private const val ankoVersion = "0.10.8"
    const val ankoSdk15 = "org.jetbrains.anko:anko-sdk15:$ankoVersion"
    const val ankoSdk21Coroutines = "org.jetbrains.anko:anko-sdk21-coroutines:$ankoVersion"
    const val ankoAppcompat = "org.jetbrains.anko:anko-appcompat-v7:$ankoVersion"

    // JUnit
    private const val jUnitVersion = "4.13"
    const val jUnit = "junit:junit:$jUnitVersion"

    // Truth
    private const val truthVersion = "1.0.1"
    const val truth = "com.google.truth:truth:$truthVersion"

    // MockWebServer
    private const val mockWebServerVersion = "4.7.2"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$mockWebServerVersion"
}