plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.smeb9716"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.smeb9716"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    viewBinding {
        enable = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    ksp(libs.hilt.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.gson)
    // https://github.com/Spikeysanju/MotionToast
    implementation(libs.motionToast)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.convertergson)
    implementation(libs.interceptor)
    implementation(libs.ok2curl)
    // Lottie Splash
    implementation(libs.lottie)
    // Circle Image
    implementation(libs.circleimageview)
    // Glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    // Lifecycle
    implementation(libs.lifecycleviewmodel)

    // Fragment
    implementation(libs.fragment)

    // ImageSlideShow: https://github.com/denzcoskun/ImageSlideshow
    implementation(libs.imageslideshow)

    // Dagger Hilt
    implementation(libs.hilt.android)

    implementation(libs.timber)
    implementation(libs.toasty)

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Coroutines
    implementation(libs.coroutine.core)
    implementation(libs.coroutine.android)

    //viewpager2
    implementation(libs.viewpager2)

    // Flexbox
    implementation(libs.flexbox)

    //Expandeble Text View
    implementation ("io.github.glailton.expandabletextview:expandabletextview:1.0.4")

    //Circle Image View
    implementation ("de.hdodenhof:circleimageview:3.1.0")
}