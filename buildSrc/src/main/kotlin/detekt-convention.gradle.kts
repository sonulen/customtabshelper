plugins {
    id("io.gitlab.arturbosch.detekt")
}

// FIXME Переписать на плагин

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    this.jvmTarget = "1.8"
}
tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    this.jvmTarget = "1.8"
}

// FIXME Можно сделать чтоб детект не прерывался на 1ой ошибке в одном из проектов?
detekt {
    config = rootProject.files("config/detekt/detekt.yml")
    baseline = file("${projectDir}/config/lint/lint-baseline.xml")
    autoCorrect = false
    parallel = true
}

// FIXME Как убрать версию от сюда?
dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.20.0")
}
