import javax.xml.parsers.DocumentBuilderFactory
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("jacoco")
}

version = "0.0.3"

android {
    namespace = "com.forceupdatekit"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        
        buildConfigField("int", "LIB_VERSION_CODE", "3")
        buildConfigField("String", "LIB_VERSION_NAME", "\"${project.version}\"")
        consumerProguardFiles("consumer-rules.pro")
        vectorDrawables {
            useSupportLibrary = true
        }
        
        // Load API URL from local.properties
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { localProperties.load(it) }
        }
        
        val apiUrl = localProperties.getProperty("API_URL") ?: "https://example.com/api/force-updates"
        buildConfigField("String", "API_URL", "\"$apiUrl\"")
    }
    buildFeatures {
        compose = true
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(libs.androidx.material3)
    implementation(libs.errorhandler)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    implementation(libs.ui.tooling.preview)

    implementation(libs.coil.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    //REST - APIService

    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)
    implementation(libs.converter.gson)
    debugImplementation(libs.ui.tooling)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.coroutines.test)

}
jacoco {
    toolVersion = "0.8.10"
}

tasks.withType<Test> {
    useJUnit()
    finalizedBy("jacocoTestReport")
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    
    // Add explicit dependencies to fix Gradle validation issues
    mustRunAfter("generateDebugAndroidTestResValues")
    mustRunAfter("syncDebugLibJars")
    mustRunAfter("syncReleaseLibJars")
    mustRunAfter("generateDebugAndroidTestLintModel")
    mustRunAfter("lintAnalyzeDebugAndroidTest")
    mustRunAfter("mergeReleaseResources")
    mustRunAfter("extractProguardFiles")
    mustRunAfter("mergeDebugJavaResource")
    mustRunAfter("mergeDebugJniLibFolders")
    mustRunAfter("mergeReleaseJniLibFolders")
    mustRunAfter("mergeReleaseJavaResource")
    mustRunAfter("copyDebugJniLibsProjectAndLocalJars")
    mustRunAfter("copyReleaseJniLibsProjectAndLocalJars")
    mustRunAfter("copyDebugJniLibsProjectOnly")
    mustRunAfter("copyReleaseJniLibsProjectOnly")
    mustRunAfter("generateDebugLintModel")
    mustRunAfter("generateReleaseLintModel")
    mustRunAfter("lintAnalyzeDebug")
    mustRunAfter("generateDebugLintReportModel")
    mustRunAfter("generateReleaseLintVitalModel")
    mustRunAfter("lintVitalAnalyzeRelease")
    mustRunAfter("bundleLibRuntimeToDirDebug")
    mustRunAfter("bundleLibRuntimeToDirRelease")
    mustRunAfter("generateDebugUnitTestLintModel")
    mustRunAfter("lintAnalyzeDebugUnitTest")
    mustRunAfter("verifyReleaseResources")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val mainSrc = files(
        "${project.projectDir}/src/main/java",
        "${project.projectDir}/src/main/kotlin"
    )

    val debugTree = files(
        fileTree("${buildDir}/tmp/kotlin-classes/debug") {
            exclude(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*"
            )
        },
        fileTree("${buildDir}/intermediates/javac/debug/classes") {
            exclude(
                "**/R.class",
                "**/R$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*"
            )
        }
    )

    sourceDirectories.setFrom(mainSrc)
    classDirectories.setFrom(debugTree)
    executionData.setFrom(fileTree(buildDir) {
        include("**/*.exec", "**/*.ec")
    })
}