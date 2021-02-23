plugins {
    id(Plugins.androidApplication)
    kotlin(Plugins.android)
    kotlin(Plugins.androidExtension)
    kotlin(Plugins.kapt)
    id("name.remal.check-dependency-updates") version "1.2.2"
    id (Plugins.androidxNavigationsafeArgsKotlin)
}

android {
    compileSdkVersion(Versions.compilesdk)

    defaultConfig {
        applicationId = Application.id
        minSdkVersion(Versions.minsdk)
        targetSdkVersion(Versions.targetsdk)
        versionCode = Application.versionCode
        versionName = Application.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
    }

    buildTypes {

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        targetCompatibility = Java.javaVersion
        sourceCompatibility = Java.javaVersion
    }

    kotlinOptions {
        jvmTarget = Java.javaVersion.toString()
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Dependencies.kotlinStandardLibrary)
    implementation(Dependencies.ktx)
    implementation(SupportDependencies.appcompat)
    implementation(SupportDependencies.constraintlayout)
    implementation(SupportDependencies.materialDesign)
    implementation(Dependencies.androidxLegacySupport)
    implementation(Dependencies.androidxAnnotation)
    implementation(Dependencies.androidXVectorDrawable)
    testImplementation(TestDependencies.junit4)
    androidTestImplementation(AndroidTestDependencies.androidxTestExt)
    androidTestImplementation(AndroidTestDependencies.espressoCore)
    implementation(Dependencies.kotlinCoroutineAndroid)
    implementation(Dependencies.kotlinCoroutineCore)
    implementation(Dependencies.retrofit2)
    implementation(Dependencies.retrofitMoshiConverter)
    implementation(Dependencies.moshiKotlin)
    kapt(AnnotationProcessors.moshiKotlinCodgen)
    implementation(Dependencies.ioCardAndroidSdk)
    implementation(Dependencies.lifecycycleViewModel)
    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.roomRuntime)
    kapt(AnnotationProcessors.roomCompiler)
    implementation(Dependencies.roomKtx)
    implementation(Dependencies.navigationFragment)
    implementation(Dependencies.navigationUi)
    implementation(Dependencies.lifecycleRuntime)
    implementation(Dependencies.lifecycleCommonJava8)
    implementation(Dependencies.lifecycleViewModelKtx)
    implementation(Dependencies.lifecycleLiveData)

}