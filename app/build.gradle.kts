import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.9.22"
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "dev.mjanusz.recruitmentapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.mjanusz.recruitmentapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildFeatures.buildConfig = true
        mapOf(
            "GITHUB_API_TOKEN" to "github.api_token",
            "GITHUB_API_BASE_URL" to "github.base_url",
        ).forEach {
            buildConfigField("String", it.key,
                "\"${gradleLocalProperties(rootDir).getProperty(it.value)}\""
            )
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    val apiDimension = "api"
    flavorDimensions += apiDimension

    productFlavors {
        create("staging") {
            dimension = apiDimension
            versionNameSuffix = "-STAGING"
            applicationIdSuffix = ".staging"
        }
        create("prod") {
            dimension = apiDimension
            /* prod flavor, inherit */
        }
    }

    androidComponents {
        beforeVariants { variantBuilder ->
            if (variantBuilder.buildType == "release" && variantBuilder.flavorName == "staging") {
                /* Disable useless staging-release variant */
                variantBuilder.enable = false
            }
        }
    }

    sourceSets {
        val mainDir = "src/main/java"
        val debugDir = "src/debug/java"
        val releaseDir = "src/release/java"
        getByName("debug") {
            java.srcDirs(mainDir, debugDir)
        }
        getByName("release") {
            java.srcDirs(mainDir, releaseDir)
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    /* Android */
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    /* Navigation */
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    /* Retrofit */
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    /* Paging */
    implementation("androidx.paging:paging-compose:3.2.1")
    implementation("androidx.room:room-paging:2.6.1")

    /* Hilt */
    implementation("com.google.dagger:hilt-android:2.50")
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("com.google.dagger:hilt-android-compiler:2.50")
    kapt("androidx.hilt:hilt-compiler:1.2.0")

    /* Room */
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    /* Coil */
    implementation("io.coil-kt:coil-compose:2.5.0")

    /* Unit testing */
    testImplementation(kotlin("test"))
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation("com.google.truth:truth:1.4.1")
    testImplementation("io.mockk:mockk:1.13.9")


    /* UI testing */
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.50")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.50")

    /* Debug tooling */
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    debugImplementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}