plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)

}

android {
    namespace = "com.sidspace.stary.navigation"
    compileSdk = 36

    defaultConfig {
        minSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {


    implementation(libs.androidx.material.icons.extended)
    implementation(libs.icons)

    implementation(libs.javax.inject)
    implementation(projects.feature.home.presentation)
    implementation(projects.feature.movie.presentation)
    implementation(projects.feature.account.presentation)
    implementation(projects.feature.search.presentation)
    implementation(projects.feature.random.presentation)

    implementation(projects.feature.bookmark.presentation)
    implementation(projects.feature.collectionmovies.presentation)
    implementation(projects.feature.collections.presentation)
    implementation(projects.feature.folder.presentation)
    implementation(projects.feature.folders.presentation)
    implementation(projects.feature.person.presentation)
    implementation(projects.feature.review.presentation)
    implementation(projects.feature.viewed.presentation)
    implementation(projects.feature.error.presentation)

    implementation(libs.compose.navigation)
    implementation(libs.kotlin.serialization)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
