plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.play.services)
}

android {
    namespace = "com.sidspace.stary"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sidspace.stary"
        minSdk = 31
        versionCode = 3
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "MOVIES_API_KEY","\"FF3PF1A-YQ6MXEK-NFQM9QD-76A6GH0\"")
        buildConfigField("String", "MOVIES_API_BASE_URL","\"https://api.kinopoisk.dev/v1.4/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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


/*kotlin{
    task("testClasses")
}*/

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //implementation(libs.androidx.room.common.jvm)
    //implementation(libs.androidx.room.runtime.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.kotlin.serialization)

    implementation(libs.core.splash.screen)



    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.coil.compose)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.compose.navigation)
    implementation(libs.viewmodel.compose.navigatiom)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.icons)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.com.android.google.auth)

    implementation(libs.androidx.webkit)




    implementation(libs.androidx.browser)

    //implementation("androidx.compose.ui:ui-text-google-fonts:1.7.6")
    implementation(libs.androidx.profileinstaller) // последнюю
    implementation(libs.cloudy)
    implementation(libs.play.services.auth.v2130)
    implementation(libs.firebase.auth.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.analytics)

    implementation(libs.firebase.messaging)

    implementation(project(":core:navigation"))
}
