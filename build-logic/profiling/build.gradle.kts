plugins {
    id("gradlebuild.build-logic.kotlin-dsl-gradle-plugin")
}

dependencies {
    implementation("com.gradle:gradle-enterprise-gradle-plugin")

    implementation(project(":basics"))
    implementation(project(":documentation"))
    implementation(project(":module-identity"))

    implementation("me.champeau.jmh:jmh-gradle-plugin")
    implementation("org.jsoup:jsoup")
}
