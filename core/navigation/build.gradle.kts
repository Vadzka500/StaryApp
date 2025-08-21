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
    implementation(project(":feature:home:presentation"))
    implementation(project(":feature:movie:presentation"))
    implementation(project(":feature:account:presentation"))
    implementation(project(":feature:search:presentation"))
    implementation(project(":feature:random:presentation"))

    implementation(project(":feature:bookmark:presentation"))
    implementation(project(":feature:collectionmovies:presentation"))
    implementation(project(":feature:collections:presentation"))
    implementation(project(":feature:folder:presentation"))
    implementation(project(":feature:folders:presentation"))
    implementation(project(":feature:person:presentation"))
    implementation(project(":feature:review:presentation"))
    implementation(project(":feature:viewed:presentation"))
    //implementation(project(":feature:home:data"))

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