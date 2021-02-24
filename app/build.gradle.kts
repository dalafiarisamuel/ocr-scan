plugins {
    id(Plugins.androidApplication)
    kotlin(Plugins.android)
    kotlin(Plugins.androidExtension)
    kotlin(Plugins.kapt)
    id("name.remal.check-dependency-updates") version "1.2.2"
    id(Plugins.androidxNavigationsafeArgsKotlin)
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
    Dependencies.implementations.forEach { implementation(it) }
    SupportDependencies.supportImplementation.forEach { implementation(it) }
    TestDependencies.testImplementation.forEach { testImplementation(it) }
    AndroidTestDependencies.androidTestImplementation.forEach { androidTestImplementation(it) }
    AnnotationProcessors.AnnotationProcessorsImplementation.forEach { kapt(it) }
}