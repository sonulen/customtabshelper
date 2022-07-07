plugins {
    id("io.gitlab.arturbosch.detekt")
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    this.jvmTarget = "1.8"
}
tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    this.jvmTarget = "1.8"
}

detekt {
    config = rootProject.files("config/detekt/detekt.yml")
    baseline = file("${projectDir}/config/lint/lint-baseline.xml")
    autoCorrect = false
    parallel = true
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.20.0")
}
