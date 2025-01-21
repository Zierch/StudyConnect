plugins {
    alias(libs.plugins.android.application)
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.studyconnect"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.studyconnect"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    viewBinding {
        enable = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    packagingOptions {
        resources {
            excludes.add("META-INF/gradle/incremental.annotation.processors")
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

//    Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

//    viewmodel
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)

//    Dagger Hilt
    implementation(libs.hilt.android)
    annotationProcessor(libs.hilt.android.compiler)

//    Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

//    Preference
    implementation(libs.preference)
    implementation(libs.glide);

//    PdfViewer
    implementation(libs.android.pdf.viewer){
        exclude(group = "com.android.support", module = "support-compat")
    }
}