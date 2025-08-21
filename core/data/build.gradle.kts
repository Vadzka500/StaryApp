plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
    //alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

android {
    namespace = "com.sidspace.stary.data"
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

}

room{
    schemaDirectory("${rootProject.projectDir}/schemas")
}

dependencies {

    implementation(project(":core:domain"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)



    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.runtime.android)
        kapt(libs.androidx.room.compiler.android)

    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.retrofit)

    implementation (libs.converter.gson)

    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation(libs.kotlin.serialization)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}