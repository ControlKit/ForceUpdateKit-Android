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
tasks.withType<Test> {
    // فعلاً JUnit 4 استفاده می‌کنی، پس useJUnitPlatform لازم نیست
    testLogging {
        events("passed", "skipped", "failed")
    }
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val debugTree = fileTree("${buildDir}/tmp/kotlin-classes/debug") {
        exclude("**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*")
    }

    classDirectories.setFrom(debugTree)
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    executionData.setFrom(fileTree(buildDir) {
        include("**/jacoco/testDebugUnitTest.exec")
    })
}

tasks.register("printCoverage") {
    dependsOn("jacocoTestReport")
    doLast {
        val reportFile = file("${project.projectDir}/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml")
        if (!reportFile.exists()) {
            println("⚠️ Jacoco XML report not found at: ${reportFile.absolutePath}")
            return@doLast
        }

        val factory = javax.xml.parsers.DocumentBuilderFactory.newInstance()
        factory.isValidating = false
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)

        val parser = factory.newDocumentBuilder()
        val doc = parser.parse(reportFile)
        val counters = doc.getElementsByTagName("counter")

        var covered = 0
        var missed = 0
        for (i in 0 until counters.length) {
            val node = counters.item(i)
            val type = node.attributes.getNamedItem("type").nodeValue
            if (type == "INSTRUCTION") {
                covered = node.attributes.getNamedItem("covered").nodeValue.toInt()
                missed = node.attributes.getNamedItem("missed").nodeValue.toInt()
                break
            }
        }

        val total = covered + missed
        val percent = if (total > 0) covered * 100.0 / total else 0.0
        println("📊 ForceUpdateKit coverage: ${"%.2f".format(percent)}%")
    }
}