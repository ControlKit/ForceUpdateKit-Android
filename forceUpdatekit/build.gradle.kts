import javax.xml.parsers.DocumentBuilderFactory

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.10"
    id("jacoco")
}

android {
    namespace = "com.forceupdatekit"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        vectorDrawables {
            useSupportLibrary = true
        }
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

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(libs.androidx.material3)

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
// ✅ تولید گزارش JaCoCo فقط برای این ماژول
// ✅ گزارش JaCoCo فقط برای ماژول forceupdate
tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }

    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))

    classDirectories.setFrom(
        fileTree("${buildDir}/intermediates/javac/debug/classes") {
            include("com/forceupdatekit/**") // فقط پکیج ماژول
            exclude(
                "**/R.class",
                "**/R\$*.class",
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*Test*.*"
            )
        }
    )

    executionData.setFrom(
        fileTree(buildDir) {
            include("**/jacoco/testDebugUnitTest.exec")
        }
    )
}

// ✅ نمایش درصد دقیق در console
tasks.register("printCoverage") {
    dependsOn("jacocoTestReport")
    doLast {
        val xmlFile = file("${buildDir}/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
        if (!xmlFile.exists()) {
            println("Coverage XML not found!")
            return@doLast
        }

        val xml = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(xmlFile)

        val counters = xml.getElementsByTagName("counter")
        var covered = 0
        var missed = 0
        for (i in 0 until counters.length) {
            val c = counters.item(i)
            val type = c.attributes.getNamedItem("type").nodeValue
            if (type == "LINE") {
                covered += c.attributes.getNamedItem("covered").nodeValue.toInt()
                missed += c.attributes.getNamedItem("missed").nodeValue.toInt()
            }
        }
        val total = covered + missed
        val percent = if (total > 0) (covered * 100.0 / total) else 0.0
        println("📊 ForceUpdate coverage: %.2f%%".format(percent))
    }
}