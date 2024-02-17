@echo off
setlocal enabledelayedexpansion

set "file=%1"

for /f "tokens=1,* delims=." %%a in ("!file!") do (
    set "filename=%%a"
    set "extension=%%b"
)

set "concatenation="

:concatenate
if not "%2"=="" (
    set "concatenation=!concatenation! %2"
    shift
    goto :concatenate
)

set "concatenation=!concatenation! extension:!extension!"

java Moteur.java %filename% %concatenation%

endlocal

@REM table:nomtable , type:(controller/controller_spring), package:nompackage