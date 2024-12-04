# Clarity-examples

This project contains example code for the [clarity replay parser](https://github.com/skadistats/clarity).

## Introducti[.idea](.idea)on

This code is derived from the [Clarity examples](https://github.com/skadistats/clarity-examples) under a BSD-3 license.

### Building / Parsing using this codebase

All provided examples can be build with Gradle. The build process yields an "uno-jar", that is a jar 
containing all the dependencies, which can be called from the command line easily without having to 
set a correct classpath. Alternatively, you can use Gradle to run an example directly.

All following commands have to be issued in the root of the project.


#### Building

Windows:

    gradlew.bat player_statusPackage
    
Linux / Mac:

    ./gradlew player_statusPackage

#### Running the built uno-jar

Windows:

    java -jar build\libs\player_status.jar replay.dem 

Linux / Mac:

    java -jar build/libs/player_status.jar replay.dem

#### Running from Gradle

Windows:

    gradlew.bat player_statusRun --args "path\to\replay.dem" 

Linux / Mac:

    ./gradlew player_statusRun  --args "path/to/replay.dem"

#### Running on an entire subdirectory of dem.bz2 files (Windows only)

Windows:

    .\parse.bat "path\to\replay_folder" "path\to\output\directory"
