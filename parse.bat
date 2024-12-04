@echo on
setlocal EnableDelayedExpansion
set filePath=%1
set destPath=%2

for %%a in ("%filePath%\*.bz2") do (
@REM   echo Processing %%a

REM Extract the filename without extension

set fileName=%%~na

echo !fileName!

REM Unzip the file
"C:\Program Files\7-Zip\7z.exe" x "%%a" -o"%temp%"

echo "%filePath%\!fileName!"

REM Run the java command
java -jar .\build\libs\player_status.jar "%temp%\\!fileName!" > "%destPath%\\player_status_!fileName!.csv"

REM Delete the unzipped file
del "%temp%\!fileName!"

echo Done with %%a
)
pause