@echo off
setlocal enabledelayedexpansion

set "concatenation="

:concatenate
if not "%1"=="" (
    set "concatenation=!concatenation! %1"
    shift
    goto :concatenate
)


java Generation.java %concatenation%

endlocal

@REM table:nomtable , make:(model/crud), namespace:package, class:nom_classe