cls
@echo off
::echo ffmpeg -y>>tempffmpegmuxer.txt
setlocal enableextensions enabledelayedexpansion
(for %%i in (*.mp4) do @echo %%i) > list.txt
(for %%i in (*.mp4) do @echo file '%%i') > clipduration.txt
FOR /F "tokens=2 delims='" %%a IN (clipduration.txt) do ( ffprobe -i %%a -show_entries format=duration -v quiet -of csv="p=0" >> forprocessing.txt)
(for /F "tokens=1" %%i IN (list.txt) do set /p filenames=-i %%i <nul %filenames%) > filename.txt
(for /F "tokens=*" %%i IN (filename.txt) do set /p filenames=ffmpeg -y %%i <nul %filenames% <nul -filter_complex ^"[0:V]) > tempffmpegmuxer.txt


::set /a count = 0
::for /f "tokens=*" %%a in (list.txt) do (
 :: set /a count += 1
  ::echo [!count!:v]xfade^=transition^=fade:duration^=1:offset^=[clip!count!])


for /f "delims=" %%a in (forprocessing.txt) do (set "var=%%a"<nul & goto :stop)
:stop
echo %var:~0,-4%>>forprocessing.txt
set /p foo=%var:~0,-4%<nul
set /A foo=foo-1
!foo!

::if exist forprocessing.txt (
::    set /p foo=<forprocessing.txt
::    echo "read: !foo!"
::    set /A foo=foo-1
::    (echo !foo!)>forprocessing.txt
::    echo "wrote: !foo!"
::) else (
::    (echo 1)>test.txt
::)
  ::echo 
::FOR /F "tokens=2 delims='" %%a IN (list.txt) do ( ffprobe -i %%a -show_entries format=duration -v quiet -of csv="p=0" >> timeo.txt)
::FOR /F "tokens=1 delims= " %%a IN (timeo.txt) do (set /A firstduration=%%a <nul)
::%firstduration%
::set /A firstoffsetduration=%firstduration%-1
::%firstoffsetduration%
::type timeo.txt
::type tempffmpegmuxer.txt
del list.txt
del filename.txt
::del timeo.txt
del tempffmpegmuxer.txt
del forprocessing.txt

endlocal