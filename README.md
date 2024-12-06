# Clarity-examples

This project contains example code for the [clarity replay parser](https://github.com/skadistats/clarity). The intended usage is to parse dota2 game (.dem) files for usage with [CS584-Project](https://github.com/CombustibleToast/584-Project).

## Introduction

This code is derived from the [Clarity examples](https://github.com/skadistats/clarity-examples) under a BSD-3 license.

### Building / Parsing using this codebase

All following commands have to be issued in the root of the project.

#### Building

Windows:

    gradlew.bat player_statusPackage
    
Linux / Mac:

    ./gradlew player_statusPackage

#### Running the built player_status.jar (runs on unzipped .dem.bz2 files)

Windows:

    java -jar build\libs\player_status.jar replay.dem 

Linux / Mac:

    java -jar build/libs/player_status.jar replay.dem

#### Running from Gradle (runs on unzipped .dem.bz2 files)

Windows:

    gradlew.bat player_statusRun --args "path\to\replay.dem" 

Linux / Mac:

    ./gradlew player_statusRun  --args "path/to/replay.dem"

#### Running on an entire subdirectory of dem.bz2 files (Windows only)

Windows:

    .\parse.bat "path\to\replay_folder" "path\to\output\directory"
