import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("java")
	kotlin("jvm") version("1.8.21")
	id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories {
	mavenLocal()
	maven {
		url = uri("https://repo.runelite.net")
	}
	mavenCentral()
}

val runeLiteVersion = "latest.release"

dependencies {
	compileOnly(group = "net.runelite", name = "client", version = runeLiteVersion)
	compileOnly("org.projectlombok:lombok:1.18.20")
	annotationProcessor("org.projectlombok:lombok:1.18.20")
	testImplementation("junit:junit:4.13.1")
	testImplementation(group = "net.runelite", name = "client", version = runeLiteVersion)
	testImplementation(group = "net.runelite", name = "jshell", version = runeLiteVersion)
	implementation("org.json:json:20230227")
}

group = "com.example"
version = "4.3"

val javaMajorVersion = JavaVersion.VERSION_11.majorVersion

tasks {
	withType<JavaCompile> {
		options.encoding = "UTF-8"
		sourceCompatibility = javaMajorVersion
		targetCompatibility = javaMajorVersion
	}
	withType<KotlinCompile> {
		kotlinOptions.jvmTarget = javaMajorVersion
	}
	withType<Jar> {
		manifest {
			attributes["Main-Class"] = "com.example.Main"
		}
	}
	withType<ShadowJar> {
		baseName = "EthanVannPlugins"
	}
}