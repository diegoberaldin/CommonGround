plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.github.diegoberaldin.commonground"
    compileSdk = libs.versions.app.targetSdk.get().toInt()

    defaultConfig {
        applicationId = "com.github.diegoberaldin.commonground"
        minSdk = libs.versions.app.minSdk.get().toInt()
        targetSdk = libs.versions.app.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0-alpha"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.splashscreen)
    implementation(libs.coil)
    implementation(libs.koin)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp)

    implementation(projects.core.architecture)
    implementation(projects.core.appearance)
    implementation(projects.core.commonui)
    implementation(projects.core.l10n)
    implementation(projects.core.persistence)
    implementation(projects.core.utils)

    implementation(projects.domain.favorites)
    implementation(projects.domain.gallery)
    implementation(projects.domain.imageFetch.cache)
    implementation(projects.domain.imageFetch.fetcherImpl)
    implementation(projects.domain.imageFetch.lemmy)
    implementation(projects.domain.imageSource.repository)
    implementation(projects.domain.imageSource.usecase)
    implementation(projects.domain.palette)
    implementation(projects.domain.settings)

    implementation(projects.feature.drawer)
    implementation(projects.feature.favorites)
    implementation(projects.feature.imagedetail)
    implementation(projects.feature.imagelist)
    implementation(projects.feature.settings)
    implementation(projects.feature.settings.configSources)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}