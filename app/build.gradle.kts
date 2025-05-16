plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.firebase.crashlytics")
    kotlin("android")
}


android {
    compileSdkVersion(Configuration.compileSdk)

    defaultConfig {
        applicationId = Configuration.artifactGroup
        minSdkVersion(Configuration.minSdk)
        targetSdkVersion(Configuration.targetSdk)
        versionCode = Configuration.versionCode
        versionName = Configuration.versionName
        multiDexEnabled = true // important

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true

            // Includes the default ProGuard rules files that are packaged with
            // the Android Gradle plugin. To learn more, go to the section about
            // R8 configuration files.
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(kotlin("stdlib", Versions.kotlin))
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.preferenceKtx)
    implementation(Dependencies.material)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.lifecycleLiveDataKtx)
    implementation(Dependencies.lifecycleViewModelKtx)
    implementation(Dependencies.navigationFragmentKtx)
    implementation(Dependencies.navigationUiKtx)
    implementation(Dependencies.legacySupportV4)
    implementation(Dependencies.junit)
    androidTestImplementation(Dependencies.androidTestJUnit)
    androidTestImplementation(Dependencies.espressoCore)

    // Retrofit client
    implementation(Dependencies.gson)
    implementation(Dependencies.retrofit)
    implementation(Dependencies.okhttpLoggingInterceptor)

    // In app update feature
    implementation(Dependencies.playCore)

    // Firebase
    implementation(platform(Dependencies.firebaseBom))
    implementation(Dependencies.firebaseAnalyticsKtx)
    implementation(Dependencies.firebaseCrashlytics)

    // Ad
    implementation(Dependencies.googleAds)

    // WorkManager
    implementation(Dependencies.workManager)

    // Room
    implementation(Dependencies.roomRuntime)
    kapt(Dependencies.roomCompiler)

    // Coroutines
    implementation(Dependencies.kotlinxCoroutinesCore)
    implementation(Dependencies.kotlinxCoroutinesAndroid)
}