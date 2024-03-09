plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.mz.storeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mz.storeapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
    kapt {
        correctErrorTypes = true
    }

    hilt {

        enableExperimentalClasspathAggregation = true
    }

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.core.livedata.ktx)
    implementation(libs.core.runtime.ktx)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.swiperefreshlayout)

    implementation (libs.androidx.transition)
    // Room
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.coil)

    // Debug
    implementation(libs.timber)

    // ******* UNIT TESTING ******************************************************
    // use for testing live data
    testImplementation(libs.core.testing)
    testImplementation(libs.junit)

    // jvm test - Hilt
    kaptTest(libs.hilt.android.compiler)

    // assertion
    testImplementation(libs.test.hamcrest)
    testImplementation(libs.test.hamcrest.library)

    // mockito with kotlin
    testImplementation(libs.test.mockk)

    // coroutines unit test
    testImplementation(libs.coroutines.test)
    testImplementation(libs.coroutines.android.test)

    androidTestImplementation(libs.androidx.espresso.core)
}