# Shared Minecraft 1.12.2 Buildscripts

This repo was created with the goal of making a build script general enough and feature-rich enough so that any Minecraft 1.12.2 mod could adapt to it and not only work without issues, but gain many new features as well as receive technical support and updates if any problems are encountered using Gradle.

This build script was heavily inspired by the build script created by GT New Horizons for 1.7.10 mods, which can be found [here](https://github.com/GTNewHorizons/ExampleMod1.7.10). Much of the credit goes to them, as this script shares many similarities to theirs.

## Features
- [RetroFuturaGradle](https://github.com/GTNewHorizons/RetroFuturaGradle) as the gradle plugin, preferred over ForgeGradle as it is significantly improved and receiving support
    - This overall performs much better, and has signficantly faster build and decompile times compared to ForgeGradle, among many other feautres.
- Build script updating, including an option to automatically update whenever a gradle task is run and an update is available
- Automatic "Tag" creation, which can supply your Java code with the ModId, ModName, and Version generated for you by Gradle
- Access Transformers, can even have multiple
- Core Mods
- Mixin using MixinBooter 8 and Unimix, or specifying Mixin as a dependency if you do not use it but projects you depend on do
- Automatic repository settings, including common repositories such as CurseMaven, Modrinth Maven, and others
- Automatically include JEI and The One Probe in your project, at both compile-time and runtime
- CurseForge and Modrinth deployments
- Jabel for Java 17 language features while still targeting Java 8
- JUnit 5 added automatically
- Spotless auto-formatting with default formatter configurations, or user-specified configurations
- API and Source deployment alongside obf and deobf jars
- Deployment testing options, so that you can ensure your tokens and other variables are set up properly
- Custom username setting for development, that can be set either project-wide or via an environment variable for shared projects
- Ships with Jetbrains annotations by default, so you have another option over `javax`
- Kotlin and Scala support
- Custom Maven publishing location support, with all secrets held by system environment variables
- Automatic mixin JSON generation, as well as mcmod.info and pack.mcmeta if they are missing
- Dependency shadowing for releasing dependencies in your builds, more info [here](https://github.com/GregTechCEu/Buildscripts/blob/master/docs/shadow.md)
- Automatic changelog generation by generating a list of all commits between the current tag and the previous tag
- Automatic mod version detection from the latest git tag (or manually specified, if you prefer)
- Updated Launchwrapper for better ASM/mixin debugging options

And many more to come! And of course, all of these features are toggleable via an option in `buildscript.properties`,
a custom file separate from `gradle.properties` for ease of use.

## How to Install (New Project)
- Download the latest **starter.zip** release from [here](https://github.com/GregTechCEu/Buildscripts/releases) and extract into your project directory
- Import into your IDE (we recommend IntelliJ IDEA, as it has the best support for modded Minecraft)
- Choose a license for your code
- Ensure your project is initialized in git. For example, you can run `git init; git commit --message "initial commit"`
- Replace placeholder values, such as in `buildscript.properties`, package/class names for your `src/main` directory, etc.
- Run `./gradlew setupDecompWorkspace`
- Run `./gradlew updateBuildScript` to ensure that you are on the latest version
- You are good to go! You can now run the `runClient` run configuration or run `./gradlew runClient` to launch the game

## How to Install (Existing Project)
- Download the latest **migration.zip** release from [here](https://github.com/GregTechCEu/Buildscripts/releases)
- Rename your `build.gradle` file to `build.gradle.old`
- Copy the [`build.gradle`](https://github.com/GregTechCEu/Buildscripts/blob/master/build.gradle), [`dependencies.gradle`](https://github.com/GregTechCEu/Buildscripts/blob/master/dependencies.gradle), and [`repositories.gradle`](https://github.com/GregTechCEu/Buildscripts/blob/master/repositories.gradle) files from this zip to your project
- Copy the [`settings.gradle`](https://github.com/GregTechCEu/Buildscripts/blob/master/settings.gradle) file from this zip to your project and replace your current one
- Copy the [`buildscript.properties`](https://github.com/GregTechCEu/Buildscripts/blob/master/buildscript.properties) file from this zip to your project
- Configure `buildscript.properties` to adjust the `build.gradle` settings to suit your project
- Configure `gradle.properties` to create any property settings for use in other gradle files - primarily `dependencies.gradle`
- Move the necessary `dependencies` and/or `repositories` from `build.gradle.old` to the respective files (`dependencies.gradle`, `repositories.gradle`)
    - NOTE that if you enable the `includeWellKnownRepositories` option in `buildscript.properties`, this build script will automatically have the following Maven locations, meaning you don't need to add them yourself in `repositories.gradle`:
      1. Curse Maven
      2. Modrinth Maven
      3. BlameJared Maven
      4. CleanroomMC Maven
    - NOTE that if you enable the `includeCommonDevEnvMods` option in `buildscript.properties`, this build script will automatically have the following Mods, meaning you don't need to add them yourself in `dependencies.gradle`:
      1. JEI
      2. The One Probe
- Delete the `build.gradle.old` file
- And lastly, run `./gradlew updateBuildScript` to ensure that you are on the latest version

### Advanced
- If your project was using Mixins, you may get a new mixin config file generated as `mixins.{modid}.json`, if yours was not named this way. Currently, you will have to move your Mixin config options to this newly generated file. If this behavior does not suit your needs, feel free to open an issue and start a discussion on different behavior
- If your project was using environment variables for CI deployments, see the `buildscript.properties` file's comments to view the environment variable names this script checks for
- If you have any additional build script code that you need to be applied, create an `addon.gradle` file and put it there
- If your project has no dependencies, you can safely delete the `repositories.gradle` and `dependencies.gradle` files, they are optional

## How to Use
### Files
- `build.gradle`: This file is automatically updated, and as a result should not be modified by the user directly
- `buildscript.properties`: Contains all the user configuration for the build script
- `gradle.properties`: Contains all the user configuration for general gradle files
- `settings.gradle`: This file contains some basic setup of the build script. It is currently not versioned, so it is safe to add to, though should not have things removed from it
### Custom Files
Any of these files can optionally be either `.gradle` (Groovy), or `.gradle.kts` (Kotlin DSL).
- `dependencies.gradle[.kts]`: Add your mod's dependencies here. This is separate from the main build script so that you can replace `build.gradle` if an update is available
- `repositories.gradle[.kts]`: Add your dependencies' repositories here. Many common ones are already handled by `build.gradle`
- `addon.gradle[.kts]`:  An optional file, which can be used if you have any more advanced gradle commands. However, we are open to new features, so let us know if there is something you want that we could add to this build script to avoid needing this file!

### Custom Gradle Tasks
- `./gradlew updateBuildScript`
    - Updates your build script to the latest version, if there is an update available
    - Running any gradle task will let you know if there is an available update
- `./gradlew faq`
    - A short FAQ explaining an abbreviated version of the information in this section of the readme
- `./gradlew generateAssets`
    - Automatically generates a Mixin config file, if one does not already exist, and if you have your project configured to use Mixin
