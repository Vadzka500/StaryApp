plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.play.services)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.sidspace.stary"
    compileSdk = 36


    defaultConfig {
        applicationId = "com.sidspace.stary"
        minSdk = 31
        targetSdk = 36
        versionCode = 5
        versionName = "1.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "MOVIES_API_KEY", "\"FF3PF1A-YQ6MXEK-NFQM9QD-76A6GH0\"")
        buildConfigField("String", "MOVIES_API_BASE_URL", "\"https://api.kinopoisk.dev/v1.4/\"")


        resourceConfigurations += setOf("ru", "en")

    }

    signingConfigs {
        create("release") {
            storeFile = File(rootDir, "sidspacekeystore.jks")
            keyPassword = "8870606v"
            keyAlias = "stary"
            storePassword = "8870606v"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }

        debug {

        }
    }



    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true

    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    "baselineProfile"(projects.baselineprofile)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.compose.navigation)

    implementation(libs.play.services.auth.v2130)
    implementation(libs.firebase.auth.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.analytics)


    implementation(projects.core.navigation)
    implementation(projects.core.logger)
}
