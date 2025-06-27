plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
   //alias(libs.plugins.kotlin.jvm)

    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.navwithapinothing_2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.navwithapinothing_2"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.compose.navigation)
    implementation(libs.kotlin.serialization)

    implementation(libs.core.splash.screen)


    implementation(libs.retrofit)
    implementation (libs.converter.gson)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.coil.compose)

    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)
   // implementation(libs.accompanist.pager)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)


    implementation(libs.viewmodel.compose.navigatiom)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.com.android.google.auth)

    implementation(libs.androidx.webkit)


    implementation(libs.icons)

    kapt(libs.androidx.room.compiler.android)
    implementation(libs.androidx.browser)

    //implementation("androidx.compose.ui:ui-text-google-fonts:1.7.6")
    implementation(libs.androidx.profileinstaller) // последнюю
    implementation(libs.cloudy)
}
