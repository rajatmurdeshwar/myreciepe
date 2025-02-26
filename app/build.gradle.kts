import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.murdeshwar.myrecipe"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.murdeshwar.myrecipe"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "1.2.2"


        testInstrumentationRunner = "com.murdeshwar.myrecipe.HiltTestRunner"

        buildConfigField("String", "RECIPE_BASE_URL", "\"https://api.spoonacular.com/\"")
        val localProperties = project.rootProject.file("local.properties")
        val properties = Properties().apply {
            if (localProperties.exists()) {
                load(localProperties.inputStream())
            }
        }
        val recipeApiKey = properties.getProperty("RECIPE_API_KEY", "")
        buildConfigField("String", "RECIPE_API_KEY", "\"$recipeApiKey\"")
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    flavorDimensions += "version"

    productFlavors {
        create("demo") {
            dimension = "version"
            applicationIdSuffix = ".demo"
            versionNameSuffix = "-demo"
        }
        create("full") {
            dimension = "version"
            applicationIdSuffix = ".demo"
            versionNameSuffix = "-demo"

        }

    }

    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isCrunchPngs = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
    }
    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.glide)
    implementation(libs.jsoup)
    implementation(libs.timber)
    implementation(libs.retrofit)
    implementation(libs.retrofit.covertor)

    // Architecture Components
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.paging.common.android)
    ksp(libs.room.compiler)
    implementation(libs.accompanist.swiperefresh)

    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)

    // Hilt
    implementation(libs.hilt.android.core)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.archcore.testing)
    testImplementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.google.truth)


    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.archcore.testing)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // AndroidX Test - Hilt testing
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)
}