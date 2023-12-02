@echo off
setlocal enabledelayedexpansion
(for %%i in (*.mp4) do @echo file '%%i') > list.txt
set prefix=:v]xfade=transition=fade:duration=1:offset=
FOR /F "tokens=2 delims='" %%a IN (list.txt) do ( ffprobe -i %%a -show_entries format=duration -v quiet -of csv="p=0" >> time.txt)
set counter=0
set clipindex=1
echo ^"[0:v]>filterset.txt
for /f "delims=" %%k in (time.txt) do (
    echo [!counter!%prefix%%%k[clip!clipindex!]; \ >> filterset.txt
    set /a counter+=1
	set /a clipindex+=1
)
del time.txt
type filterset.txt
del filterset.txt
endlocal
